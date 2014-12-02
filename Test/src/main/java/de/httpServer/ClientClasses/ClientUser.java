package de.httpServer.ClientClasses;

public class ClientUser {
    public final String name;
    public final String password;
    public final String eMail;
    
    public ClientUser(String name, String eMail, String password) {
	this.name = name;
	this.password = password;
	this.eMail = eMail;
    }
}
