package de.httpServer;

import de.logger.Failure;

public class NameInUseException extends Failure {

	public NameInUseException(String message, boolean sendToUser) {
		super(message, sendToUser);
	}

}
