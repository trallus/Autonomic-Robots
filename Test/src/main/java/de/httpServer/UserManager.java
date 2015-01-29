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
	 * @param db instance of the database interface
	 */
	void clareDB (CRUDIF db);

	/**
	 * update a user in database
	 * 
	 * @param user the user that will be updated
	 * @param db instance of the databes interface
	 */
	void upDate(CRUDIF db, User user);

	/**
	 * get the user with this SID or create a new one
	 * 
	 * @param db instance of the databes interface
	 * @param sessionID the SID for which the user is returned
	 * @return the User of the SID or a new one if no found
	 */
	User getUser(CRUDIF db, String sessionID);

	/**
	 * registrate e new user in database
	 * 
	 * @param db instance of the databes interface
	 * @param userName the nickname of the user
	 * @param eMail the email of the user
	 * @param password the password of the user
	 * @param user instance of the user
	 * @return a String with the Message for the User
	 * @throws NoSuchAlgorithmException thrown when the requested hash algorithm is not available
	 * @throws EmailInUseException when the email is already registered
	 * @throws NameInUseException  when the userName is already registered
	 */
	 String register(CRUDIF db, String userName, String eMail, String password, User user)
			throws NoSuchAlgorithmException, EmailInUseException, NameInUseException;

	/**
	 * log in a user
	 * 
	 * @param db instance of the databes interface
	 * @param eMail the email of the user
	 * @param password the password of the user
	 * @param user instance of the user
	 * @throws NoSuchAlgorithmException when the requested hash algorithm is not available
	 * @throws EmailNotFoundException when the email is already registered
	 */
	void logIn(CRUDIF db, String eMail, String password, User user)
			throws NoSuchAlgorithmException, EmailNotFoundException;

	/**
	 * log out a user
	 * 
	 * @param db instance of the databes interface
	 * @param user instance of the user
	 */
	void logOut(CRUDIF db, User user);
	
	/**
	 * delete a user from database
	 * 
	 * @param db instance of the databes interface
	 * @param eMail the email of the user
	 * @param password the password of the user
	 * @param user instance of the user
	 * @throws NoSuchAlgorithmException when the requested hash algorithm is not available
	 * @throws EmailNotFoundException when the email is already registered
	 */
	void removeUser (CRUDIF db, String eMail, String password, User user )
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
	 * @param db instance of the databes interface
	 * @param userName the nickname of the user
	 * @return list of usernames: String []
	 */
	String[] searchUser (CRUDIF db, String userName ); 
	
	/**
	 * Change atributes of a User
	 * 
	 * @param user instance of the user
	 * @param db instance of the databes interface
	 * @param newName the new Name of the user
	 * @param newEMail the new Email of the user
	 * @param newPassword the new password of the user
	 * @throws NoSuchAlgorithmException thrown when the requested hash algorithm is not available
	 * @throws EmailInUseException when the email is already registered
	 * @throws NameInUseException  when the userName is already registered
	 */
	void changeUser(CRUDIF db, User user, String newName, String newEMail, String newPassword) throws EmailInUseException, NameInUseException, NoSuchAlgorithmException; 
}
