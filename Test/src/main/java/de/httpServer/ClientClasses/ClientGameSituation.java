package de.httpServer.ClientClasses;

public class ClientGameSituation {
    public final long armor;
    public final long enginePower;
    public final String behaviour;
    public final long range;
    public final long damage;
    public final long rateOfFire;
    
    public ClientGameSituation(long armor, long enginePower, String behaviour, long range, long damage, long rateOfFire) {
		this.armor = armor;
		this.enginePower = enginePower;
		this.behaviour = behaviour;
		this.range = range;
		this.damage = damage;
		this.rateOfFire = rateOfFire;
    }
}
