package de.persistence;

import de.logger.Failure;

/**
 * A wrapper exception for all exceptions that occur in the persistence system
 * @author Mike Kiekebusch
 * @version 0.2
 * @since 15.05.2014
 */
@SuppressWarnings("serial")
public class PersistenceException extends Failure {

    public PersistenceException(final String message, final Exception cause,
	    final boolean sendToUser) {
	super(message, cause, sendToUser);
    }
    
    
}
