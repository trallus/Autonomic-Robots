package de.physicEngine;

import de.math.Vector2D;

/**
 * The basic Class of all PhysikObjects
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 07.10.2014
 */
public abstract class PhysikObject {
	
	/**
	 * @param direction Richtung des Objektes in Rad
	 * @param speed Geschwindigkeit des Objektes
	 */
	public PhysikObject (final double direction, final double speed) {
		this.direction = direction;
		this.speed = speed;
		updateMoveVector();
	}
	
    /**
     * The actual position of the PhysikObject
     */
    private Vector2D position;
    /**
     * The current Movment-Vector of this PhysikObject
     */
    private Vector2D moveVector;
    /**
     * The Radius (Size) of this PhysikObject
     */
    private double radius = 10;
    /**
     * The Mass of this PhysikObject
     */
    private double mass;
    /**
     * The LifePoints of this PhysikObject
     */
    private double hitPoints;
    /**
     * The move direction
     */
    private double direction;
    /**
     * The speed of the object
     */
    private double speed;
    /**
     * Calculates the Effects that the given PhysikObject has on this one when hit
     * @param po The other PhysicObject
     */
    public abstract void onHit(final PhysikObject po);
    /**
     * @return the position
     */
    public Vector2D getPosition() {
        return position;
    }
    /**
     * @param position the position to set
     */
    public void setPosition(final Vector2D position) {
        this.position = position;
    }
    /**
     * @return the moveVector
     */
    public Vector2D getMoveVector() {
        return moveVector;
    }
    /**
     * @param moveVector the moveVector to set
     */
    public void setMoveVector(final Vector2D moveVector) {
        this.moveVector = moveVector;
        this.speed = moveVector.getMagnitude();
        
        final double pih = Math.PI/2;
        direction = Math.asin(moveVector.getN1());
        if (moveVector.getN1() > 0 && moveVector.getN2() < 0) {
        	this.direction = Math.PI - direction;
        } else if (moveVector.getN1() <= 0 && moveVector.getN2() < 0) {
        	this.direction = - Math.PI - direction;
        }
    }
    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }
    /**
     * @param radius the radius to set
     */
    public void setRadius(final double radius) {
        this.radius = radius;
    }
    /**
     * @return the mass
     */
    public double getMass() {
        return mass;
    }
    /**
     * @param mass the mass to set
     */
    public void setMass(final double mass) {
        this.mass = mass;
    }
    /**
     * @return the hitPoints
     */
    public double getHitPoints() {
        return hitPoints;
    }
    /**
     * @param hitPoints the hitPoints to set
     */
    public void setHitPoints(final double hitPoints) {
        this.hitPoints = hitPoints;
    }
    /**
     * @return return the direction of the object
     */
    public double getDirection() {
        return direction;
    }
    /**
     * turn the object with this angle
     * @param angle
     */
    public void turn(final double angle) {
        direction += angle;
        updateMoveVector();
    }
    /**
     * accelerate with this speed
     * @param speed
     */
    public void accelerate(double speed) {
    	
    	if (speed < 0) speed *= 2;
    	
        this.speed += speed;
    	if (this.speed <= 0) {
    		this.speed = 0;
    	}
        updateMoveVector();
    }
	/**
	 * Move the physical element
	 * @param elapsedTime Time since last call
	 */
	public void move(final double elapsedTime) {
		position = position.addition(moveVector.mul(elapsedTime));
	}
    
	/**
	 * update the moveVector with direction and speed
	 */
	private void updateMoveVector () {
		moveVector = new Vector2D(speed*Math.sin(direction), speed*Math.cos(direction));
	}
	@Override
	public String toString() {
		return "PhysikObject [position=" + position + ", moveVector="
				+ moveVector + ", radius=" + radius + ", mass=" + mass
				+ ", hitPoints=" + hitPoints + ", direction=" + direction
				+ ", speed=" + speed + "]";
	}
}
