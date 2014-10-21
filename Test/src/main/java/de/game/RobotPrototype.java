package de.game;

import de.math.Vector2D;

public class RobotPrototype {
	final WeaponPrototype weaponPrototype;
	final long armor;
	final long enginePower;
	final String behaviour;
	
	public RobotPrototype (final WeaponPrototype wp, final long armor, final long enginePower, final String behaviour) {
		this.weaponPrototype = wp;
		this.armor = armor;
		this.enginePower = enginePower;
		this.behaviour = behaviour;
	}

	public WeaponPrototype getWeaponPrototype() {
		return weaponPrototype;
	}

	public long getArmor() {
		return armor;
	}

	public long getEnginePower() {
		return enginePower;
	}

	public String getBehaviour() {
		return behaviour;
	}
	
	public Robot generateRobot (final long id, final Vector2D position) {
		final Robot robot = new Robot(id, position, this);
		return robot;
	}
}