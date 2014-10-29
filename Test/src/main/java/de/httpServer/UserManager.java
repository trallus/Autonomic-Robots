package de.httpServer;

import java.security.NoSuchAlgorithmException;

import de.persistence.CRUDIF;
import de.persistence.PersistenceFacade;

public interface UserManager {
	/**
	 *  connection to database
	 */
	
	void shutDown ();
	
	/**
	 * remove all Users from Database
	 */
	void clareDB ();

	/**
	 * update a user in database
	 * 
	 * @param user
	 */
	void upDate(User user);

	/**
	 * get the user with this SID or create a new one
	 * 
	 * @param sessionID
	 * @return
	 */
	User getUser(String sessionID);

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
	 * @throws NameInUseException 
	 */
	 String register(String userName, String eMail, String password, User user)
			throws NoSuchAlgorithmException, EmailInUseException, NameInUseException;

	/**
	 * log in a user
	 * 
	 * @param eMail
	 * @param password
	 * @param user
	 * @throws NoSuchAlgorithmException
	 * @throws EmailNotFoundException
	 */
	void logIn(String eMail, String password, User user)
			throws NoSuchAlgorithmException, EmailNotFoundException;

	/**
	 * log out a user
	 * 
	 * @param user
	 */
	void logOut(User user);
	
	/**
	 * delete a user from database
	 * 
	 * @param user
	 * @throws EmailNotFoundException 
	 * @throws NoSuchAlgorithmException 
	 */
	void removeUser ( String eMail, String password, User user )
			throws NoSuchAlgorithmException, EmailNotFoundException;

	/**
	 * get the PersistenceFacade
	 * 
	 * @return PersistenceFacade
	 */
	PersistenceFacade getPersistence ();
	
	/**
	 * Search user/s by name and return a list of usernames
	 * 
	 * @param userName
	 * @return list of usernames: String []
	 */
	String[] searchUser ( String userName ); 
	
	/**
	 * Change atributes of a User
	 * 
	 * @param user
	 * @throws NameInUseException 
	 * @throws EmailInUseException 
	 * @throws NoSuchAlgorithmException 
	 */
	void changeUser(User user, String newName, String newEMail, String newPassword) throws EmailInUseException, NameInUseException, NoSuchAlgorithmException; 
}
