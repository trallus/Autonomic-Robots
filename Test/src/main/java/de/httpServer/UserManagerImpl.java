package de.httpServer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import de.data.DBUser;
import de.logger.Log;
import de.persistence.CRUDIF;
import de.persistence.PersistenceFacade;

/**
 * @author ko
 *
 */
public class UserManagerImpl implements UserManager {

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
	public final PersistenceFacade persistence;
	/**
	 * to get new session IDs
	 */
	private final SessionManager sessionManager;

	/**
	 * 
	 */
	public UserManagerImpl() {

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
	 * remove all Users from Database
	 */
	public void clareDB () {
		List<DBUser> userList = db.readAll(DBUser.class);
		for ( DBUser u : userList ) {
			db.remove(u);
		}
	}

	/**
	 * update a user in database
	 * 
	 * @param user
	 */
	public void upDate(User user) {
		db.update(user.getDBUser());
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
		// check if email already registered
		if ( getUserWithEmail (eMail) != null ) {
			Log.debugLog("Registration failes. Email already registered: " + eMail);
			throw (new EmailInUseException());
		}

		// create new user in database
		String passwordHash = convertToMD5Hash(password);
		DBUser dbUser = new DBUser(userName, eMail, passwordHash);
		db.insert(dbUser);
		user.setDBUser(dbUser);
		Log.debugLog("User registed: " + userName);
		return "Registrierung erfolgreich";
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
		DBUser dbUser = getUserWithEmail(eMail);
		
		if ( dbUser != null ) {
			// test the password
			final String passwordHash = convertToMD5Hash(password);
			if (dbUser.getPassword().equals(passwordHash)) {
				user.logIn(dbUser);
				Log.debugLog("Log in erfolgreich. User: " + user.toString());
				return ("Log in erfolgreich");
			}
		}

		Log.debugLog("Log in fehlgeschlagen. User: " + user.toString());
		throw new EmailNotFoundException();
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
	 * @throws EmailNotFoundException 
	 * @throws NoSuchAlgorithmException 
	 */
	public void removeUser ( String eMail, String password, User user ) throws NoSuchAlgorithmException, EmailNotFoundException {
		logIn(eMail, password, user);	// to be sure that he will be removed
		db.remove(user.getDBUser());
		user.logOut();
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

	public PersistenceFacade getPersistence() {
		return persistence;
	}
	
	private DBUser getUserWithEmail ( String eMail ) {
		List<DBUser> userList = db.readAll(DBUser.class, "eMail", eMail);
		if ( userList.size() == 0 ) {
			return null;
		}
		
		return userList.get(0);
	}
}
