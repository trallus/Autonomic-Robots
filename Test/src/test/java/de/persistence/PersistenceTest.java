package de.persistence;

import static org.junit.Assert.*;

import java.sql.SQLNonTransientConnectionException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.data.DBUser;

/**
 * Test if the CrudWorker is working correctly
 * 
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 15.05.2014
 */
public class PersistenceTest {

    private CRUDIF crud;
    private DBUser user;
    private static PersistenceFacadeIF persistence;

    @BeforeClass
    public static void startDB(){
	persistence = new PersistenceFacade();
	persistence.startDBSystem();
    }
    @AfterClass
    public static void stopDB(){
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
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	crud = persistence.getDBController();
	user = new DBUser("Tester", "Tester@test.de", "1245");
    }
    
    /**
     * Cleares the database of all Users entries if any
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
	final List<DBUser> temp = crud.readAll(DBUser.class);
	for(DBUser u : temp){
	    crud.remove(u);
	}
    }

    @Test(expected = PersistenceException.class)
    public void nullTest(){
	crud.insert(null);
    }
    
    @Test
    public void twoInsertsOfSameEntityTest(){
	/*
	 * No longer throws an exception because the cache registers that the entity is already inserted
	 */
	crud.insert(user);
	crud.insert(user);
    }
    
    @Test(expected = PersistenceException.class)
    public void removeNotPersistedEntityTest(){
	crud.remove(user);
    }
    
    @Test
    public void insertReadTest(){
	crud.insert(user);
	assertEquals(user, crud.readID(user.getClass(), user.getId()));
    }
    @Test
    public void updateTest(){
	crud.insert(user);
	user.setName("Updated");
	crud.update(user);
	assertEquals(new DBUser("Updated", "Tester@test.de", "1245"), crud.readID(user.getClass(), user.getId()));
    }
    @Test
    public void removeTest(){
	crud.insert(user);
	assertEquals(1,crud.readAll(user.getClass()).size());
	crud.remove(user);
	assertEquals(0,crud.readAll(user.getClass()).size());
    }
    
    @Test
    public void readWhereTest() {
	crud.insert(user);
	assertTrue(crud.readAll(user.getClass(),"pwHash","1245").size()==1);
    }

}
