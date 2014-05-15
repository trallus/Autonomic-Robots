package de;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.CRUDIF;
import de.CRUDWorker;

/**
 * Test if the CrudWorker is working correctly
 * 
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 15.05.2014
 */
public class CRUDWorkerTest {

    private CRUDIF crud;
    private Address a1;
    private TelephoneNumber t1;
    private TelephoneNumber t2;
    private ArrayList<TelephoneNumber> numbers;
    private Person p1;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	crud = new CRUDWorker();
	a1 = new Address("BlubStraße", 12345);
	t1 = new TelephoneNumber(123, 456789);
	t2 = new TelephoneNumber(98, 7654321);
	numbers = new ArrayList<>();
	numbers.add(t1);
	numbers.add(t2);
	p1 = new Person("Testie", "von Test", a1, numbers,
		"Hopefully i will be persistend");
    }

    @Test
    public void dbTest() {
	// Null as parameter tests
	assertTrue("Christians Nachricht",false);
	// Insert 2 times the same object

	// ReadAll after p1 insertion

	// ReadID after p1 insertion

	// Update

	// Remove

    }

}
