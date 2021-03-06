package de.persistence;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.data.DBUser;
import de.logger.LogExceptionFacade;
import de.logger.LogLevel;
import de.logger.LoggerAndExceptionHandlerFacadeIF;

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
    public static void startDB() {
	final LoggerAndExceptionHandlerFacadeIF laefif = new LogExceptionFacade(null, null, LogLevel.OFF);
	persistence = new PersistenceFacade(laefif);
	persistence.startDBSystem();
    }

    @AfterClass
    public static void stopDB() {
	persistence.shutdownDBSystem();
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
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
	final List<DBUser> temp = crud.readAll(DBUser.class);
	persistence.beginTransaction(crud);
	for (DBUser u : temp) {
	    crud.remove(u);
	}
	persistence.endTransaction(true,crud);
    }

    @Test
    public void nullTest() {
	persistence.beginTransaction(crud);
	try {
	    crud.insert(null);
	    fail("No exception for insert(null)");
	}
	catch (PersistenceException expected) {
	    // Must throw this exception
	}
	finally {
	    persistence.endTransaction(false,crud);
	}
    }
    
    @Test
    public void stopAndRestartDBTest(){
	persistence.shutdownDBSystem();
	try{
	    persistence.beginTransaction(crud);
	    fail("Expected IllegalStateException was not thrown");
	}
	catch(PersistenceException expected){
	    if(!(expected.getCause() instanceof IllegalStateException)){
		fail("Expected IllegalStateException was not thrown");
	    }
	}
	persistence.startDBSystem();
	crud = persistence.getDBController();
    }

    @Test
    public void insertReadTest() {
	persistence.beginTransaction(crud);
	crud.insert(user);
	persistence.endTransaction(true,crud);
	assertEquals(user, crud.readID(user.getClass(), user.getId()));
    }

    @Test
    public void updateTest() {
	persistence.beginTransaction(crud);
	crud.insert(user);
	persistence.endTransaction(true,crud);
	user.setName("Updated");
	persistence.beginTransaction(crud);
	crud.update(user);
	persistence.endTransaction(true,crud);
	assertEquals(user, crud.readID(user.getClass(), user.getId()));
    }

    @Test
    public void removeTest() {
	persistence.beginTransaction(crud);
	crud.insert(user);
	persistence.endTransaction(true,crud);
	assertEquals(1, crud.readAll(user.getClass()).size());
	persistence.beginTransaction(crud);
	crud.remove(user);
	persistence.endTransaction(true,crud);
	assertEquals(0, crud.readAll(user.getClass()).size());
    }

    @Test
    public void readWhereTest() {
	persistence.beginTransaction(crud);
	crud.insert(user);
	persistence.endTransaction(true,crud);
	assertEquals(1, crud.readAll(user.getClass(), "pwHash", "1245").size());
    }

    @Test
    public void transactionRolledBackTest() throws Exception {
	assertEquals(0, crud.readAll(user.getClass()).size());
	persistence.beginTransaction(crud);
	crud.insert(user);
	persistence.endTransaction(false,crud);
	assertEquals(0, crud.readAll(user.getClass()).size());
    }
}
