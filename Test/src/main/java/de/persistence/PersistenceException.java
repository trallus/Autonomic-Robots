package de.persistence;

import java.util.HashMap;

import de.logger.Failure;

/**
 * A wrapper exception for all exceptions that occur in the persistence system
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 15.05.2014
 */
@SuppressWarnings("serial")
public class PersistenceException extends Failure {

	/**
	 * @see de.logger.Failure#Failure(HashMap, String, Exception, boolean)
	 */
	public PersistenceException(String message,
			Exception cause, boolean sendToUser) {
		super(message, cause, sendToUser);
	}
}
