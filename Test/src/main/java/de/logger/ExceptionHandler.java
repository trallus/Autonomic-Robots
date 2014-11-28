package de.logger;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * @author mike
 * @version 0.1
 */
public class ExceptionHandler implements ExceptionHandlerIF, LoggerIF {

    private final PrintWriter errorLog;
    private final PrintWriter normalLog;
    private final DateFormat dateFormat;
    private final Calendar calendar;
    private final LogLevel logLevel;

    /**
     * @param errorLog
     *            The Printwritter for the errorLog
     * @param normalLog
     *            The Printwritter for the normalLog
     * @param logLevel
     *            The LogLevel of this Handler. Warnig {@link LogLevel#OFF}
     *            means that there will be no sort of loggin for Console use
     *            {@link LogLevel#DEBUG}.
     */
    public ExceptionHandler(final PrintWriter errorLog,
	    final PrintWriter normalLog, final LogLevel logLevel) {
	this.errorLog = errorLog;
	this.normalLog = normalLog;
	this.dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
		DateFormat.MEDIUM);
	this.calendar = Calendar.getInstance();
	this.logLevel = logLevel;
    }

    /**
     * @see de.logger.ExceptionHandlerIF#handle(Throwable)
     */
    @Override
    public void handle(final Throwable throwable) {
	handle(throwable, null);
    }

    /**
     * @see de.logger.ExceptionHandlerIF#handle(java.lang.Throwable,
     *      java.util.Map)
     */
    @Override
    public void handle(final Throwable throwable, Map<String, Object> replyJson) {
	if (throwable instanceof Failure) { // Frontend Message
	    final Failure fail = (Failure) throwable;
	    final String message = fail.getMessage();
	    if (fail.getSendToUser() && replyJson != null)
		replyJson.put("failure", message);
	}
	log("", null, logLevel, throwable); // Delegating the logging at the
					    // specified methode
    }

    /**
     * @see de.logger.ExceptionHandlerIF#log(java.lang.String, java.lang.String)
     */
    @Override
    public void log(final String message, final String caller,
	    final LogLevel lvl) {
	log(message, caller, lvl, null);
    }

    /**
     * @see de.logger.ExceptionHandlerIF#log(java.lang.String, java.lang.String,
     *      java.lang.Throwable)
     */
    @Override
    public void log(final String message, final String caller,
	    final LogLevel lvl, final Throwable cause) {
	// TODO Redundanz reduzieren & LogLevel Check verbessern
	if (logLevel.equals(LogLevel.OFF)) // Deactivated logging
	    return;

	final String temp = (caller != null ? caller : "") + " with Message: "
		+ message; // Build Message String, with caller if != null

	if (logLevel.equals(LogLevel.DEBUG) && lvl.equals(LogLevel.DEBUG)) { // Console
									     // logging
	    System.out.println(temp);
	    if (cause != null)
		cause.printStackTrace(System.out);
	    return;
	}
	else if (lvl.equals(LogLevel.NORMAL)) {
	    if (cause == null) {
		printLog(temp);
	    }
	    else if (cause instanceof Error) {
		final Error error = (Error) cause;
		printErrorLog(temp + " with cause:");
		printErrorLog(error);
	    }
	    else {
		printLog(temp + " with cause:");
		printLog(cause);
	    }
	}
	else {
	    throw new IllegalArgumentException("Unknown LogLevel: " + lvl);
	}
	// TODO Redundanz reduzieren & LogLevel Check verbessern
    }

    /**
     * 
     * @param message
     *            Message that will be writen into the log file
     */
    protected void printLog(final String message) {
	normalLog.println(getTimeStemp() + " " + message);
    }

    /**
     * 
     * @param throwable
     *            which message and stacktrace will be writen into the log file
     */
    private void printLog(final Throwable throwable) {
	normalLog.println(getTimeStemp() + " " + throwable.getMessage());
	throwable.printStackTrace(normalLog);
    }

    /**
     * 
     * @param message
     *            Message that will be writen into the errorlog file
     */
    protected void printErrorLog(final String message) {
	errorLog.println(getTimeStemp() + " " + message);
    }

    /**
     * 
     * @param error
     *            which message and stacktrace will be writen into the errorlog
     *            file
     */
    private void printErrorLog(final Error error) {
	errorLog.println(getTimeStemp() + " " + error.getMessage());
	error.printStackTrace(errorLog);
    }

    /**
     * @return The actual time stemp
     */
    private String getTimeStemp() {
	return dateFormat.format(calendar.getTime());
    }
}
