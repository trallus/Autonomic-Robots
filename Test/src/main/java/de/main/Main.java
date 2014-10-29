package de.main;

import de.game.behaviour.BehaviourFactory;
import de.httpServer.HTTPServer;

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
		if (args.length != 1) {
			portNumber = 80;
		} else {
			portNumber = Integer.parseInt(args[0]);
		}

		//BehaviourFactory starten (XML wird geladen)
		BehaviourFactory.getBehaviourFactory();
		
		// Start HTTP Server

		String httpPath = System.getProperty("user.dir") + "/http/final/";
		String keyURI = "serverRequest";

		new HTTPServer(keyURI, httpPath, portNumber);
	}
}
