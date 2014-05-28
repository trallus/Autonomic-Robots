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
	persistence.beginTransaction();
	for(DBUser u : temp){
	    crud.remove(u);
	}
	persistence.commitTransaction();
    }

    @Test
    public void nullTest(){
	persistence.beginTransaction();
	try{
	    crud.insert(null);
	    fail("No exception for insert(null)");
	}
	catch(PersistenceException arg0){
	    //Must throw this exception
	}
	finally{
	    persistence.rollbackTransaction();
	}
    }
    
    @Test
    public void twoInsertsOfSameEntityTest(){
	/*
	 * No longer throws an exception because the cache registers that the entity is already inserted
	 */
	persistence.beginTransaction();
	crud.insert(user);
	crud.insert(user);
	persistence.commitTransaction();
    }
    
    @Test
    public void removeNotPersistedEntityTest(){
	persistence.beginTransaction();
	try{
	    crud.remove(user);
	    fail("No exception for insert(null)");
	}
	catch(PersistenceException arg0){
	    //Must throw this exception
	}
	finally{
	    persistence.rollbackTransaction();
	}
    }
    
    @Test
    public void insertReadTest(){
	persistence.beginTransaction();
	crud.insert(user);
	persistence.commitTransaction();
	assertEquals(user, crud.readID(user.getClass(), user.getId()));
    }
    @Test
    public void updateTest(){
	persistence.beginTransaction();
	crud.insert(user);
	persistence.commitTransaction();
	user.setName("Updated");
	persistence.beginTransaction();
	crud.update(user);
	persistence.commitTransaction();
	assertEquals(new DBUser("Updated", "Tester@test.de", "1245"), crud.readID(user.getClass(), user.getId()));
    }
    @Test
    public void removeTest(){
	persistence.beginTransaction();
	crud.insert(user);
	persistence.commitTransaction();
	assertEquals(1,crud.readAll(user.getClass()).size());
	persistence.beginTransaction();
	crud.remove(user);
	persistence.commitTransaction();
	assertEquals(0,crud.readAll(user.getClass()).size());
    }
    
    @Test
    public void readWhereTest() {
	persistence.beginTransaction();
	crud.insert(user);
	persistence.commitTransaction();
	assertEquals(1,crud.readAll(user.getClass(),"pwHash","1245").size());
    }

    @Test
    public void transactionRolledBackTest() throws Exception{
	assertEquals(0,crud.readAll(user.getClass()).size());
	persistence.beginTransaction();
	crud.insert(user);
	persistence.rollbackTransaction();
	assertEquals(0,crud.readAll(user.getClass()).size());
    }
}
