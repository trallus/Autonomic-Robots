package de.logger;

import java.util.Map;

/**
 * Specifies methods for central Exception and loggin handling
 * 
 * @author Mike Kiekebusch
 * @version 0.1
 */
public interface ExceptionHandlerIF {

    /**
     * The only methode that can send a Failure Message to the Frontend but only
     * if the throwable has subtype Failure
     * 
     * @param throwable
     *            The throwable that should be handeled
     * @param replyJson
     *            The answer Json in which the failure message will be writen
     */
    void handle(Throwable throwable, Map<String, Object> replyJson);

    /**
     * Handels Exceptions so that they can be logged but can't send anything to the frontend
     * @param throwable The throwable that shoudl be handeled
     */
    void handle(Throwable throwable);

}
