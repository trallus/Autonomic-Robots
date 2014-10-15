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
	    return new CRUDWorker(em);
	}
	catch (Exception arg0) {
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
	    Log.info("Database started");
	}
	catch (Exception arg0) {
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
	    throw new PersistenceException(arg0);
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
	    throw new PersistenceException(new IllegalStateException(
		    "DB System not started"));
	try {
	    em.getTransaction().begin();
	}
	catch (Exception arg0) {
	    throw new PersistenceException(arg0);
	}
    }

    /**
     * @see de.persistence.PersistenceFacadeIF#endTransaction(boolean)
     */
    @Override
    public void endTransaction(boolean state) {
	if (!dbStarted)
	    throw new PersistenceException(new IllegalStateException(
		    "DB System not started"));
	try {
	    if (state) {
		em.getTransaction().commit();
	    }
	    else {
		em.getTransaction().rollback();
	    }
	}
	catch (Exception arg0) {
	    Log.errorLog(arg0.getLocalizedMessage());
	}
    }
}
