package de.httpServer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import de.data.DBUser;
import de.logger.ExceptionHandlerFacade;
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
	public void clareDB (final CRUDIF db) {
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
	public void upDate(final CRUDIF db, User user) {
		db.update(user.getDBUser());
	}

	/**
	 * get the user with this SID or create a new one
	 * 
	 * @param sessionID
	 * @return
	 */
	public User getUser(final CRUDIF db, String sessionID) {

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
	public String register(final CRUDIF db, String userName, String eMail, String password, User user)
			throws NoSuchAlgorithmException, EmailInUseException, NameInUseException {
		checkInUse(db, eMail, userName);
		// create new user in database
		String passwordHash = convertToMD5Hash(password);
		DBUser dbUser = new DBUser(userName, eMail, passwordHash);
		db.insert(dbUser);
		user.setDBUser(dbUser);
		ExceptionHandlerFacade.getExceptionHandler().log("User registed, userName: " + userName, this.getClass().toString()+".register");
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
	public void logIn(final CRUDIF db, String eMail, String password, User user)
			throws NoSuchAlgorithmException, EmailNotFoundException {
		DBUser dbUser = getUserWithEmail(db, eMail);
		
		if ( dbUser != null ) {
			// test the password
			final String passwordHash = convertToMD5Hash(password);
			if (dbUser.getPassword().equals(passwordHash)) {
				user.logIn(dbUser);
				final String caller = this.getClass().toString()+".logIn";
				ExceptionHandlerFacade.getExceptionHandler().log("Log in erfolgreich.", caller);
				return;
			}
		}
		EmailNotFoundException e = new EmailNotFoundException("Wrong e-mail or password", true);
		e.putParameter("eMail", eMail);
		e.putParameter("password", password);
		e.putParameter("user", user);
		throw e;
	}

	/**
	 * log out a user
	 * 
	 * @param user
	 */
	public void logOut(final CRUDIF db, User user) {
		userList.remove(user);
		user.logOut();
		ExceptionHandlerFacade.getExceptionHandler().log("User loged out, user name: " + user.getDBUser().getName(), this.getClass().toString()+".logOut");
	}
	
	/**
	 * delete a user from database
	 * 
	 * @param user
	 * @throws EmailNotFoundException 
	 * @throws NoSuchAlgorithmException 
	 */
	public void removeUser (final CRUDIF db, String eMail, String password, User user ) throws NoSuchAlgorithmException, EmailNotFoundException {
		logIn(db, eMail, password, user);	// to be sure that he will be removed
		db.remove(user.getDBUser());
		user.logOut();
		ExceptionHandlerFacade.getExceptionHandler().log("User removed: eMail: " + user.getDBUser().getEMail(), this.getClass().toString()+".register");
	}

	@Override
	public String[] searchUser(final CRUDIF db, String prefix) {
		final List<DBUser> allUsers = db.readAll(DBUser.class);
		final ArrayList<String> nameList = new ArrayList<String>();
		for (final DBUser u : allUsers) {
			final String dbName = u.getName();
			if (dbName.startsWith(prefix)) {
				nameList.add(dbName);
			}
		}
		
		return nameList.toArray(new String[nameList.size()]);
	}

	@Override
	public void changeUser(final CRUDIF db, User user, String newName, String newEMail, String newPassword) throws EmailInUseException, NameInUseException, NoSuchAlgorithmException {
		checkInUse(db, newEMail, newName);
		
		final DBUser dbu = user.getDBUser();
		
		// dont change to emty strings
		if (newName.length() > 0) {
			dbu.setName(newName);
		}
		if (newEMail.length() > 0) {
			dbu.setEMail(newEMail);
		}
		if (newPassword.length() > 0) {
			dbu.setPassword(convertToMD5Hash(newPassword));
		}
		
		db.update(dbu);

		ExceptionHandlerFacade.getExceptionHandler().log("Chagne User: newName " + newName, this.getClass().toString()+".changeUser");
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

		ExceptionHandlerFacade.getExceptionHandler().log("Chagne User: user " + u.toString(), this.getClass().toString()+".createUser");

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
	
	private DBUser getUserWithEmail (final CRUDIF db, String eMail ) {
		List<DBUser> userList = db.readAll(DBUser.class, "eMail", eMail);
		if ( userList.size() == 0 ) {
			return null;
		}
		
		return userList.get(0);
	}
	
	private DBUser getUserWithName (final CRUDIF db, String name ) {
		List<DBUser> userList = db.readAll(DBUser.class, "name", name);
		if ( userList.size() == 0 ) {
			return null;
		}
		
		return userList.get(0);
	}
	
	private void checkInUse (final CRUDIF db, String eMail, String name) throws EmailInUseException, NameInUseException {
		// check if email already registered
		if ( getUserWithEmail (db, eMail) != null ) {
			final EmailInUseException e = new EmailInUseException("Registration failes. Email already registered.", true);
			e.putParameter("name", name);
			e.putParameter("eMail", eMail);
			throw (e);
		}
		// check if name already registered
		if ( getUserWithName (db, name) != null ) {
			final NameInUseException e = new NameInUseException("Registration failes. Name already registered.", true);
			e.putParameter("name", name);
			e.putParameter("eMail", eMail);
			throw (e);
		}
	}
}
