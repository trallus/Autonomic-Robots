package de.persistence;

/**
 * Declaration of the menagment Methods of the Persitence
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 28.05.2014
 */
public interface PersistenceFacadeIF {

    /**
     * Returns a Implementing Instance of the CRUDIF
     * @return A new implementing Object of the CRUDIF
     *         (Create,Read,Update,Delete)
     */
    public CRUDIF getDBController();

    /**
     * Starts the DataBaseSystem
     */
    public void startDBSystem();

    /**
     * Stops the DataBaseSystem
     */
    public void shutdownDBSystem();
    
    /**
     * Beginns a new Transaction
     * @param dbcontroller The DB Controller for which the Transaction should be started
     */
    public void beginTransaction(final CRUDIF dbcontroller);
    
    /**
     * Ends the Transaction with the specified state
     * @param state	true := commit
     * 			false := rollback
     * @param dbcontroller The DB Controller for which the Transaction should be ended
     */
    public void endTransaction(final boolean state, final CRUDIF dbcontroller);
}
