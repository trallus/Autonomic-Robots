package de.game;

public class Robot {
	
	private int armour; 
	private int enginePower;
	private int healthPoints;
	private double speed;
	private double directionRad;
	private Account owner;
	private Weapon weapon;
	
	
	public Robot(int armour, int enginePower, int healthPoints, double speed,
			double directionRad, Account owner, Weapon weapon) {
		this.armour = armour;
		this.enginePower = enginePower;
		this.healthPoints = healthPoints;
		this.speed = speed;
		this.directionRad = directionRad;
		this.owner = owner;
		this.weapon = weapon;
	}

	// TODO: Interface implementieren
	

	public int getArmour() {
		return armour;
	}

	public void setArmour(int armour) {
		this.armour = armour;
	}

	public int getEnginePower() {
		return enginePower;
	}

	public void setEnginePower(int enginePower) {
		this.enginePower = enginePower;
	}

	public int getHealthPoints() {
		return healthPoints;
	}

	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getDirectionRad() {
		return directionRad;
	}

	public void setDirectionRad(double directionRad) {
		this.directionRad = directionRad;
	}

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
	

}
