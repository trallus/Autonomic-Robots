package de.data;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.persistence.CRUDIF;
import de.persistence.PersistenceFacade;

public class UserTest {

    private CRUDIF db;
    private User user;
    private String eMail = "eMailAddress";
    private String password = "password";
    private String userName = "userName";
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
		db = PersistenceFacade.getDBController();
		user = new User ( db );
    }
    
    @After
    public void end() throws Exception {
		cleareDB();
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
