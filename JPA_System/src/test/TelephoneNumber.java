package test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A Simpel Number for JPA tech Demo
 * 
 * @author Mike Kiekebusch
 * @version 0.1
 */
@Entity
public class TelephoneNumber {
    @Id
    @GeneratedValue
    private long id;

    private int areaCode;
    private int number;

    /**
     * @param areaCode
     * @param number
     */
    public TelephoneNumber(int areaCode, int number) {
	super();
	this.areaCode = areaCode;
	this.number = number;
    }
    
    public TelephoneNumber(){
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "TelephoneNumber [id=" + id + ", areaCode=" + areaCode
		+ ", number=" + number + "]";
    }

    

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + areaCode;
	result = prime * result + number;
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
	if (!(obj instanceof TelephoneNumber))
	    return false;
	TelephoneNumber other = (TelephoneNumber) obj;
	if (areaCode != other.areaCode)
	    return false;
	if (number != other.number)
	    return false;
	return true;
    }

    /**
     * @return the areaCode
     */
    public int getAreaCode() {
        return areaCode;
    }

    /**
     * @param areaCode the areaCode to set
     */
    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    
}
