package de.persistence;

import java.sql.DriverManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * The Facade for the Persistence system
 * 
 * @author Mike Kiekebusch
 * @version 0.2
 * @since 15.05.2014
 */
public class PersistenceFacade implements PersistenceFacadeIF {

    private boolean dbStarted;
    private EntityManagerFactory emf;
    private EntityManager em;

    public PersistenceFacade() {
	dbStarted = false;
    }

    @Override
    public final CRUDIF getDBController() {
	if (!dbStarted)
	    throw new PersistenceException(new IllegalStateException(
		    "DB System not started"));
	try {
	    return new CRUDWorker(em,
		    emf.getPersistenceUnitUtil());
	}
	catch (Throwable arg0) {
	    throw new PersistenceException(arg0);
	}
    }

    @Override
    public final void startDBSystem() {
	if (dbStarted)
	    throw new PersistenceException(new IllegalStateException(
		    "DB System already started"));
	try {
	    emf = Persistence.createEntityManagerFactory("monster");
	    em = emf.createEntityManager();
	    dbStarted = true;
	}
	catch (Throwable arg0) {
	    throw new PersistenceException(arg0);
	}
    }

    @Override
    public final void shutdownDBSystem() {
	if (!dbStarted)
	    throw new PersistenceException(new IllegalStateException(
		    "DB System not started"));
	try {
	    DriverManager.getConnection("jdbc:derby:DB;shutdown=true");
	    emf.close();
	    dbStarted = false;
	}
	catch (Throwable arg0) {
	    throw new PersistenceException(arg0);
	}
    }

    /**
     * @see de.persistence.PersistenceFacadeIF#beginTransaction()
     */
    @Override
    public void beginTransaction() {
	if (!dbStarted)
	    throw new PersistenceException(new IllegalStateException(
		    "DB System not started"));
	try{
	    em.getTransaction().begin();
	}
	catch(Throwable arg0){
	    throw new PersistenceException(arg0);
	}
    }

    /**
     * @see de.persistence.PersistenceFacadeIF#commitTransaction()
     */
    @Override
    public void commitTransaction() {
	if (!dbStarted)
	    throw new PersistenceException(new IllegalStateException(
		    "DB System not started"));
	try{
	    em.getTransaction().commit();
	}
	catch(Throwable arg0){
	    throw new PersistenceException(arg0);
	}
    }

    /**
     * @see de.persistence.PersistenceFacadeIF#rollbackTransaction()
     */
    @Override
    public void rollbackTransaction() {
	if (!dbStarted)
	    throw new PersistenceException(new IllegalStateException(
		    "DB System not started"));
	try{
	    em.getTransaction().rollback();
	}
	catch(Throwable arg0){
	    throw new PersistenceException(arg0);
	}
    }
}
