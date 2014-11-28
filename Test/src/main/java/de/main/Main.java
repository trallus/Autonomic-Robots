package de.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import de.httpServer.HTTPServer;
import de.logger.LogExceptionFacade;
import de.logger.LogLevel;
import de.logger.LoggerAndExceptionHandlerFacadeIF;

/**
 * The Entry Point for the Program
 * 
 * @author Mike Kiekebusch
 * @version 0.2
 */
public class Main {

    /**
     * The Entry Point for the program
     * 
     * @param args
     *            The First entry is the portnumber
     */
    public static void main(String[] args) throws Exception {
	final int portNumber;
	final LogLevel logLevel;
	if (args.length < 1) { // TODO change so that the log level can be set
	    portNumber = 80;
	    logLevel = LogLevel.NORMAL;
	}
	else {
	    portNumber = Integer.parseInt(args[0]);
	    logLevel = LogLevel.NORMAL;
	}

	// Initialize Logging

	final PrintWriter normalWriter = new PrintWriter(new BufferedWriter(
		new FileWriter("log.log",true)),true);
	final PrintWriter errorWriter = new PrintWriter(new BufferedWriter(
		new FileWriter("error.log",true)),true);
	final LoggerAndExceptionHandlerFacadeIF logFacade = new LogExceptionFacade(
		errorWriter, normalWriter, logLevel);

	// Start HTTP Server

	String httpPath = System.getProperty("user.dir") + "/http/final/";

	String keyURI = "serverRequest";

	try {
	    new HTTPServer(keyURI, httpPath, portNumber, logFacade);
	}
	catch (Throwable arg) { //Last catch so that all can be handeled and logged
	    logFacade.getExceptionHandlerInstance().handle(arg);
	}
    }
}
