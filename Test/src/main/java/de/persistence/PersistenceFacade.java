package de.persistence;

import de.data.User;

/**
 * The Facade for the Persistence system
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 15.05.2014
 */
public class PersistenceFacade {
    
    /**
     * @return A new implementing Object of the CRUDIF (Create,Read,Update,Delete)
     */
    public final static CRUDIF getDBController(){
	return new CRUDWorker();
    }
    
    /**
     * Starts the DataBaseSystem
     * @throws PersistenceException might be thrown if an error occurs
     */
    public final static void startDBSystem(){
	final CRUDIF temp = new CRUDWorker();
	temp.readAll(User.class);
    }
}
