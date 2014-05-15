package de;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * A Simple Person for a JPA TechDemo
 * 
 * @author Mike Kiekebusch
 * @version 0.1a
 */
@Entity
public class Person {
    /**
     * Used automatically from the JPA System, JUST LEAVE IT ALONE
     */
    @Id
    @GeneratedValue
    private long id;

    private String firstName;
    private String lastName;

    /**
     * One to One foreignKey Assoziation CascadeType.ALL := all operations on
     * this object will be done for the asociated
     */
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    /**
     * One to Many Assoziation with help tabel EAGER := loads the total list
     * with this object
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private List<TelephoneNumber> numbers;

    /**
     * Will not be put into the DataBase
     */
    @Transient
    private String lastWish;

    /**
     * @param firstName
     * @param lastName
     * @param address
     * @param numbers
     * @param lastWish
     */
    public Person(String firstName, String lastName, Address address,
	    List<TelephoneNumber> numbers, String lastWish) {
	super();
	this.firstName = firstName;
	this.lastName = lastName;
	this.address = address;
	this.numbers = numbers;
	this.lastWish = lastWish;
    }
    
    public Person(){
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Person [id=" + id + ", firstName=" + firstName + ", lastName="
		+ lastName + ", address=" + address + ", numbers=" + numbers
		+ ", lastWish=" + lastWish + "]";
    }

    

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((address == null) ? 0 : address.hashCode());
	result = prime * result
		+ ((firstName == null) ? 0 : firstName.hashCode());
	result = prime * result
		+ ((lastName == null) ? 0 : lastName.hashCode());
	result = prime * result + ((numbers == null) ? 0 : numbers.hashCode());
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
	if (!(obj instanceof Person))
	    return false;
	Person other = (Person) obj;
	if (address == null) {
	    if (other.address != null)
		return false;
	}
	else if (!address.equals(other.address))
	    return false;
	if (firstName == null) {
	    if (other.firstName != null)
		return false;
	}
	else if (!firstName.equals(other.firstName))
	    return false;
	if (lastName == null) {
	    if (other.lastName != null)
		return false;
	}
	else if (!lastName.equals(other.lastName))
	    return false;
	if (numbers == null) {
	    if (other.numbers != null)
		return false;
	}
	else if (!numbers.equals(other.numbers))
	    return false;
	return true;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @return the numbers
     */
    public List<TelephoneNumber> getNumbers() {
        return numbers;
    }

    /**
     * @param numbers the numbers to set
     */
    public void setNumbers(List<TelephoneNumber> numbers) {
        this.numbers = numbers;
    }

    /**
     * @return the lastWish
     */
    public String getLastWish() {
        return lastWish;
    }

    /**
     * @param lastWish the lastWish to set
     */
    public void setLastWish(String lastWish) {
        this.lastWish = lastWish;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }
    
    
}
