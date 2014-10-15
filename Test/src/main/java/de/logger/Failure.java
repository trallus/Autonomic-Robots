package de.logger;

import java.util.HashMap;
/**
 * A Simple Head Wrapper for all Exceptions
 * @author Mike
 * @version 0.1
 */
@SuppressWarnings("serial")
public class Failure extends RuntimeException {
	/**
	 * List of all Parameters
	 */
	private final HashMap<String, String> param;
	/**
	 * Failure Message
	 */
	private final String message;
	
	private final boolean sendToUser;
	/**
	 * @param param Parameter of the methode that issues this failure
	 * @param message The failure Message for the invking Message
	 * @param cause Exception that Caused this Failure
	 */
	public Failure(HashMap<String, String> param, String message, Exception cause, boolean sendToUser) {
		super(cause);
		this.sendToUser = sendToUser;
		this.param = param;
		this.message = message;
	}

	/**
	 * @return The Message of this Failure with Parameter Key-Value pairs
	 */
	public String getMessage (){
		StringBuilder ausgabe = new StringBuilder(message);
		for(String key : param.keySet()){
			ausgabe.append(" ");
			ausgabe.append(key);
			ausgabe.append(": ");
			ausgabe.append(param.get(key));
		}
		return ausgabe.toString();
	}
}
