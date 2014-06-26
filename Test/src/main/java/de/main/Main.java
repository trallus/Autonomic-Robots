package de.main;

import de.game.GameControler;

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
     * @param args The First entry is the portnumber
     */
    public static void main(String[] args) throws Exception {
	int portNumber;
	if (args.length != 1) {
	    portNumber = 80;
	} else {
		portNumber = Integer.parseInt(args[0]);
	}
	new GameControler(portNumber);

    }
}
