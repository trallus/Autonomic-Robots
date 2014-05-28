package de.game;

import de.httpServer.HTTPServer;
import de.httpServer.Robot;
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
	public final Robot robot = new Robot ();
	
	public GameControler (int portNumber) throws Exception{
	    	PersistenceFacadeIF persistence = new PersistenceFacade();
		//Start Database
		persistence.startDBSystem();
		//Get Database Controller
		db = persistence.getDBController();
		
		//Start HTTP Server
		httpServer = new HTTPServer( this, portNumber );
		
		//Start Game
	}
}
