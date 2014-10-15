package de.game;

import de.httpServer.HTTPServer;
import de.persistence.CRUDIF;
import de.persistence.PersistenceFacade;
import de.persistence.PersistenceFacadeIF;

/**
 * Controls the Game
 * @author ko
 *
 */
public class GameControler {
	final HTTPServer httpServer;
	final CRUDIF db;
	
	public GameControler (int portNumber) throws Exception{
	    	PersistenceFacadeIF persistence = new PersistenceFacade();
		//Start Database
		persistence.startDBSystem();
		//Get Database Controller
		db = persistence.getDBController();
		
		//Start HTTP Server

        String httpPath = System.getProperty("user.dir") + "/http/final/";
        String keyURI = "serverRequest";
        	    
        httpServer = new HTTPServer(keyURI, httpPath, portNumber);
		
		//Start Game
	}
}
