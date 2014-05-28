package de.httpServer;

import java.sql.SQLNonTransientConnectionException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.data.DBUser;
import de.httpServer.EmailInUseException;
import de.httpServer.EmailNotFoundException;
import de.httpServer.User;
import de.persistence.CRUDIF;
import de.persistence.PersistenceException;
import de.persistence.PersistenceFacade;
import de.persistence.PersistenceFacadeIF;

public class UserTest {

    private CRUDIF db;
    private User user;
    private String eMail = "eMailAddress";
    private String password = "password";
    private String userName = "userName";
    private static PersistenceFacadeIF persistence;
    
    @BeforeClass
    public static void start(){
	persistence = new PersistenceFacade();
	persistence.startDBSystem();
	
    }
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
		db = persistence.getDBController();
		user = new User ( db );
    }
    
    @After
    public void tearDown() throws Exception {
		cleareDB();
    }
    
    @AfterClass
    public static void end(){
	try{
	    persistence.shutdownDBSystem();
	}
	catch(PersistenceException arg0){
	    if(arg0.getCause() instanceof SQLNonTransientConnectionException){
		/*
		 * DO NOTHING, this exception is thrown from derby during shutdown and means no error
		 * See http://db.apache.org/derby/papers/DerbyTut/embedded_intro.html
		 */
	    }
	    else
		throw arg0;
	}
    }
    
    /**
     * bevore registration, expected failed
     */
    @Test ( expected = EmailNotFoundException.class )
	public void logInTestBevore () throws Exception {
    	cleareDB();
    	
    	User user2 = new User ( db );
    	
		user2.logIn ( eMail, password );
    }
    
    @Test
	public void registerTest () throws Exception {
    	cleareDB();
    	
		user.register ( eMail, password, userName );
    }
    
    @Test( expected = EmailInUseException.class )
	public void registerTest2 () throws Exception {
    	registerTest();
    	
    	User user2 = new User ( db );
    	user2.register ( eMail, password, userName );
    }
    
    /**
     * after registration
     * @throws Exception 
     */
    @Test
	public void logInTestAfter () throws Exception {
    	registerTest();
    	
    	User user2 = new User ( db );
    	
		user2.logIn ( eMail, password );
    }
    

    public void cleareDB() throws Exception {
		final List<DBUser> temp = db.readAll(DBUser.class);
		
		for(DBUser u : temp){
			db.remove(u);
		}
    }
	
}
