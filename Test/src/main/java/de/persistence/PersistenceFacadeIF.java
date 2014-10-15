package de.persistence;

/**
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 28.05.2014
 */
public interface PersistenceFacadeIF {

    /**
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
     * @throws PersistenceException
     */
    public void shutdownDBSystem();
    
    /**
     * Beginns a new Transaction
     */
    public void beginTransaction();
    
    /**
     * Ends the Transaction with the specified state
     * @param state	true := commit
     * 			false := rollback
     */
    public void endTransaction(boolean state);
}
