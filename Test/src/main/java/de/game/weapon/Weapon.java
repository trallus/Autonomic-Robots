package de.game.weapon;

import de.game.Battle;
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
     * @param range of the weapon, greater zero
     * @param rateOfFire of this weapon, greater zero
     * @param damage not equal zero
     */
    public Weapon(final int range, final int rateOfFire, final int damage) {
	if(range<=0 || damage==0 || rateOfFire<=0){
	    final CouldNotCreateWeaponException exception = new CouldNotCreateWeaponException("One or more parameters out of boundary", false);
	    exception.putParameter("range", range);
	    exception.putParameter("rateOfFire", rateOfFire);
	    exception.putParameter("damage", damage);
	    throw exception;
	}
	this.range = range;
	this.rateOfFire = rateOfFire;
	this.damage = damage;
    }
    
    public int getRange() {
    	return range;
    }
    
    /**
     * Fires the Wepon with the given targetPosition
     * @param targetPosition the position at which the weapon aims it's shoots
     * @param battle the battle to which the shoot of the weapon will be added
     * @param elapsedTime The elapsed time since the last call of this methode
     * @param startingPosition The position from which the weapon is fired
     */
    public abstract void shoot(final Vector2D targetPosition, final Battle battle, double elapsedTime, Vector2D startingPosition);
}
