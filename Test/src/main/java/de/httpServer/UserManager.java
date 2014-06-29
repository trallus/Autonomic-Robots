package de.httpServer;

import java.security.NoSuchAlgorithmException;

import de.persistence.PersistenceFacade;

public interface UserManager {
	/**
	 *  connection to database
	 */
	
	public void shutDown ();
	public void clareDB ();
	public void upDate(User user);
	public User getUser(String sessionID);
	public String register(String userName, String eMail, String password, User user)
			throws NoSuchAlgorithmException, EmailInUseException;
	public String logIn(String eMail, String password, User user)
			throws NoSuchAlgorithmException, EmailNotFoundException;
	public void logOut(User user);
	public void removeUser ( String eMail, String password, User user )
			throws NoSuchAlgorithmException, EmailNotFoundException;
	public PersistenceFacade getPersistence ();
}
