package de.game.weapon;

/**
 * @author ko
 * holds all values to generate a new wapon
 */
public class WeaponPrototype {

	/**
	 * the range of the weapon
	 */
	private final int range;
	/**
	 * the fire rate
	 */
	private final int rateOfFire;
	/**
	 * the damage per shot
	 */
	private final int damage;
	
    /**
     * @param range the range of the weapon
     * @param rateOfFire the fire rate
     * @param damage the damage per shot
     */
    public WeaponPrototype(long range, long rateOfFire, long damage) {
    	this.range =(int)  range;
    	this.rateOfFire = (int) rateOfFire;
    	this.damage = (int) damage;
    }

    /**
     * @return range the range of the weapon
     */
    public int getRange() {
	return range;
    }

    /**
     * @return rateOfFire the fire rate
     */
    public int getRateOfFire() {
	return rateOfFire;
    }

    /**
     * @return damage the damage per shot
     */
    public int getDamage() {
	return damage;
    }

    /**
     * @return a new Weapon with this values
     */
    public Weapon generateWeapon () {
    	return new WeaponLaser(range, rateOfFire, damage);
    }
}
