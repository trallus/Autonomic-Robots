package de.httpServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Proxy;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.data.DBUser;
import de.httpServer.EmailNotFoundException;
import de.httpServer.User;

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
    private static UserManager plainUserManager;

    /**
     * create a new UserManager to be tested
     */
    @Before
    public void setUp() {
	UserManager proxydUserManager = (UserManager) Proxy.newProxyInstance(
		plainUserManager.getClass().getClassLoader(), plainUserManager
			.getClass().getInterfaces(), new TranactionHandler(
			plainUserManager));
	userManager = proxydUserManager;
	user = userManager.getUser(null);
	userManager.clareDB();
    }

    /**
     * Star the DATABASE before all test
     */
    @BeforeClass
    public static void start(){
	plainUserManager = new UserManagerImpl();
    }
    
    /**
     * Shut down the DATABASE after all tests
     */
    @AfterClass
    public static void end() {
	plainUserManager.shutDown();
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
	}
	catch (EmailNotFoundException expected) {
	}
    }

    /**
     * Register, remove and register the User again
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
     * Try to register 2 Users with the same eMail This shoud be failed
     * 
     * @throws Exception
     */
    @Test
    public void registerTest2() throws Exception {
	userManager.register(userName, eMail, password, user);

	User user2 = userManager.getUser(null);
	try {
	    userManager.register("anotherName", eMail, password, user2);
	    fail("expected EmailInUseException");
	}
	catch (EmailInUseException e) {
	}
    }

    /**
     * Try to register 2 Users with the same name This shoud be failed
     * 
     * @throws Exception
     */
    @Test
    public void registerTest3() throws Exception {
	userManager.register(userName, eMail, password, user);

	User user2 = userManager.getUser(null);
	try {
	    userManager.register(userName, "amotherEmail", password, user2);
	    fail("expected NameInUseException");
	}
	catch (NameInUseException e) {
	}
    }

    /**
     * Register many users and Search all how started with man
     * 
     * @throws Exception
     */
    @Test
    public void searchUserTest() throws Exception {
	// define expectet result
	final String searchPrefix = "man";
	final String[] newUserNames = { "man", "mantest2", "3mantest",
		"ma4ntest", "mantest5", "mantest6" };
	final ArrayList<String> expectedUserNames = new ArrayList<String>();
	expectedUserNames.add("man");
	expectedUserNames.add("mantest2");
	expectedUserNames.add("mantest5");
	expectedUserNames.add("mantest6");
	// registrate the users
	for (int i = 0; i < newUserNames.length; i++) {
	    userManager.register(newUserNames[i], eMail + i, password, user);
	}

	final String[] resultUserNames = userManager.searchUser(searchPrefix);
	for (final String resultName : resultUserNames) {
	    final int i = expectedUserNames.indexOf(resultName);
	    if (i == -1) {
		fail("Found unexpected user name: \"" + resultName
			+ "\" in the search result");
	    }
	    expectedUserNames.remove(i); // remove the found name
	}

	if (expectedUserNames.size() > 0) {
	    fail("Found not all expected names in the search result");
	}
    }

    /**
     * Be sure that a User can change his values
     * 
     * @throws Exception
     */
    @Test
    public void changeUserTest() throws Exception {
	final String newName = "khcnifhxf";
	final String newEMail = "alskjfdoiadhnfak";
	final String newPassword = "sadfkpadskfsadöfl";
	final String newPasswordHash = "-25139618-11143-41105-7963-457511621-27-26";

	userManager.register(userName, eMail, password, user);
	userManager.logIn(eMail, password, user);
	userManager.changeUser(user, newName, newEMail, newPassword);

	userManager.logOut(user);

	final User testUser = userManager.getUser(null);
	userManager.logIn(newEMail, newPassword, testUser);
	final DBUser testBDUser = testUser.getDBUser();

	assertEquals(testBDUser.getName(), newName);
	assertEquals(testBDUser.getEMail(), newEMail);
	assertEquals(testBDUser.getPassword(), newPasswordHash);
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

	if (!user2.isLogedIn()) {
	    fail("Unable to log in with registrated eMail");
	}
    }

}
