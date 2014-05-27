package de.game;

public class Weapon {
	
	private int range;
	private int damage;
	private int rateOfFire;
	
	
	/**
	 * 
	 * @param range
	 * @param damage
	 * @param rateOfFire
	 */
	public Weapon(int range, int damage, int rateOfFire) {
		super();
		this.range = range;
		this.damage = damage;
		this.rateOfFire = rateOfFire;
	}


	//Getter und Setter: 
	
	public int getRange() {
		return range;
	}



	public void setRange(int range) {
		this.range = range;
	}



	public int getDamage() {
		return damage;
	}



	public void setDamage(int damage) {
		this.damage = damage;
	}



	public int getRateOfFire() {
		return rateOfFire;
	}



	public void setRateOfFire(int rateOfFire) {
		this.rateOfFire = rateOfFire;
	}
	
	
	
	

}
