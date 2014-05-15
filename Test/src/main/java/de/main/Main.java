package de.main;

import de.data.User;
import de.persistence.CRUDIF;
import de.persistence.PersistenceFacade;

/**
 * The Entry Point for the Programm and for Christian ;P
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 15.05.2014
 */
public class Main {
    
    /**
     * The Entry Point for the program
     * @param args does nothing
     */
    public static void main(String[] args){
	//Start Database
	PersistenceFacade.startDBSystem();
	//Get Database Controller
	final CRUDIF db = PersistenceFacade.getDBController();
	//Create and insert User in Database
	final User user = new User("Tester", "Test@test.de", 45781549);
	db.insert(user);
    }
}
