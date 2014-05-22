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
     * @param args
     *            does nothing
     */
    public static void main(String[] args) throws Exception {
	if (args.length != 1) {
	    System.err.println("Usage java Main <port Number>");
	    System.exit(-2);
	}
	final int portNumber = Integer.parseInt(args[0]);
	new GameControler(portNumber);

    }
}
