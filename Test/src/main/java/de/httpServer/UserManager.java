package de.httpServer;

import java.security.NoSuchAlgorithmException;

import de.persistence.PersistenceFacade;

public interface UserManager {
	/**
	 *  connection to database
	 */
	
	public void shutDown ();
	
	/**
	 * remove all Users from Database
	 */
	public void clareDB ();

	/**
	 * update a user in database
	 * 
	 * @param user
	 */
	public void upDate(User user);

	/**
	 * get the user with this SID or create a new one
	 * 
	 * @param sessionID
	 * @return
	 */
	public User getUser(String sessionID);

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
			throws NoSuchAlgorithmException, EmailInUseException;

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
			throws NoSuchAlgorithmException, EmailNotFoundException;

	/**
	 * log out a user
	 * 
	 * @param user
	 */
	public void logOut(User user);
	
	/**
	 * delete a user from database
	 * 
	 * @param user
	 * @throws EmailNotFoundException 
	 * @throws NoSuchAlgorithmException 
	 */
	public void removeUser ( String eMail, String password, User user )
			throws NoSuchAlgorithmException, EmailNotFoundException;

	/**
	 * get the PersistenceFacade
	 * 
	 * @return PersistenceFacade
	 */
	public PersistenceFacade getPersistence ();
}
