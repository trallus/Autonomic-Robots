package de.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The entity that represent a single user.
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
    /**
     * The nickname of the user
     */
    private String name;
    /**
     * The email address of the user
     */
    private String eMail;
    /**
     * The hased password of the user
     */
    private String pwHash;
    /**
     * The statistic of the user
     */
    @OneToOne(cascade = CascadeType.ALL)
    private Statistic statistic;

    /**
     * Initializes the entity with the given name, email and hashed password.
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
	statistic = new Statistic();
    }

    /**
     * Default Consturctor for the persistence system
     */
    public DBUser() {
    }

    /**
     * Getter for the name of the user
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Setter for the name of the user
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Getter for the hashed password of the user
     * @return the pwHash
     */
    public String getPassword() {
	return pwHash;
    }

    /**
     * Setter for the hashed password of the user
     * @param pwHash
     *            the pwHash to set
     */
    public void setPassword(String pwHash) {
	this.pwHash = pwHash;
    }

    /**
     * Getter for the primary key (id) of this user in the database
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

    /**
     * Getter for the email of the user
     * @return the email address of this user
     */
    public String getEMail() {
	return eMail;
    }

    /**
     * Setter for the email of the user
     * @param eMail the email address that will be set for the user
     */
    public void setEMail(String eMail) {
	this.eMail = eMail;
    }

    /**
     * Getter for the statistic of this user
     * @return the statistic of this user
     */
    public Statistic getStatistic(){
	return statistic;
    }
}
