package de.httpServer;


public class Robot {
	private double posX = (int) ( Math.random()*100 );
	private double posY = (int) ( Math.random()*100 );
	private int speed = 20;
	private double direction = 0;
	private long timeStamp = System.currentTimeMillis();
	
	
	public Position getNextPosition () {
		direction += .1;
		posX = Math.sin(direction) * 100 + 100;
		posY = Math.cos(direction) * 100 + 100;
		return new Position(posX, posY);
	}
}
