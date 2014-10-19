package de.logger;
/**
 * Specifies administrative methods for the central exception handling
 * @author mike
 * @version 0.1
 */
public interface ExceptionHandlerFacadeIF {
    
    /**
     * Start the file logging system
     */
    void startFileLogging();
    /**
     * A new Instance is neede when the file logging is started afterwards
     * @return Instance of a ExceptionHandlerIF
     */
    ExceptionHandlerIF getExceptionHandler();
}
