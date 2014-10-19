package de.persistence;

import java.sql.DriverManager;
import java.sql.SQLNonTransientConnectionException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.logger.ExceptionHandlerFacade;
import de.logger.ExceptionHandlerIF;
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
    private ExceptionHandlerIF handler;

    public PersistenceFacade() {
	dbStarted = false;
	handler = ExceptionHandlerFacade.getExceptionHandler();
    }

    @Override
    public final CRUDIF getDBController() {
	if (!dbStarted)
	    throw new PersistenceException("DB System not started",
		    new IllegalStateException(), false);
	try {
	    return new CRUDWorker(em);
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException(
		    "Could not create instance of CRUDWorker", arg0, false);
	    throw pex;
	}
    }

    @Override
    public final void startDBSystem() {
	if (dbStarted)
	    throw new PersistenceException("DB System already started",
		    new IllegalStateException(), dbStarted);
	try {
	    emf = Persistence.createEntityManagerFactory("monster");
	    em = emf.createEntityManager();
	    dbStarted = true;
	    handler.handle("DB system started", "PersistenceFacade");
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not start DB system", arg0, false);
	    throw pex;
	}
    }

    @Override
    public final void shutdownDBSystem() {
	if (!dbStarted)
	    throw new PersistenceException("DB System not started", new IllegalStateException(), dbStarted);
	try {
	    DriverManager.getConnection("jdbc:derby:DB;shutdown=true");
	}
	catch (SQLNonTransientConnectionException expected) {
	    handler.handle("DB system shutdown", "PersistenceFacade");
	    /*
	     * DO NOTHING, this exception is thrown from derby during shutdown
	     * and means no error See http://db.apache.org/derby/papers
	     * /DerbyTut/embedded_intro.html
	     */
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not shutdown DB system", arg0, false);
	    throw pex;
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
	    throw new PersistenceException("DB System not started", new IllegalStateException(), dbStarted);
	try {
	    em.getTransaction().begin();
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not beginn database transaction", arg0, false);
	    throw pex;
	}
    }

    /**
     * @see de.persistence.PersistenceFacadeIF#endTransaction(boolean)
     */
    @Override
    public void endTransaction(boolean state) {
	if (!dbStarted)
	    throw new PersistenceException("DB System not started", new IllegalStateException(), state);
	try {
	    if (state) {
		em.getTransaction().commit();
	    }
	    else {
		em.getTransaction().rollback();
	    }
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not end database transaction", arg0, false);
	    pex.putParameter("State", state);
	    throw pex;
	}
    }
}
