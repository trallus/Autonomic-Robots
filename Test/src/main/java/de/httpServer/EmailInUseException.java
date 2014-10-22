package de.httpServer;

import java.util.HashMap;

import de.logger.Failure;

public class EmailInUseException extends Failure {

	public EmailInUseException(String message, String caller, boolean sendToUser) {
		super(message, caller, sendToUser);
	}

}
