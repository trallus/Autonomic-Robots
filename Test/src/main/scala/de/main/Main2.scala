package de.main

import de.game.GameControler;

/**
* A Example to show that the Scala Configuration in the Maven Project
* is working this does the same as the Main Class wirten in Java
* @author Mike
* @version 0.1
*/
object Main2 extends App {

var portNumber = 80

if(args.size == 1)
	portNumber = args(0).toInt

new GameControler(portNumber)
}
