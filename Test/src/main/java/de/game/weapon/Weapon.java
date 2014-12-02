package de.game.weapon;

import de.math.Vector2D;

/**
 * The Base Class for every Weapon
 * @author mike
 * @version 0.1
 */
public abstract class Weapon {
    /**
     * Range of the Weapon
     * Must be greater zero
     */
    protected final int range;
    /**
     * Rate of fire of the Weapon
     * Must be greater zero
     */
    protected final int rateOfFire;
    /**
     * Damage that one Shoot from this Weapon will do
     * Not allowed to be zero
     */
    protected final int damage;
    /**
     * Initializes the Weapon with the given range, rateOfFire and damage
     * @param range of the weapon, >0
     * @param rateOfFire of this weapon, >0
     * @param damage, !=0
     */
    public Weapon(final int range, final int rateOfFire, final int damage) {
	if(range<=0){ //TODO reduce redundance
	    final CouldNotCreateWeaponException exception = new CouldNotCreateWeaponException("Weapon Range not allowed to be less than 1", false);
	    exception.putParameter("range", range);
	    exception.putParameter("rateOfFire", rateOfFire);
	    exception.putParameter("damage", damage);
	    throw exception;
	}
	if(rateOfFire<=0){
	    final CouldNotCreateWeaponException exception = new CouldNotCreateWeaponException("Weapon rateOfFire not allowed to be less than 1", false);
	    exception.putParameter("range", range);
	    exception.putParameter("rateOfFire", rateOfFire);
	    exception.putParameter("damage", damage);
	    throw exception;
	}
	if(damage==0){
	    final CouldNotCreateWeaponException exception = new CouldNotCreateWeaponException("Weapon Damage not allowed to be 0", false);
	    exception.putParameter("range", range);
	    exception.putParameter("rateOfFire", rateOfFire);
	    exception.putParameter("damage", damage);
	    throw exception;
	}
	this.range = range;
	this.rateOfFire = rateOfFire;
	this.damage = damage;
    }
    
    /**
     * Fires the Wepon with the given targetPosition
     * @param targetPosition the position at which the weapon aims it's shoots
     */
    public abstract void shoot(Vector2D targetPosition);
}
