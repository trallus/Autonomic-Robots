package de.math;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 07.10.2014
 */
public class Vector2DTest {

    private Vector2D vec1;
    private Vector2D vec2;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	vec1 = new Vector2D(2, 3);
	vec2 = new Vector2D(4, 5);
    }

    /**
     * Tests if the magnitude calculation is correct
     */
    @Test
    public void testMagnitude(){
	final double expected = Math.sqrt(13);
	assertEquals(expected, vec1.getMagnitude(), 0);
    }
    
    /**
     * Test method for {@link de.math.Vector2D#addition(de.math.Vector2D)}.
     */
    @Test
    public void testAddition() {
	final Vector2D expected = new Vector2D(6, 8);
	assertEquals(expected, vec1.addition(vec2));
    }

    /**
     * Test method for {@link de.math.Vector2D#substraction(de.math.Vector2D)}.
     */
    @Test
    public void testSubstraction1() {
	final Vector2D expected1 = new Vector2D(-2, -2);
	assertEquals(expected1, vec1.substraction(vec2));
    }

    /**
     * Test method for {@link de.math.Vector2D#substraction(de.math.Vector2D)}.
     */
    @Test
    public void testSubstraction2() {
	final Vector2D expected2 = new Vector2D(2, 2);
	assertEquals(expected2, vec2.substraction(vec1));
    }

    /**
     * Test method for {@link de.math.Vector2D#mul(double)}.
     */
    @Test
    public void testMul() {
	final Vector2D expected = new Vector2D(8, 12);
	assertEquals(expected, vec1.mul(4));
    }

    /**
     * Test method for {@link de.math.Vector2D#dot(de.math.Vector2D)}.
     */
    @Test
    public void testDot() {
	final double expected = 23;
	assertEquals(expected, vec1.dot(vec2),0);
    }

}
