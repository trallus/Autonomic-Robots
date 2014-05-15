package de.persistence;

/**
 * A wrapper exception for all exceptions that occur in the persistence system
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 15.05.2014
 */
public class PersistenceException extends RuntimeException {

    private static final long serialVersionUID = 7433466422648828178L;
    
    /**
     * @param arg0 The Throwable that should be wraped by this exception
     */
    public PersistenceException(Throwable arg0){
	super(arg0);
    }
}
