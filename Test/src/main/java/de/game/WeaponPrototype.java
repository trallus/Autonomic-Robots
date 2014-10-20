package de.game;

public class WeaponPrototype {
	final long range;
	final long rateOfFire;
	final long damage;
	
	public WeaponPrototype (final long range, final long rateOfFire, final long damage) {
		this.range = range;
		this.rateOfFire = rateOfFire;
		this.damage = damage;
	}

	public long getRange() {
		return range;
	}

	public long getRateOfFire() {
		return rateOfFire;
	}

	public long getDamage() {
		return damage;
	}
}
