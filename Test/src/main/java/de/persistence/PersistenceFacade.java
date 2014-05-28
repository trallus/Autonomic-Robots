package de.persistence;

import java.sql.DriverManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.data.DBUser;

/**
 * The Facade for the Persistence system
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 15.05.2014
 */
public class PersistenceFacade {
    
    private static boolean dbStarted = false;
    private static EntityManagerFactory emf;
    
    /**
     * @return A new implementing Object of the CRUDIF (Create,Read,Update,Delete)
     */
    public final static CRUDIF getDBController(){
	if(!dbStarted)
	    throw new PersistenceException(new IllegalStateException("DB System not started"));
	try{
	    return new CRUDWorker(emf.createEntityManager(), emf.getPersistenceUnitUtil());
	}
	catch(Throwable arg0){
	    throw new PersistenceException(arg0);
	}
    }
    
    /**
     * Starts the DataBaseSystem
     * @throws PersistenceException might be thrown if an error occurs
     */
    public final static void startDBSystem(){
	try{
	    emf = Persistence.createEntityManagerFactory("monster");
	    dbStarted = true;
	}
	catch(Throwable arg0){
	    throw new PersistenceException(arg0);
	}
    }
    
    public final static void shutdownDBSystem(){
	if(!dbStarted)
	    throw new PersistenceException(new IllegalStateException("DB System not started"));
	try{
	    DriverManager.getConnection("jdbc:derby:DB;shutdown=true");
	    dbStarted = false;
	}
	catch(Throwable arg0){
	    throw new PersistenceException(arg0);
	}
    }
}
