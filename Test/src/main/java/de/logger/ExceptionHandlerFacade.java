package de.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @author mike
 * @version 0.1
 */
public class ExceptionHandlerFacade {
    
    private static PrintWriter errorLog;
    private static PrintWriter normalLog;
    
    public static ExceptionHandlerIF getExceptionHandler(){
	if(errorLog == null || normalLog == null){
	    try{
		    errorLog = new PrintWriter(new BufferedWriter(new FileWriter("error.log", true)), true);
		    normalLog = new PrintWriter(new BufferedWriter(new FileWriter("log.log", true)), true);
		}
		catch(IOException e){
		    //Shit happens...
		}
	    final ExceptionHandler eh = new ExceptionHandler(errorLog, normalLog);
	    eh.printLog("####Logging started####");
	    eh.printErrorLog("####Logging started####");
	    return eh;
	}
	return new ExceptionHandler(errorLog, normalLog);
    }
}
