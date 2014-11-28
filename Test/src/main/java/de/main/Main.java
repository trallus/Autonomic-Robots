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
	int portNumber;
	if (args.length != 1) { // TODO change so that the log level can be set
	    portNumber = 80;
	}
	else {
	    portNumber = Integer.parseInt(args[0]);
	}
	System.out.println(args.length);

	// Initialize Logging

	final PrintWriter normalWriter = new PrintWriter(new BufferedWriter(
		new FileWriter("log.log")));
	final PrintWriter errorWriter = new PrintWriter(new BufferedWriter(
		new FileWriter("error.log")));
	final LoggerAndExceptionHandlerFacadeIF logFacade = new LogExceptionFacade(
		errorWriter, normalWriter, LogLevel.NORMAL);

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
