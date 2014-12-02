package de.game.behaviour;

import java.util.HashMap;

import de.logger.Failure;

/**
 * Thrown when the calles Behaviour could not be initialized
 * 
 * @author mike
 * @version 0.1
 */
@SuppressWarnings("serial")
public class CouldNotCreateBehaviourException extends Failure {

    /**
     * Initializes this CouldNotCreateBehaviourException with the given Values
     * 
     * @param message
     *            The Failure message of this exception
     * @param sendToUser
     *            flag that indicates if this exception should be propagated to
     *            the user, if possible
     * @see de.logger.Failure#Failure(String, boolean)
     */
    public CouldNotCreateBehaviourException(String message, boolean sendToUser) {
	super(message, sendToUser);
    }

    /**
     * Initializes this CouldNotCreateBehaviourException with the given Values
     * 
     * @param param
     *            Map of Parametrs and Values of the Methode that invokes this
     *            Exception
     * @param message
     *            The Failure message of this exception
     * @param sendToUser
     *            flag that indicates if this exception should be propagated to
     *            the user, if possible
     * @param cause
     *            The Exception that caused the throw of this Exception
     * @see de.logger.Failure#Failure(HashMap, String, Exception, boolean)
     */
    public CouldNotCreateBehaviourException(HashMap<String, String> param,
	    String message, Exception cause, boolean sendToUser) {
	super(param, message, cause, sendToUser);
    }

    /**
     * Initializes this CouldNotCreateBehaviourException with the given Values
     * 
     * @param message
     *            The Failure message of this exception
     * @param sendToUser
     *            flag that indicates if this exception should be propagated to
     *            the user, if possible
     * @param cause
     *            The Exception that caused the throw of this Exception
     * @see de.logger.Failure#Failure(String, Exception, boolean)
     */
    public CouldNotCreateBehaviourException(String message, Exception cause,
	    boolean sendToUser) {
	super(message, cause, sendToUser);
    }

}
