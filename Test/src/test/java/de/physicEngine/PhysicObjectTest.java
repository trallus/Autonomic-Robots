package de.physicEngine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.math.Vector2D;
import de.physicEngine.PhysikObject;

public class PhysicObjectTest {

    @Test
    public void constructorSetMoveVectorTest(){
    	final PhysikObject p1 = createPhysicObject(0, 0);
    	p1.setPosition(new Vector2D(0, 0));
    	p1.setMoveVector(new Vector2D(0, 0));
    	
    	PhysikObject p2;
		
    	p1.setMoveVector(new Vector2D(0, 1));
		p2 = createPhysicObject(Math.PI/4*0, 1);
		testEqualMoveVektor(p1, p2);
		
    	p1.setMoveVector(new Vector2D(1, 1));
		p2 = createPhysicObject(Math.PI/4*1, Math.sqrt(2));
		testEqualMoveVektor(p1, p2);
		
    	p1.setMoveVector(new Vector2D(1, 0));
		p2 = createPhysicObject(Math.PI/4*2, 1);
		testEqualMoveVektor(p1, p2);
		
    	p1.setMoveVector(new Vector2D(1, -1));
		p2 = createPhysicObject(Math.PI/4*3, Math.sqrt(2));
		testEqualMoveVektor(p1, p2);
		
    	p1.setMoveVector(new Vector2D(0, -1));
		p2 = createPhysicObject(Math.PI/4*4, 1);
		testEqualMoveVektor(p1, p2);
		
    	p1.setMoveVector(new Vector2D(-1, -1));
		p2 = createPhysicObject(Math.PI/4*5, Math.sqrt(2));
		testEqualMoveVektor(p1, p2);
		
    	p1.setMoveVector(new Vector2D(-1, 0));
		p2 = createPhysicObject(Math.PI/4*6, 1);
		testEqualMoveVektor(p1, p2);
		
    	p1.setMoveVector(new Vector2D(-1, 1));
		p2 = createPhysicObject(Math.PI/4*7, Math.sqrt(2));
		testEqualMoveVektor(p1, p2);
		
    	p1.setMoveVector(new Vector2D(0, 1));
		p2 = createPhysicObject(Math.PI/4*8, 1);
		testEqualMoveVektor(p1, p2);
    	
    }
    
    private void testEqualMoveVektor (final PhysikObject p1, final PhysikObject p2) {
    	final double delta = .00000000001;
    	
    	final Vector2D p1mv = p1.getMoveVector();
    	final Vector2D p2mv = p2.getMoveVector();

    	final double p1mvN1 = p1mv.getN1();
    	final double p1mvN2 = p1mv.getN2();
    	final double p2mvN1 = p2mv.getN1();
    	final double p2mvN2 = p2mv.getN2();
    	
		//System.out.println("p1:" + p1mv.toString());
		//System.out.println("p2:" + p2mv.toString());
    	
    	assertEquals(p2mvN1, p1mvN1, delta);
    	assertEquals(p2mvN2, p1mvN2, delta);
    }

    private PhysikObject createPhysicObject (final double direction, final double speed) {
    	
    	final PhysikObject p = new PhysikObject(direction, speed) {
			
			@Override
			public void onHit(PhysikObject po) {
				// TODO Auto-generated method stub
				
			}
		};
		
		p.setMass(1);
		p.setRadius(1);
		p.setPosition(new Vector2D(0, 0));
		
		return p;
    }

}
