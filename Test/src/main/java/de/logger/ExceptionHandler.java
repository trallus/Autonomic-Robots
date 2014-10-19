package de.logger;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * @author mike
 * @version 0.1
 */
public class ExceptionHandler implements ExceptionHandlerIF {

    private final PrintWriter errorLog;
    private final PrintWriter normalLog;
    private final DateFormat dateFormat;
    private final Calendar calendar;

    /**
     * @param errorLog
     * @param normalLog
     */
    public ExceptionHandler(PrintWriter errorLog, PrintWriter normalLog) {
	this.errorLog = errorLog;
	this.normalLog = normalLog;
	this.dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
		DateFormat.MEDIUM);
	this.calendar = Calendar.getInstance();
    }

    /**
     * @see de.logger.ExceptionHandlerIF#handle(java.lang.Throwable,
     *      java.util.Map)
     */
    @Override
    public void handle(Throwable throwable, Map<String, Object> replyJson) {
	if(throwable instanceof Failure){
	    final Failure fail = (Failure) throwable;
	    final String message = fail.getMessage();
	    if(fail.getSendToUser())
		replyJson.put("failure", message);
	    printLog(message);
	}
	else if(throwable instanceof Error){
	    final Error error = (Error) throwable;
	    printErrorLog(error);
	}
	else{
	    printLog(throwable);
	}
    }

    /**
     * @see de.logger.ExceptionHandlerIF#handle(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void handle(String message, String caller) {
	handle(message, caller, null);
    }

    /**
     * @see de.logger.ExceptionHandlerIF#handle(java.lang.String,
     *      java.lang.String, java.lang.Throwable)
     */
    @Override
    public void handle(String message, String caller, Throwable cause) {
	final String temp = caller+" with Message: "+message;
	if(cause == null){
	    printLog(temp);
	}
	else if(cause instanceof Error){
	    final Error error = (Error) cause;
	    printErrorLog(temp+" with cause:");
	    printErrorLog(error);
	}
	else{
	    printLog(temp+" with cause:");
	    printLog(cause);
	}
    }

    /**
     * 
     * @param message
     *            Message that will be writen into the log file
     */
    protected void printLog(String message) {
	normalLog.println(getTimeStemp() + " " + message);
    }

    /**
     * 
     * @param throwable
     *            which message and stacktrace will be writen into the log file
     */
    private void printLog(Throwable throwable) {
	normalLog.println(getTimeStemp() + " " + throwable.getMessage());
	throwable.printStackTrace(normalLog);
    }

    /**
     * 
     * @param message
     *            Message that will be writen into the errorlog file
     */
    protected void printErrorLog(String message) {
	errorLog.println(getTimeStemp() + " " + message);
    }

    /**
     * 
     * @param error
     *            which message and stacktrace will be writen into the errorlog
     *            file
     */
    private void printErrorLog(Error error) {
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
