package de.logger;

/**
 * Interface for the logging of messages
 * 
 * @author Mike Kiekebusch
 * @version 0.1
 */
public interface LoggerIF {

    /**
     * Logs the given message and caller information
     * 
     * @param message
     *            Failure message
     * @param caller
     *            Caller of this methode
     * @param lvl
     *            The lvl at which this things will be logged
     */
    void log(final String message, final String caller, final LogLevel lvl);

    /**
     * Logs the given message, caller and cause information
     * 
     * @param message
     *            The failure message
     * @param caller
     *            The caller of this methode
     * @param lvl
     *            The lvl at which this things will be logged
     * @param cause
     *            The cause for the logging
     */
    void log(final String message, final String caller, final LogLevel lvl,
	    final Throwable cause);
}
