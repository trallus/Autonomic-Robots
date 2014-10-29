package de.game.weapon;

import de.math.Vector2D;

public class WeaponPrototype extends Weapon {

    public WeaponPrototype(long range, long rateOfFire, long damage) {
	super(1,1,1); //TODO fixen und untere Zeile wieder herstellen
	//super((int) range, (int) rateOfFire,(int) damage);
	//Why is this long and not int like it is Declared in UML???
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

    @Override
    public void shoot(Vector2D targetPosition) {
	//Does nothing in the Prototype
    }
}
