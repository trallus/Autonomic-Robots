package de.httpServer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.data.DBUser;
import de.logger.Log;
import de.persistence.CRUDIF;
import de.persistence.PersistenceFacade;

/**
 * @author ko
 *
 */
public class UserManager {

	/**
	 * Buffer for all loged in users
	 */
	private List<User> userList = new ArrayList<User>();
	/**
	 * connection to database
	 */
	public final CRUDIF db;
	/**
	 *  connection to database
	 */
	private final PersistenceFacade persistence;
	/**
	 * to get new session IDs
	 */
	private final SessionManager sessionManager;

	/**
	 * 
	 */
	public UserManager() {

		sessionManager = new SessionManager();
		// Start Database
		persistence = new PersistenceFacade();
		persistence.startDBSystem();
		// Get Database Controller
		db = persistence.getDBController();

	}
	
	/**
	 * 
	 */
	public void shutDown () {
		persistence.shutdownDBSystem();
	}

	/**
	 * update a user in database
	 * 
	 * @param user
	 */
	public void upDate(User user) {
		persistence.beginTransaction();
		db.update(user.getDBUser());
		persistence.commitTransaction();
	}

	/**
	 * get the user with this SID or create a new one
	 * 
	 * @param sessionID
	 * @return
	 */
	public User getUser(String sessionID) {

		if (sessionID != null) {
			for (User u : userList) {
				if (u.sessionID.equals(sessionID)) {
					return (u);
				}
			}
		}

		return (createUser());
	}

	/**
	 * registrate e new user in database
	 * 
	 * @param userName
	 * @param eMail
	 * @param password
	 * @param user
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws EmailInUseException
	 */
	public String register(String userName, String eMail, String password, User user)
			throws NoSuchAlgorithmException, EmailInUseException {
		// check if username exist
		List<DBUser> users = db.readAll(DBUser.class);
		for (DBUser dbUser : users) {
			if (dbUser.getEMail().equals(eMail)) {
				Log.debugLog("Registration failes. Email already registered: " + eMail);
				throw (new EmailInUseException());
			}
		}

		// create new user in database
		String passwordHash = convertToMD5Hash(password);
		DBUser dbUser = new DBUser(userName, eMail, passwordHash);
		persistence.beginTransaction();
		db.insert(dbUser);
		persistence.commitTransaction();
		user.setDBUser(dbUser);
		Log.debugLog("User registed: " + userName);
		return ("Registrierung erfolgreich");
	}

	/**
	 * log in a user
	 * 
	 * @param eMail
	 * @param password
	 * @param user
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws EmailNotFoundException
	 */
	public String logIn(String eMail, String password, User user)
			throws NoSuchAlgorithmException, EmailNotFoundException {

		// get all users from db
		List<DBUser> users = db.readAll(DBUser.class);
		// find the user with this name
		for (DBUser dbUser : users) {
			if (dbUser.getEMail().equals(eMail)) {
				// test the password
				final String passwordHash = convertToMD5Hash(password);
				if (dbUser.getPassword().equals(passwordHash)) {
					user.logIn(dbUser);
					Log.debugLog("Log in erfolgreich. User: " + user.toString());
					return ("Log in erfolgreich");
				} else {
					break;
				}
			}
		}

		Log.debugLog("Log in fehlgeschlagen. User: " + user.toString());
		throw (new EmailNotFoundException());
	}

	/**
	 * log out a user
	 * 
	 * @param user
	 */
	public void logOut(User user) {
		userList.remove(user);
		user.logOut();
		Log.debugLog("User loged out");
	}
	
	/**
	 * delete a user from database
	 * 
	 * @param user
	 */
	public void removeUser ( User user ) {
		persistence.beginTransaction();
		db.remove(user.getDBUser());
		persistence.commitTransaction();
		Log.debugLog("User removed: eMail:" + user.getDBUser().getEMail());
	}

	/**
	 * create a new user
	 * 
	 * @return
	 */
	private User createUser() {
		final String sessionID = sessionManager.getSessionID();

		final User u = new User(sessionID);
		userList.add(u);

		Log.debugLog("create User: " + u.toString());

		return (u);
	}

	/**
	 * create a md5 sum
	 * 
	 * @param string
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String convertToMD5Hash(String string)
			throws NoSuchAlgorithmException {

		// generate md5 String
		byte[] pwBytes = string.getBytes();

		MessageDigest md = MessageDigest.getInstance("MD5");

		byte[] byteHash = md.digest(pwBytes);

		String md5String = "";

		for (byte b : byteHash) {
			md5String += b;
		}

		return (md5String);
	}
}
