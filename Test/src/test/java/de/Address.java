package de;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A Address represantation for a simple JPA techDemo
 * 
 * @author Mike Kiekebusch
 * @version 0.1
 */
@Entity
public class Address {
    @Id
    @GeneratedValue
    private long id;
    private String street;
    private int zip;

    /**
     * Simpel Field Constructor
     * 
     * @param street
     * @param zip
     */
    public Address(String street, int zip) {
	super();
	this.street = street;
	this.zip = zip;
    }
    
    public Address(){
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Address [street=" + street + ", zip=" + zip + "]";
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((street == null) ? 0 : street.hashCode());
	result = prime * result + zip;
	return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Address other = (Address) obj;
	if (street == null) {
	    if (other.street != null)
		return false;
	}
	else if (!street.equals(other.street))
	    return false;
	if (zip != other.zip)
	    return false;
	return true;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return the zip
     */
    public int getZip() {
        return zip;
    }

    /**
     * @param zip the zip to set
     */
    public void setZip(int zip) {
        this.zip = zip;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    
}
