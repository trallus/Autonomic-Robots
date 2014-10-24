package de.game;

import de.logger.Log;
import de.math.Vector2D;
import de.physicEngine.PhysikObject;

public class Robot extends PhysikObject implements Tick {
	private final long id;
	private final double turningSpeed;
	private final double enginePower;
	private final double armor;
	private final double acceleration;
	private boolean accelerate;
	private boolean turnLeft;
	
	public Robot (final long id, final Vector2D position, final RobotPrototype rb) {
		super(0, 0);	// start direction 0°, speed 0 pixel/sec
		this.accelerate = true;
		this.turnLeft = true;
		this.id = id;
		setPosition(position);
		this.enginePower = rb.getEnginePower();
		this.armor = rb.getArmor();
		this.turningSpeed = .1/armor*enginePower;
		this.acceleration = .1/armor*enginePower;
	}
	
	public long getID () {
		return id;
	}

	public void setBehaviour(String behaviour) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHit(PhysikObject po) {
		// TODO noch keine physik, nur ph abzug durch kollision
		final double power = po.getMoveVector().getMagnitude() * po.getMass();
		final double hp = getHitPoints() - power;
		if (hp < 0) {
			die();
		} else {
			setHitPoints(hp);
		}
	}
	
	public void turnLeft () {
		turnLeft = true;
	}
	
	public void turnRight () {
		turnLeft = false;
	}
	
	public void accelerate () {
		accelerate = true;
	}
	
	public void breack () {
		accelerate = false;
	}
	
	private void die() {
		Log.log("Robot die: " + id);
	}

	@Override
	public void onTick(Battle battle, double elapsedTime) {
		if (turnLeft) turn(turningSpeed * elapsedTime);
		else turn(-turningSpeed * elapsedTime);
		if (accelerate) accelerate(acceleration * elapsedTime);
		else accelerate(-acceleration * elapsedTime);
		move(elapsedTime);
	}
}
