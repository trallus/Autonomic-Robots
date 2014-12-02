package de.httpServer.ClientClasses;

/**
 * Destination to convert a JSON Robot to a Java Object
 * @author Christian Köditz
 * @version 0.1
 * @since 02.12.2014
 */
public class ClientNextRobot {
    /**
     * the armor value from the JSON Robot
     */
    public final long armor;
    /**
     * the engine power value from the JSON Robot
     */
    public final long enginePower;
    /**
     * the behavior value from the JSON Robot
     */
    public final String behaviour;
    /**
     * the range value from the JSON Robot
     */
    public final long range;
    /**
     * the damage value from the JSON Robot
     */
    public final long damage;
    /**
     * the rate of fire value from the JSON Robot
     */
    public final long rateOfFire;
    
    /**
     * @param armor
     * @param enginePower
     * @param behaviour
     * @param range
     * @param damage
     * @param rateOfFire
     */
    public ClientNextRobot(long armor, long enginePower, String behaviour, long range, long damage, long rateOfFire) {
		this.armor = armor;
		this.enginePower = enginePower;
		this.behaviour = behaviour;
		this.range = range;
		this.damage = damage;
		this.rateOfFire = rateOfFire;
    }
}
