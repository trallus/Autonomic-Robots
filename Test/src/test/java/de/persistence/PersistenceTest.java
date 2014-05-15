package de.persistence;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Test if the CrudWorker is working correctly
 * 
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 15.05.2014
 */
public class PersistenceTest {

    private CRUDIF crud;


    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	crud = new CRUDWorker();
    }

    @Test
    public void dbTest() {
	// Null as parameter tests
	
	// Insert 2 times the same object

	// ReadAll after p1 insertion

	// ReadID after p1 insertion

	// Update

	// Remove

    }

}
