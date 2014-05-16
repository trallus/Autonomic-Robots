package de.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The representation of a User
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 15.05.2014
 */
@Entity
@Table(name="UserAccount") //Because User is reserve in DerbyDB
public class DBUser {
    
    /**
     * The Id for the Persistence System, DO NOT TOUCH THIS!
     */
    @Id
    @GeneratedValue
    private long id;
    /**
     * Name of the User
     */
    private String name;
    /**
     * Email Address of the User
     */
    private String eMail;
    /**
     * Password hashed of the User
     */
    private String pwHash;
    /**
     * The start point of the user
     */
    @Transient
    private Point startPoint;
    
    /**
     * @param name The Name of the user
     * @param eMail The Email of the user
     * @param pwHash The hashed password of the user
     */
    public DBUser(String name, String eMail, String pwHash) {
	this.name = name;
	this.eMail = eMail;
	this.pwHash = pwHash;
    }
    
    /**
     * Default Consturctor for the persistence system
     */
    public DBUser(){
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the eMail
     */
    public String geteMail() {
        return eMail;
    }

    /**
     * @param eMail the eMail to set
     */
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    /**
     * @return the pwHash
     */
    public String getPwHash() {
        return pwHash;
    }

    /**
     * @param pwHash the pwHash to set
     */
    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    /**
     * @return the startPoint
     */
    public Point getStartPoint() {
        return startPoint;
    }

    /**
     * @param startPoint the startPoint to set
     */
    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((eMail == null) ? 0 : eMail.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((name == null) ? 0 : pwHash.hashCode());
	//result = prime * result + (int) (pwHash ^ (pwHash >>> 32));
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (!(obj instanceof DBUser))
	    return false;
	DBUser other = (DBUser) obj;
	if (eMail == null) {
	    if (other.eMail != null)
		return false;
	}
	else if (!eMail.equals(other.eMail))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	}
	else if (!name.equals(other.name))
	    return false;
	if (pwHash != other.pwHash)
	    return false;
	return true;
    }
    
    
}
