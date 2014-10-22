package de.httpServer;

import de.logger.Failure;

public class EmailNotFoundException extends Failure {

	public EmailNotFoundException(String message, String caller, boolean sendToUser) {
		super(message, caller, sendToUser);
	}
	
}
