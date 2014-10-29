package de.game.behaviour;

import java.util.HashMap;

import de.logger.Failure;

/**
 * Thrown when the calles Behaviour could not be initialized
 * @author mike
 * @version 0.1
 */
@SuppressWarnings("serial")
public class CouldNotCreateBehaviourException extends Failure {

    /**
     * @param message
     * @param sendToUser
     * @see de.logger.Failure#Failure(String, boolean)
     */
    public CouldNotCreateBehaviourException(String message, boolean sendToUser) {
	super(message, sendToUser);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param param
     * @param message
     * @param cause
     * @param sendToUser
     * @see de.logger.Failure#Failure(HashMap, String, Exception, boolean)
     */
    public CouldNotCreateBehaviourException(HashMap<String, String> param,
	    String message, Exception cause, boolean sendToUser) {
	super(param, message, cause, sendToUser);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     * @param sendToUser
     * @see de.logger.Failure#Failure(String, Exception, boolean)
     */
    public CouldNotCreateBehaviourException(String message, Exception cause,
	    boolean sendToUser) {
	super(message, cause, sendToUser);
	// TODO Auto-generated constructor stub
    }

}
