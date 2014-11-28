package de.logger;

/**
 * Interface for methods to get Instances of ExceptionHandlerIF and LoggerIF
 * 
 * @author mike
 * @version 0.1
 */
public interface LoggerAndExceptionHandlerFacadeIF {

    /**
     * Returns a implemented instance of the LoggerIF
     * 
     * @return the LoggerIF instance
     */
    public LoggerIF getLoggerInstance();

    /**
     * Returns a implemented instance of the ExceptionHandlerIF
     * 
     * @return the ExceptionHandlerIF instance
     */
    public ExceptionHandlerIF getExceptionHandlerInstance();
}
