package de.httpServer;

import de.logger.Failure;

public class EmailNotFoundException extends Failure {

	public EmailNotFoundException(String message, boolean sendToUser) {
		super(message, sendToUser);
	}
	
}
