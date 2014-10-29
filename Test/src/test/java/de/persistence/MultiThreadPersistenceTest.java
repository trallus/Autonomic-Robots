package de.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.data.DBUser;

/**
 * @author mike
 *
 */
public class MultiThreadPersistenceTest {

    private PersistenceFacadeIF psf;

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
	psf.shutdownDBSystem();
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	psf = new PersistenceFacade();
	psf.startDBSystem();
    }

    @Test
    public void test() {
	for(int i=0;i<1000;i++){
	    final Intern intern = new Intern(psf);
	    intern.start();
	}
    }
    
    private class Intern extends Thread{
	
	private final CRUDIF crud;
	private final PersistenceFacadeIF psf;
	
	Intern(final PersistenceFacadeIF psf){
	    this.psf = psf;
	    crud = psf.getDBController();
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run(){
	    for(int i=0;i<1000;i++){
		psf.beginTransaction(crud);
		try {
		    Thread.currentThread().sleep(1000);
		}
		catch (InterruptedException e) {
		    e.printStackTrace();
		}
		crud.insert(new DBUser());
		psf.endTransaction(true, crud);
	    }
	}
    }

}
