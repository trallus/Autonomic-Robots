package de.game.weapon;

import java.util.HashMap;

import de.logger.Failure;

/**
 * Thrown when the Weapon could not be created
 * 
 * @author mike
 * @version 0.1
 */
@SuppressWarnings("serial")
public class CouldNotCreateWeaponException extends Failure {

    /**
     * Gives all Parameters to it's Super Constructor
     * 
     * @see de.logger.Failure#Failure(String, boolean)
     */
    public CouldNotCreateWeaponException(final String message,
	    final boolean sendToUser) {
	super(message, sendToUser);
    }

    /**
     * Gives all Parameters to it's Super Constructor
     * 
     * @see de.logger.Failure#Failure(HashMap, String, Exception, boolean)
     */
    public CouldNotCreateWeaponException(final HashMap<String, String> param,
	    final String message, final Exception cause,
	    final boolean sendToUser) {
	super(param, message, cause, sendToUser);
    }

    /**
     * Gives all Parameters to it's Super Constructor
     * 
     * @see de.logger.Failure#Failure(String, Exception, boolean)
     */
    public CouldNotCreateWeaponException(final String message,
	    final Exception cause, boolean sendToUser) {
	super(message, cause, sendToUser);
    }

}
