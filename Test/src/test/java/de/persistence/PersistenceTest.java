package de.persistence;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
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

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	crud = PersistenceFacade.getDBController();
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
    
    @Test(expected = PersistenceException.class)
    public void twoInsertsOfSameEntityTest(){
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
	assertTrue(crud.readAll(user.getClass()).size()==1);
	crud.remove(user);
	assertTrue(crud.readAll(user.getClass()).size()==0);
    }
    
    @Test
    public void readWhereTest() {
	crud.insert(user);
	assertTrue(crud.readAll(user.getClass(),"pwHash","1245").size()==1);
    }

}
