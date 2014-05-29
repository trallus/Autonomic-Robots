package de.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The representation of a User
 * 
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 15.05.2014
 */
@Entity
@Table(name = "UserAccount")
// Because User is reserve in DerbyDB
public class DBUser {

    /**
     * The Id for the Persistence System, DO NOT TOUCH THIS!
     */
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String eMail;
    private String pwHash;

    /**
     * @param name
     *            The Name of the user
     * @param eMail
     *            The Email of the user
     * @param pwHash
     *            The hashed password of the user
     */
    public DBUser(String name, String eMail, String pwHash) {
	this.name = name;
	this.pwHash = pwHash;
	this.eMail = eMail;
    }

    /**
     * Default Consturctor for the persistence system
     */
    public DBUser() {
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the pwHash
     */
    public String getPassword() {
	return pwHash;
    }

    /**
     * @param pwHash
     *            the pwHash to set
     */
    public void setPassword(String password) {
	this.pwHash = password;
    }

    /**
     * @return the id
     */
    public long getId() {
	return id;
    }

    @Override
    public String toString() {
	String s = "name:" + name;
	s += ", password:" + pwHash;
	return s;
    }

	public Object getEMail() {
		return eMail;
	}

}
