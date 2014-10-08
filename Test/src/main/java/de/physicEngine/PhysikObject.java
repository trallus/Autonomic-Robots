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
    private double radius;
    /**
     * The Mass of this PhysikObject
     */
    private double mass;
    /**
     * The LifePoints of this PhysikObject
     */
    private double hitPoints;
    /**
     * Calculates the Effects that the given PhysikObject has on this one when hit
     * @param po The other PhysicObject
     */
    public abstract void onHit(PhysikObject po);
    /**
     * @return the position
     */
    public Vector2D getPosition() {
        return position;
    }
    /**
     * @param position the position to set
     */
    public void setPosition(Vector2D position) {
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
    public void setMoveVector(Vector2D moveVector) {
        this.moveVector = moveVector;
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
    public void setRadius(double radius) {
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
    public void setMass(double mass) {
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
    public void setHitPoints(double hitPoints) {
        this.hitPoints = hitPoints;
    }
    
}
