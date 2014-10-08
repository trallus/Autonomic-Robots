package de.math;

/**
 * A Simple 2D Vector
 * 
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 07.10.2014
 */
public class Vector2D {
    /**
     * First Element of the Vector
     */
    private final double n1;
    /**
     * Second Element of the Vector
     */
    private final double n2;
    /**
     * The magnitude of the Vector
     */
    private final double magnitude;

    /**
     * @param n1
     *            Used as the first Element of the Vector
     * @param n2
     *            Used as the second Element of the Vecotr
     */
    public Vector2D(final double n1, final double n2) {
	this.n1 = n1;
	this.n2 = n2;
	this.magnitude = Math.sqrt(n1 * n1 + n2 * n2);
    }

    /**
     * @return the n1
     */
    public double getN1() {
	return n1;
    }

    /**
     * @return the n2
     */
    public double getN2() {
	return n2;
    }

    /**
     * @return the magnitude
     */
    public double getMagnitude() {
	return magnitude;
    }

    /**
     * Calculates the sum of the this Vector with the give vector (this+v2)
     * 
     * @param v2
     *            The Vector that will be added to this one
     * @return The sum of both vectors
     */
    public Vector2D addition(final Vector2D v2) {
	return new Vector2D(n1 + v2.n1, n2 + v2.n2);
    }

    /**
     * Substractes the given Vector from this one (this-v2)
     * 
     * @param v2
     *            The Vector that will be substracte from this one
     * @return The Resulting Vector of the substraction
     */
    public Vector2D substraction(final Vector2D v2) {
	return new Vector2D(n1 - v2.n1, n2 - v2.n2);
    }

    /**
     * Multiplies this Vector with the given Number
     * 
     * @param number
     *            the Number with which this Vector will be multiplied
     * @return The Resulting Vector of this multiplication
     */
    public Vector2D mul(final double number) {
	return new Vector2D(n1 * number, n2 * number);
    }

    /**
     * Produces the scalar product between this and the given Vector
     * 
     * @param v2
     *            The 2nd Vector for the Scala Product
     * @return The resulting value
     */
    public double dot(final Vector2D v2) {
	return n1 * v2.n1 + n2 * v2.n2;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	long temp;
	temp = Double.doubleToLongBits(magnitude);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	temp = Double.doubleToLongBits(n1);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	temp = Double.doubleToLongBits(n2);
	result = prime * result + (int) (temp ^ (temp >>> 32));
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
	if (!(obj instanceof Vector2D))
	    return false;
	Vector2D other = (Vector2D) obj;
	if (Double.doubleToLongBits(magnitude) != Double
		.doubleToLongBits(other.magnitude))
	    return false;
	if (Double.doubleToLongBits(n1) != Double.doubleToLongBits(other.n1))
	    return false;
	if (Double.doubleToLongBits(n2) != Double.doubleToLongBits(other.n2))
	    return false;
	return true;
    }

}
