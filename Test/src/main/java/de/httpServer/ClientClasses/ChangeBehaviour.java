package de.httpServer.ClientClasses;

public class ChangeBehaviour {
	public final long robotID;
	public final String behaviour;

	public ChangeBehaviour (final long robotID, final String behaviour) {
		this.behaviour = behaviour;
		this.robotID = robotID;
	}
}
