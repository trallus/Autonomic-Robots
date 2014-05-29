package de.httpServer;

import static org.junit.Assert.fail;

import java.sql.SQLNonTransientConnectionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import de.httpServer.EmailNotFoundException;
import de.httpServer.User;
import de.persistence.PersistenceException;

/**
 * Test if the UserManager Works fine
 * 
 * @author ko
 */
public class UserManagerTest {

	private User user;
	private String eMail = "eMailAddress";
	private String password = "password";
	private String userName = "userName";
	private UserManager userManager;

	/**
	 * create a new UserManager to be tested
	 */
	@Before
	public void start() {
		userManager = new UserManager();
		user = userManager.getUser(null);
	}

	/**
	 * Shut down the UserManager
	 */
	@After
	public void end() {
		try {
			userManager.shutDown();
		} catch (PersistenceException arg0) {
			if (arg0.getCause() instanceof SQLNonTransientConnectionException) {
				/*
				 * DO NOTHING, this exception is thrown from derby during
				 * shutdown and means no error See
				 * http://db.apache.org/derby/papers
				 * /DerbyTut/embedded_intro.html
				 */
			} else
				throw arg0;
		}
	}

	/**
	 * try to login before registrated, should be failed
	 *
	 * @throws Exception
	 */
	@Test
	public void logInTestBevore() throws Exception {
		User user2 = userManager.getUser(null);

		try {
			userManager.logIn(eMail, password, user2);
		} catch (EmailNotFoundException e) {
			return;
		}
		fail("expected EmailNotFoundException");
	}

	/**
	 * Register, remove and register the User  again
	 * 
	 * @throws Exception
	 */
	@Test
	public void registerRemoveTest() throws Exception {
		userManager.register(userName, eMail, password, user);
		userManager.removeUser(user);
		userManager.register(userName, eMail, password, user);
		// just to cleare
		userManager.removeUser(user);
	}

	/**
	 * Try to register 2 Users with the same eMail
	 * This shoud be failed
	 * 
	 * @throws Exception
	 */
	@Test
	public void registerTest2() throws Exception {
		userManager.register(userName, eMail, password, user);

		User user2 = userManager.getUser(null);
		try {
			userManager.register(userName, eMail, password, user2);
		} catch (EmailInUseException e) {
			userManager.removeUser(user);	// just to cleare
			return;
		}
		userManager.removeUser(user);	// just to cleare
		fail("expected EmailInUseException");
	}

	/**
	 * Log in after registration
	 * 
	 * @throws Exception
	 */
	@Test
	public void logInTestAfter() throws Exception {
		userManager.register(userName, eMail, password, user);

		User user2 = userManager.getUser(null);

		userManager.logIn(eMail, password, user2);
		
		userManager.removeUser(user);
		
		if (! user2.isLogedIn()) {
			fail("Unable to log in with registrated eMail");
		}
	}

}