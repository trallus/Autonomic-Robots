package de.httpServer.ClientClasses;

/**
 * Destination to convert a JSON User to a Java Object
 * @author Christian Köditz
 * @version 0.1
 * @since 02.12.2014
 */
public class ClientUser {
    /**
     * the name value from the JSON Robot
     */
    public final String name;
    /**
     * the password value from the JSON Robot
     */
    public final String password;
    /**
     * the e-mail value from the JSON Robot
     */
    public final String eMail;
    
    /**
     * @param name
     * @param eMail
     * @param password
     */
    public ClientUser(String name, String eMail, String password) {
	this.name = name;
	this.password = password;
	this.eMail = eMail;
    }
}
