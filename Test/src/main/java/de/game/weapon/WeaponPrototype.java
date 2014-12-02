package de.game.weapon;

import de.math.Vector2D;

/**
 * Repräsentiert den Prototypen einer Waffe
 * @author ko
 *
 */
public class WeaponPrototype extends Weapon {

    /**
     * @param range reichweite der Waffe
     * @param rateOfFire Feuerrate der Waffe pro Minute
     * @param damage Schaden der waffe pro Schuss
     */
    public WeaponPrototype(long range, long rateOfFire, long damage) {
	super(1,1,1); //TODO fixen und untere Zeile wieder herstellen
	//super((int) range, (int) rateOfFire,(int) damage);
	//Why is this long and not int like it is Declared in UML???
    }

    /**
     * @return die Reichweite
     */
    public int getRange() {
	return range;
    }

    /**
     * @return die Feuerrate
     */
    public int getRateOfFire() {
	return rateOfFire;
    }

    /**
     * @return den Schaden
     */
    public int getDamage() {
	return damage;
    }

    /* (non-Javadoc)
     * @see de.game.weapon.Weapon#shoot(de.math.Vector2D)
     */
    @Override
    public void shoot(Vector2D targetPosition) {
	//Does nothing in the Prototype
    }
}
