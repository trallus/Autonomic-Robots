package de.httpServer;

import java.util.HashMap;

import de.logger.Failure;

public class EmailInUseException extends Failure {

	public EmailInUseException(String message, boolean sendToUser) {
		super(message, sendToUser);
	}

}
