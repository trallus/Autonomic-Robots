package de.persistence;

import java.sql.DriverManager;
import java.sql.SQLNonTransientConnectionException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.logger.Log;

/**
 * The Facade for the Persistence system
 * 
 * @author Mike Kiekebusch
 * @version 0.3
 * @since 19.10.2014
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
	    throw new PersistenceException(
		    "Could not create CRUDIF because the DB System is not started",
		    new IllegalStateException(), false);
	try {
	    return new CRUDWorker(em);
	}
	catch (Exception arg0) {
	    throw new PersistenceException("Could not create CRUDIF", arg0,
		    false);
	}
    }

    @Override
    public final void startDBSystem() {
	if (dbStarted)
	    throw new PersistenceException(
		    "Could not start the DB System because it is already started",
		    new IllegalStateException(), false);
	try {
	    emf = Persistence.createEntityManagerFactory("monster");
	    em = emf.createEntityManager();
	    dbStarted = true;
	}
	catch (Exception arg0) {
	    throw new PersistenceException("Could not start DB System", arg0,
		    false);
	}
    }

    @Override
    public final void shutdownDBSystem() {
	if (!dbStarted)
	    throw new PersistenceException(
		    "Could not shutdown the DB System because it is not started",
		    new IllegalStateException(), false);
	try {
	    DriverManager.getConnection("jdbc:derby:DB;shutdown=true");
	}
	catch (SQLNonTransientConnectionException expected) {
	    Log.info("Database shutdown");
	    /*
	     * DO NOTHING, this exception is thrown from derby during shutdown
	     * and means no error See http://db.apache.org/derby/papers
	     * /DerbyTut/embedded_intro.html
	     */
	}
	catch (Exception arg0) {
	    throw new PersistenceException("DB System could not be shutdown",
		    arg0, false);
	}
	finally {
	    emf.close();
	    dbStarted = false;
	}
    }

    /**
     * @see de.persistence.PersistenceFacadeIF#beginTransaction()
     */
    @Override
    public void beginTransaction() {
	if (!dbStarted)
	    throw new PersistenceException(
		    "Could not begin transaction because the DB System is not started",
		    new IllegalStateException(), false);
	try {
	    em.getTransaction().begin();
	}
	catch (Exception arg0) {
	    throw new PersistenceException(
		    "Could not begin database transaction", arg0, false);
	}
    }

    /**
     * @see de.persistence.PersistenceFacadeIF#endTransaction(boolean)
     */
    @Override
    public void endTransaction(boolean state) {
	if (!dbStarted)
	    throw new PersistenceException(
		    "Could not end transaction because the DB System is not started",
		    new IllegalStateException(), state);
	try {
	    if (state) {
		em.getTransaction().commit();
	    }
	    else {
		em.getTransaction().rollback();
	    }
	}
	catch (Exception arg0) {
	    // TODO
	}
    }
}
