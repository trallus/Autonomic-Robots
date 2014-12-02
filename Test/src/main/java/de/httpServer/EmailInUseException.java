package de.httpServer;

import java.util.HashMap;

import de.logger.Failure;

/**
 * Thrown if the Email is in use
 * @author Christian Köditz
 * @version 0.1
 * @since 15.10.2014
 */
public class EmailInUseException extends Failure {

	/**
	 * @see de.logger.Failure#Failure(String, boolean)
	 */
	public EmailInUseException(String message,  boolean sendToUser) {
		super(message, sendToUser);
	}

}
