package de.game;

import java.util.ArrayList;

public class Battle {
	
	public static final int NUMBER_OF_PLAYERS = 4;
	
	private int fieldRadius;
	private long timeLimitMs;
	private ArrayList<Account>players = new ArrayList(NUMBER_OF_PLAYERS);
	/**
	 * 
	 * @param fieldRadius
	 * @param timeLimitMs
	 */
	public Battle(int fieldRadius, long timeLimitMs){
		this.fieldRadius = fieldRadius;
		this.timeLimitMs = timeLimitMs;
	}
	
	public Battle(int fieldRadius, long timeLimitMs, ArrayList<Account>players){
		this.fieldRadius = fieldRadius;
		this.timeLimitMs = timeLimitMs;
		if(players.size() == NUMBER_OF_PLAYERS)this.players = players;
	}

	
	//Getter und Setter: 
	
	public int getFieldRadius() {
		return fieldRadius;
	}

	public void setFieldRadius(int fieldRadius) {
		this.fieldRadius = fieldRadius;
	}

	public long getTimeLimitMs() {
		return timeLimitMs;
	}

	public void setTimeLimitMs(long timeLimitMs) {
		this.timeLimitMs = timeLimitMs;
	}
	
	/**
	 * Adds Player to the Battle if the constraints are complied with. In case of adding the player to the battle
	 * the method returns the value true, false else. 
	 * @param player
	 * @return boolean
	 */
	public boolean addPlayerToBattle(Account player){
		if(players.size() < NUMBER_OF_PLAYERS){
			players.add(player);
			return true;
		}
		return false;
	}
	

}
