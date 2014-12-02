package de.game.physicEngine;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import de.game.Matrix;

public class MatrixTest {
    @Test
    public void resultTest() {
    	final Matrix testMatrix = new Matrix(
		    /*
	        new Array(1,6,4,10),
	        new Array(2,5,3,20),
	        new Array(3,5,2,28)

	        1x + 6y + 4z = 10
	        2x + 5y + 3z = 20
	        3x + 5y + 2z = 28

	        10.8 -2 2.8

	        x = 10,8
	        y = -2
	        z = 2,8
	     */
		new Double[][] {
				{1.0, 6.0, 4.0, 10.0},
				{2.0, 5.0, 3.0, 20.0},
				{3.0, 5.0, 2.0, 28.0}
			}
		);
    	
    	final Double[] result = testMatrix.getResult();
    	final Double[] expectedResult = {10.8, -2.0, 2.8};
    	
    	for (int i=0; i < expectedResult.length; i++) {
        	assertEquals(expectedResult[i], result[i]);
    	}
    }
}
