package de.game.weapon;

public class WeaponPrototype {

	private final int range;
	private final int rateOfFire;
	private final int damage;
	
    public WeaponPrototype(long range, long rateOfFire, long damage) {
    	this.range =(int)  range;
    	this.rateOfFire = (int) rateOfFire;
    	this.damage = (int) damage;
    }

    public int getRange() {
	return range;
    }

    public int getRateOfFire() {
	return rateOfFire;
    }

    public int getDamage() {
	return damage;
    }

    public Weapon generateWeapon () {
    	return new WeaponLaser(range, rateOfFire, damage);
    }
}
