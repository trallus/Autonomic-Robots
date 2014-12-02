package de.game.physicEngine;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.math.Vector2D;
import de.physicEngine.PhysicEngineImpl;
import de.physicEngine.PhysikObject;

/**
 * @author Christian Köditz
 * @version 0.1
 * @since 07.10.2014
 */
public class PhysicEngineTest {

    private PhysicEngineImpl physicEngine;
    private ArrayList<PhysikObject> physicObjects;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    	physicObjects = new ArrayList<PhysikObject>();
    	physicEngine = new PhysicEngineImpl(physicObjects);
    }

    /**
     * Tests Kollision
     */
    @Test
    public void collisionTest(){
    	/*
    	 * Bewegtes auf stehendes Object
    	 * gleiche masse
    	 */
    	final double plast = .8;  // Plastizitätskonstante in der PhysicEngine
    	final Vector2D moveVector = new Vector2D(-1, 0);
    	final double mass = 1.0;
    	final double radius = 1.0;
    	
    	// definire die Kollisionsobjekte
    	final PhysikObject p1 = createPhysicObject(0, 0);
    	p1.setPosition(new Vector2D(0, 0));
    	p1.setRadius(radius);
    	p1.setMass(mass);
    	p1.setMoveVector(new Vector2D(0, 0));
    	physicObjects.add(p1);
    	
    	final PhysikObject p2 = createPhysicObject(0, 0);
    	p2.setPosition(new Vector2D(1, 0)); //kollidieren beim ersten tick()
    	p2.setRadius(radius);
    	p2.setMass(mass);
    	p2.setMoveVector(moveVector);
    	physicObjects.add(p2);

    	tick(); //kollision

    	testEqualVektor(p2.getMoveVector(), new Vector2D(0, 0)); // p2 sollte nach der kollision stehen
    	testEqualVektor(p1.getMoveVector(), moveVector.mul(plast)); // p1 sollte sich nach der kollision bewegen
    	
    }
    

    
    private void testEqualVektor (final Vector2D p1mv, final Vector2D p2mv) {
    	final double delta = .00000000001;

    	final double p1mvN1 = p1mv.getN1();
    	final double p1mvN2 = p1mv.getN2();
    	final double p2mvN1 = p2mv.getN1();
    	final double p2mvN2 = p2mv.getN2();
    	
		//System.out.println("p1:" + p1mv.toString());
		//System.out.println("p2:" + p2mv.toString());
    	
    	assertEquals(p2mvN1, p1mvN1, delta);
    	assertEquals(p2mvN2, p1mvN2, delta);
    }
    
    private void tick () {
    	final double elapsedTime = .1;
    	
    	for(final PhysikObject p : physicObjects) {
    		p.move(elapsedTime);
    	}
		physicEngine.onTick(null, elapsedTime);
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
