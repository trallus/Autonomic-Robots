package de.persistence;

import java.sql.DriverManager;
import java.sql.SQLNonTransientConnectionException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.logger.LogLevel;
import de.logger.LoggerAndExceptionHandlerFacadeIF;
import de.logger.LoggerIF;

/**
 * The Facade for the Persistence system
 * 
 * @author Mike Kiekebusch
 * @version 0.3
 * @since 15.05.2014
 */
public class PersistenceFacade implements PersistenceFacadeIF {

    /**
     * Flag that indicates if the underlying DB was started
     */
    private boolean dbStarted;
    /**
     * Instance of the EntityManagerFactory used to create EntityManagers for the getDBController Methode
     */
    private EntityManagerFactory emf;
    /**
     * Instance of the LoggerIf that is used to handle the logging
     */
    private final LoggerIF logger;
    
    /**
     * Creats a Instance of the PersistenceFacade with the given Instance of the LoggerAndExceptionHandlerFacadeIF
     * @param logFacade the LoggerAndExceptionHandlerFacade that is used to get the Logger
     */
    public PersistenceFacade(final LoggerAndExceptionHandlerFacadeIF logFacade) {
	dbStarted = false;
	logger = logFacade.getLoggerInstance();
    }

    @Override
    public final CRUDIF getDBController() {
	if (!dbStarted)
	    throw new PersistenceException("DB System not started",
		    new IllegalStateException(), false);
	try {
	    return new CRUDWorker(emf.createEntityManager());
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
		    new IllegalStateException(), false);
	try {
	    emf = Persistence.createEntityManagerFactory("monster");
	    emf.createEntityManager(); //Check if DB really is started, will throw Exception otherwise
	    dbStarted = true;
	    logger.log("DB system started", "PersistenceFacade",LogLevel.DEBUG);
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not start DB system", arg0, false);
	    throw pex;
	}
    }

    @Override
    public final void shutdownDBSystem() {
	if (!dbStarted)
	    throw new PersistenceException("DB System not started", new IllegalStateException(), false);
	try {
	    DriverManager.getConnection("jdbc:derby:DB;shutdown=true");
	}
	catch (SQLNonTransientConnectionException expected) {
	    logger.log("DB system shutdown", "PersistenceFacade",LogLevel.DEBUG);
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

    @Override
    public void beginTransaction(final CRUDIF dbcontroller) {
	if (!dbStarted)
	    throw new PersistenceException("DB System not started", new IllegalStateException(), false);
	try {
	    EntityManager em = ((CRUDWorker) dbcontroller).getEntityManager();
	    em.getTransaction().begin();
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not beginn database transaction", arg0, false);
	    pex.putParameter("dbcontroller", dbcontroller);
	    throw pex;
	}
    }

    @Override
    public void endTransaction(final boolean state,final CRUDIF dbcontroller) {
	if (!dbStarted)
	    throw new PersistenceException("DB System not started", new IllegalStateException(), false);
	try {
	    EntityManager em = ((CRUDWorker) dbcontroller).getEntityManager();
	    if (state) {
		em.getTransaction().commit();
	    }
	    else {
		em.getTransaction().rollback();
	    }
	}
	catch (Exception arg0) {
	    logger.log("Could not end transaction", "Persitence Facade", LogLevel.NORMAL, arg0);
	}
    }
}
