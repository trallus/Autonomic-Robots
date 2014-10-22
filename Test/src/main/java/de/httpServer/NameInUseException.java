package de.httpServer;

import de.logger.Failure;

public class NameInUseException extends Failure {

	public NameInUseException(String message, String caller, boolean sendToUser) {
		super(message, caller, sendToUser);
	}

}
