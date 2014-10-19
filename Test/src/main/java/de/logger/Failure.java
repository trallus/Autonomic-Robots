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
	 * @param message The failure Message, cant be null
	 * @param cause Exception that Caused this Failure
	 * @param sendToUser if this Failure should be communicated to the user
	 */
	public Failure(HashMap<String, String> param, String message, Exception cause, boolean sendToUser) {
		super(cause);
		this.sendToUser = sendToUser;
		this.param = param;
		this.message = message;
	}
	
	/**
	 * @param message The failure Message, cant be null
	 * @param cause Exception that Caused this Failure
	 * @param sendToUser if this Failure should be communicated to the user
	 */
	public Failure(String message, Exception cause, boolean sendToUser) {
		super(cause);
		this.sendToUser = sendToUser;
		this.message = message;
		this.param = new HashMap<String, String>();
	}
	
	/**
	 * Add the Parameter name value pair to the parameterlist 
	 * @param name the name of the param 
	 * @param value the value of the param
	 */
	public void putParameter(String name, Object value){
	    if(value instanceof String)
		param.put(name, (String)value);
	    else if(value != null)
		param.put(name, value.toString());
	    else
		param.put(name, "null");
	}

	/**
	 * @return The Message of this Failure with Parameter Key-Value pairs
	 */
	public String getMessage (){
		StringBuilder ausgabe = new StringBuilder(message);
		if(param != null){
			for(String key : param.keySet()){
				ausgabe.append(" ");
				ausgabe.append(key);
				ausgabe.append(": ");
				ausgabe.append(param.get(key));
			}
		}
		return ausgabe.toString();
	}
	/**
	 * @return the value of sendToUser
	 */
	public boolean getSendToUser(){
		return sendToUser;
	}
}
