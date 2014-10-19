package de.logger;

import java.util.Map;

/**
 * Specifies methods for central Exception and loggin handling
 * 
 * @author mike
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
     * Logs the given message and caller information but will not inform the frontend
     * @param message Failure message
     * @param caller Caller of this methode
     */
    void handle(String message, String caller);

    /**
     * Logs the given message, caller and cause information but will not inform the frontend
     * @param message The failure message
     * @param caller The caller of this methode
     * @param cause The cause for the logging
     */
    void handle(String message, String caller, Throwable cause);

}
