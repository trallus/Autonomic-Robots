package de.logger;

import java.io.PrintWriter;

/**
 * Facade that returns Instances of LoggerIF and ExceptionHandlerIF
 * 
 * @author mike
 * @version 0.3
 */
public class LogExceptionFacade implements LoggerAndExceptionHandlerFacadeIF {

    /**
     * The instance for the ErrorLog
     */
    private final PrintWriter errorLog;
    /**
     * The instance for the NormalLog
     */
    private final PrintWriter normalLog;
    /**
     * The Level of logging the Instances will handle
     */
    private final LogLevel logLevel;

    /**
     * Creates Instance of LogExceptionFacade
     * 
     * @param errorLog
     *            The Printwriter in which all error will be logged, must be not
     *            null and writable if the LogLevel is not OFF
     * @param normalLog
     *            The Printwriter in which all nonerror will be logged, must be
     *            not null and writable if the LogLevel is not OFF
     * @param logLevel
     *            The level of logging that will be done. Warnig
     *            {@link LogLevel#OFF} means that there will be no sort of
     *            loggin for Console use{@link LogLevel#DEBUG}.
     */
    public LogExceptionFacade(final PrintWriter errorLog,
	    final PrintWriter normalLog, LogLevel logLevel) {
	if (!logLevel.equals(LogLevel.OFF)
		&& (errorLog == null || normalLog == null))
	    throw new IllegalArgumentException("PrintWriters can't be null");
	if (!logLevel.equals(LogLevel.OFF)
		&& (errorLog.checkError() || normalLog.checkError()))
	    throw new IllegalArgumentException("PrintWriter must be writable");
	// TODO Entscheidung ob Printwriter oder Verzeichnis und Writer selber
	// anlegen
	this.errorLog = errorLog;
	this.normalLog = normalLog;
	this.logLevel = logLevel;
	if (!logLevel.equals(LogLevel.OFF)) {
	    normalLog.println("####Logging started####");
	    errorLog.println("####Logging started####");
	}
    }

    @Override
    public LoggerIF getLoggerInstance() {
	return new ExceptionHandler(errorLog, normalLog, logLevel);
    }

    @Override
    public ExceptionHandlerIF getExceptionHandlerInstance() {
	return new ExceptionHandler(errorLog, normalLog, logLevel);
    }
}
