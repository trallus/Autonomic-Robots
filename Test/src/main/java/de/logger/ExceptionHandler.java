package de.logger;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Implements the ExceptionHandlerIF and LoggerIF and uses the {@link Calendar}
 * with {@link DateFormat#MEDIUM} for timestemp generation
 * 
 * @author Mike Kiekebusch
 * @version 0.1
 */
public class ExceptionHandler implements ExceptionHandlerIF, LoggerIF {

    /**
     * PrintWriter used to log exclusively for {@link Error}
     */
    private final PrintWriter errorLog;
    /**
     * PrintWriter used to log the rest
     */
    private final PrintWriter normalLog;
    /**
     * The DateFormat that is used for timestemp generation
     */
    private final DateFormat dateFormat;
    /**
     * The Calendar that is used for timestemp generation
     */
    private final Calendar calendar;
    /**
     * The LogLevel of this
     */
    private final LogLevel logLevel;

    /**
     * Inititalizes the ExceptionHandler with the given printwritters and
     * loglevel
     * 
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

    @Override
    public void handle(final Throwable throwable) {
	handle(throwable, null);
    }

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

    @Override
    public void log(final String message, final String caller,
	    final LogLevel lvl) {
	log(message, caller, lvl, null);
    }

    @Override
    public void log(final String message, final String caller,
	    final LogLevel lvl, final Throwable cause) {
	if (logLevel.equals(LogLevel.OFF)) // Deactivated logging
	    return;
	else if (logLevel.compareTo(lvl) < 0)
	    return;

	final String temp = (caller != null ? caller : "") + " with Message: "
		+ message; // Build Message String, with caller if != null

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

    /**
     * Print the given message as line into the normalLog
     * 
     * @param message
     *            Message that will be writen into the log file
     */
    protected void printLog(final String message) {
	normalLog.println(getTimeStemp() + " " + message);
    }

    /**
     * Print the given throwable into the normalLog
     * 
     * @param throwable
     *            which message and stacktrace will be writen into the log file
     */
    private void printLog(final Throwable throwable) {
	normalLog.println(getTimeStemp() + " " + throwable.getMessage());
	throwable.printStackTrace(normalLog);
    }

    /**
     * Print the given message into the errorLog
     * 
     * @param message
     *            Message that will be writen into the errorlog file
     */
    protected void printErrorLog(final String message) {
	errorLog.println(getTimeStemp() + " " + message);
    }

    /**
     * Print the given error into the errorLog
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
     * Getter for the actual timestemp
     * 
     * @return The actual time stemp
     */
    private String getTimeStemp() {
	return dateFormat.format(calendar.getTime());
    }
}
