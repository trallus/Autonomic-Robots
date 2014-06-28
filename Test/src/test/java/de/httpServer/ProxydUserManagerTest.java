package de.httpServer;

import static org.junit.Assert.fail;

import java.lang.reflect.Proxy;
import java.sql.SQLNonTransientConnectionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.httpServer.EmailNotFoundException;
import de.httpServer.User;
import de.logger.Log;
import de.persistence.PersistenceException;

/**
 * Test if the UserManager Works fine
 * 
 * @author ko
 */
public class ProxydUserManagerTest {

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
		UserManager plainUserManager = new UserManagerImpl();
		UserManager proxydUserManager = ( UserManager )
				Proxy.newProxyInstance(
						plainUserManager.getClass().getClassLoader(), 
						plainUserManager.getClass().getInterfaces(),
						new TranactionHandler ( plainUserManager ));
		userManager = proxydUserManager;
		user = userManager.getUser(null);
		userManager.clareDB();
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
		final User user2 = userManager.getUser(null);

		try {
			userManager.logIn(eMail, password, user2);
			fail("EmailNotFoundException expected");
		} catch (EmailNotFoundException expected) {
		}
	}

	/**
	 * Register, remove and register the User  again
	 * 
	 * @throws Exception
	 */
	@Test
	public void registerRemoveTest() throws Exception {
		userManager.register(userName, eMail, password, user);
		userManager.removeUser(eMail, password, user);
		userManager.register(userName, eMail, password, user);
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
			fail("expected EmailInUseException");
		} catch (EmailInUseException e) {
		}
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
		
		if (! user2.isLogedIn()) {
			fail("Unable to log in with registrated eMail");
		}
	}

}
