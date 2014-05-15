package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.CRUDIF;
import de.CRUDWorker;

/**
 * Test if the CrudWorker is working correctly
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 15.05.2014
 */
public class CRUDWorkerTest {
    
    private CRUDIF crud;
    private Address a1;
    private TelephoneNumber t1;
    private TelephoneNumber t2;
    private ArrayList<TelephoneNumber>numbers;
    private Person p1;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	crud = CRUDWorker.getInstance();
	a1 = new Address("BlubStraße", 12345);
	t1 = new TelephoneNumber(123, 456789);
	t2 = new TelephoneNumber(98, 7654321);
	numbers = new ArrayList<>();
	numbers.add(t1);
	numbers.add(t2);
	p1 = new Person("Testie", "von Test", a1, numbers, "Hopefully i will be persistend");
    }
    
    /**
     * Tests the Singelton Pattern for the CRUDWorker
     */
    @Test
    public void getInstanceTest(){
	assertEquals("CRUDWorker singelton pattern implementation", crud, CRUDWorker.getInstance());
    }
    
    @Test
    public void dbTest(){
	//Null as parameter tests
	assertNull("readAll(null)==null", crud.readAll(null));
	assertNull("readAll(null,x,y)==null", crud.readAll(null, "Name", ""));
	assertNull("readAll(x,null,y)==null", crud.readAll(Person.class, null, ""));
	assertNull("readAll(x,y,null)==null", crud.readAll(Person.class, "Name", null));
	assertFalse("insert(null)==false",crud.insert(null));
	assertFalse("update(null)==false",crud.update(null));
	assertFalse("remove(null)==false",crud.remove(null));
	assertNull("readID(null,x)==null",crud.readID(null, 5));
	assertNull("readID(...) without previous insert",crud.readID(Person.class, 1));
	
	//Insert 2 times the same object
	assertTrue("insert(p1)==true",crud.insert(p1));
	assertFalse("insert(p1)==false",crud.insert(p1));
	assertTrue("After two inserts the objct is only one time in DB",crud.readAll(p1.getClass()).size()==1);
	
	//ReadAll after p1 insertion
	assertEquals("after insert of p1 readall(p1.getClass) returns list with p1 as first entry",p1, crud.readAll(p1.getClass()).get(0));
	assertEquals("get t1 for readAll(t1.getClass(),'areaCode',123) as first entry in the list",t1, crud.readAll(t1.getClass(), "areaCode", 123).get(0));
	assertTrue("readAll(t1.getClass(),'areaCode',123) returns a list with a size of 1",crud.readAll(t1.getClass(), "areaCode", 123).size()==1);
	
	//ReadID after p1 insertion
	assertEquals("readID(p1.class,p1.ID)==p1",p1, crud.readID(p1.getClass(), p1.getId()));
	
	//Update
	p1.getNumbers().clear(); //clears the telephone list of p1
	assertTrue("After clear of telephonelist from p1, p1 is updated",crud.update(p1));
	assertTrue("List of telephonenumbers in DB is zero after p1 update",crud.readAll(t1.getClass()).size()==0);
	
	//Remove
	assertTrue("Remove of p1 from database",crud.remove(p1));
	assertTrue("Returned list for readAll(p1) has size zero",crud.readAll(p1.getClass()).size()==0);
	assertFalse("Try to remove p1 a second time doesn't work",crud.remove(p1));
    }

}
