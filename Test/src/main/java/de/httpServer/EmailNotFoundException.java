package de.httpServer;

import de.logger.Failure;

/**
 * Thrown if the Email was not found
 * @author Christian Köditz
 * @version 0.1
 * @since 15.10.2014
 */
public class EmailNotFoundException extends Failure {

	/**
	 * @see de.logger.Failure#Failure(String, boolean)
	 */
	public EmailNotFoundException(String message, boolean sendToUser) {
		super(message, sendToUser);
	}
	
}
