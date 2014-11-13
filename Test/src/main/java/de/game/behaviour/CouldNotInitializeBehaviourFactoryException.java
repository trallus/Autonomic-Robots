package de.game.behaviour;

import java.util.HashMap;

import de.logger.Failure;

/**
 * Thrown when the BehaviourFactory could not be created
 * @author mike
 * @version 0.1
 */
@SuppressWarnings("serial")
public class CouldNotInitializeBehaviourFactoryException extends Failure {

    /**
     * Initializes the Exception with the given Parameters
     * @param message The info text of this exception
     * @param sendToUser true when this exception should be send to the user, else false
     */
    public CouldNotInitializeBehaviourFactoryException(String message,
	    boolean sendToUser) {
	super(message, sendToUser);
    }

    /**
     * Initializes the Exception with the given Parameters
     * @param param Parameters of the Methode that trowhs this exception
     * @param message The info text of this exception
     * @param cause The exception that invoked the throw of this exception
     * @param sendToUser true when this exception should be send to the user, else false
     */
    public CouldNotInitializeBehaviourFactoryException(
	    HashMap<String, String> param, String message, Exception cause,
	    boolean sendToUser) {
	super(param, message, cause, sendToUser);
    }

    /**
     * Initializes the Exception with the given Parameters
     * @param message The info text of this exception
     * @param cause The exception that invoked the throw of this exception
     * @param sendToUser true when this exception should be send to the user, else false
     */
    public CouldNotInitializeBehaviourFactoryException(String message,
	    Exception cause, boolean sendToUser) {
	super(message, cause, sendToUser);
    }

}
