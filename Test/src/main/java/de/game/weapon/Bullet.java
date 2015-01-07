package de.game.weapon;

import de.game.Robot;
import de.logger.LogLevel;
import de.logger.LoggerIF;
import de.math.Vector2D;
import de.physicEngine.PhysikObject;

/**
 * A standard Bullet that can only hit robots and which dealt damage is reduced
 * by the armor of the hit robot
 * 
 * @author mike
 * @version 0.1
 */
public class Bullet extends PhysikObject {

    /**
     * The base damage this bullet will deal onhit
     */
    final double damage;
    /**
     * The logging system
     */
    final LoggerIF log;

    /**
     * @param direction
     *            The direction in which the bullet is moving
     * @param speed
     *            The Speed with which the bullet is moving
     * @param damage
     *            The Damage the Bullet will deal onHit
     * @param log
     *            The interface to the logging system
     * @param startPosition The position at which this bullet starts
     */
    public Bullet(final double direction, final double speed,
	    final double damage, final LoggerIF log, final Vector2D startPosition) {
	super(direction, speed);
	this.damage = damage;
	this.log = log;
	this.setHitPoints(1);
	this.setPosition(startPosition);
    }

    @Override
    public void onHit(PhysikObject po) {
	if (po instanceof Robot) {
	    final Robot robot = (Robot) po;
	    final double damage = this.damage - robot.getArmor();
	    final double hp = robot.getHitPoints() - damage;
	    robot.setHitPoints(hp);
	    this.setHitPoints(0);
	    log.log("Bullet hit Robot"+robot.getID()+" for "+damage+" Damage", this.getClass().getName(), LogLevel.DEBUG);
	}
	else {
	    return; // Bullets can't hit each other
	}
    }

    /**
     * Overriden because a standard bullet can not change it's MoveVector
     */
    @Override
    public void setMoveVector(final Vector2D moveVector) {
	log.log("Call of unsupported Operation setMoveVector", this.getClass()
		.getName() + ".setMoveVector", LogLevel.DEBUG);
    }

}
