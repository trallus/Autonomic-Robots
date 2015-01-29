package de.game;

import de.game.behaviour.BehaviourFactory;
import de.game.weapon.WeaponPrototype;
import de.httpServer.User;
import de.logger.LoggerAndExceptionHandlerFacadeIF;
import de.math.Vector2D;

/**
 * @author ko
 * holds the values for a robot
 */
public class RobotPrototype {
	/**
	 * a prototype of a weapon
	 */
	final WeaponPrototype weaponPrototype;
	/**
	 * the severe of the armor
	 */
	final long armor;
	/**
	 * the power of the engine
	 */
	final long enginePower;
	/**
	 * the behavior which control the robot
	 */
	final String behaviour;
	/**
	 * to log any thing
	 */
	final LoggerAndExceptionHandlerFacadeIF logFacade;
	
	/**
	 * @param wp prototype of a weapon
	 * @param armor the severe of the armor
	 * @param enginePower the power of the engine
	 * @param behaviour the behavior which control the robot
	 * @param logFacade to log any thing
	 */
	public RobotPrototype (final WeaponPrototype wp, final long armor, final long enginePower, final String behaviour, final LoggerAndExceptionHandlerFacadeIF logFacade) {
		this.weaponPrototype = wp;
		this.armor = armor;
		this.enginePower = enginePower;
		this.behaviour = behaviour;
		this.logFacade = logFacade;
	}

	/**
	 * @return severe of the armor
	 */
	public long getArmor() {
		return armor;
	}

	/**
	 * @return power of the engine
	 */
	public long getEnginePower() {
		return enginePower;
	}
	
	/**
	 * @param id the robot identification number
	 * @param position the start position
	 * @param user the robot belong to this user
	 * @param behaviourFactory to create behaviors
	 * @return the new generated Robot
	 */
	public Robot generateRobot (final long id, final Vector2D position, final User user, final BehaviourFactory behaviourFactory) {
		final Robot robot = new Robot(id, position, this, weaponPrototype.generateWeapon(), user,behaviourFactory, behaviour, logFacade);
		return robot;
	}
}
