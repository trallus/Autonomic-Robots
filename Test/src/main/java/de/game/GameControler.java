package de.game;

import de.HTTPServer.HTTPServer;
import de.HTTPServer.Robot;
import de.data.DBUser;
import de.data.User;
import de.persistence.CRUDIF;
import de.persistence.PersistenceFacade;

/**
 * Controls the Game
 * @author ko
 *
 */
public class GameControler {
	final HTTPServer httpServer;
	final CRUDIF db;
	public final Robot robot = new Robot ();
	
	public GameControler () throws Exception{
		//Start Database
		PersistenceFacade.startDBSystem();
		//Get Database Controller
		db = PersistenceFacade.getDBController();
		
		//Start HTTP Server
		httpServer = new HTTPServer( this );
	}
}
