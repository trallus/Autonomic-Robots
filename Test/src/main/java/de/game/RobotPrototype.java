package de.game;

import de.game.behaviour.BehaviourFactory;
import de.game.weapon.WeaponPrototype;
import de.httpServer.User;
import de.logger.LoggerAndExceptionHandlerFacadeIF;
import de.math.Vector2D;

public class RobotPrototype {
	final WeaponPrototype weaponPrototype;
	final long armor;
	final long enginePower;
	final String behaviour;
	final LoggerAndExceptionHandlerFacadeIF logFacade;
	
	public RobotPrototype (final WeaponPrototype wp, final long armor, final long enginePower, final String behaviour, final LoggerAndExceptionHandlerFacadeIF logFacade) {
		this.weaponPrototype = wp;
		this.armor = armor;
		this.enginePower = enginePower;
		this.behaviour = behaviour;
		this.logFacade = logFacade;
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
	
	public Robot generateRobot (final long id, final Vector2D position, final User user, final BehaviourFactory behaviourFactory) {
		final Robot robot = new Robot(id, position, this, weaponPrototype.generateWeapon(), user,behaviourFactory, behaviour, logFacade);
		return robot;
	}
}
