package de.httpServer;

import de.logger.Failure;

/**
 * Thrown if the Name is already in use
 * @author Christian Köditz
 * @version 0.1
 * @since 15.10.2014
 */
public class NameInUseException extends Failure {

	/**
	 * @see de.logger.Failure#Failure(String, boolean)
	 */
	public NameInUseException(String message, boolean sendToUser) {
		super(message, sendToUser);
	}

}
