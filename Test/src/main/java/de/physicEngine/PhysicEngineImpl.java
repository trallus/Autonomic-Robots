package de.physicEngine;

import java.util.List;

import de.game.Battle;
import de.game.Tick;
import de.math.Matrix;
import de.math.Vector2D;

public class PhysicEngineImpl implements Tick{
	
	/**
	 * alle bekannten PhysicObject`s
	 */
	private final List<PhysikObject> physicObjects;
	
	/**
	 * @param physicObjects referenz auf die Liste der bekannten physicObject`s
	 */
	public PhysicEngineImpl (final List<PhysikObject> physicObjects) {
		this.physicObjects = physicObjects;
	}

	/* (non-Javadoc)
	 * @see de.game.Tick#onTick(de.game.Battle, double)
	 */
	@Override
	public void onTick(final Battle battle, final double elapsedTime) {
		for (final PhysikObject p1 : physicObjects) {
			int i = physicObjects.indexOf(p1) + 1;
			for ( ; i<physicObjects.size(); i++ ) {
				final PhysikObject p2 = physicObjects.get(i);
				final double distance = checkCollision(p1, p2);
				if (distance >= 0) {
					handleCollision(p1, p2, elapsedTime, distance);
				}
			}
		}
	}
	
	/**
	 * Check the distance between tow physic Objects
	 * @param p1
	 * @param p2
	 * @return the distance between the Objects or -1 if no collision
	 */
	private double checkCollision (final PhysikObject p1, final PhysikObject p2) {
		final double minDistance = p1.getRadius() + p2.getRadius();
		final double distance = calculateDistance(p1.getPosition(), p2.getPosition());
		if (distance < minDistance) return distance;
		return -1;
	}
	
	/**
	 * Verarbeitet die kollision zwischen zwei Objekten
	 * @param p1
	 * @param p2
	 * @param elapsedTime vergangene Zeit seit der letzten betrachtung der physikalischen Situation
	 * @param distance entfernung zwischen den Mittelpunkten der Objekte
	 */
	private void handleCollision (final PhysikObject p1, final PhysikObject p2, final double elapsedTime, final double distance) {
		final Vector2D mV1 = p1.getMoveVector();
		final Vector2D mV2 = p2.getMoveVector();
		
		final Vector2D pV1 = p1.getPosition();
		final Vector2D pV2 = p2.getPosition();
		
		// came the objects closer to each other?
		// if not, do nothing
		// Which distance have the PhysikObject in the (close) future (.01 sec)
		final double closeFuture = 0.01;
		final Vector2D p1FuturePos = pV1.addition(mV1.mul(closeFuture));
		final Vector2D p2FuturePos = pV2.addition(mV2.mul(closeFuture));
		final double futureDistance = calculateDistance(p1FuturePos, p2FuturePos);
		if (futureDistance > distance) return;
		
		final Vector2D kV = pV2.substraction(pV1); //Stoßvektor
		final Vector2D tV = new Vector2D(kV.getN2(), -kV.getN1()); //Tangente (kV + 90°)

		// Trenen der Bewegungsvektoren in eine Matrix aus zwei Vektoren.
		// Entlang der Kollisionsrichtung und entlang der Tangente
		Vector2D[] vm1 = sliceVectors(kV, tV, mV1);
		Vector2D[] vm2 = sliceVectors(kV, tV, mV2);
		
		// berücksichtigung der Masse
        // Achtung: hier noch nur Kollisionsanteile (vmX[0])
		final Double m1 = p1.getMass();
		final Double m2 = p2.getMass();
		
        final Double sp1 = 2 * (m1*vm1[0].getN1()+m2*vm2[0].getN1()) / (m1+m2); // 2 * geschwindigkeit des gemeinsamen schwerpunktes
        final Double sp2 = 2 * (m1*vm1[0].getN2()+m2*vm2[0].getN2()) / (m1+m2); // 2 * geschwindigkeit des gemeinsamen schwerpunktes
        
        // berücksichtigung der masse bei den kollisionsvektoren
        vm1[0] = new Vector2D(sp1 - vm1[0].getN1(), sp2 - vm1[0].getN2());
        vm2[0] = new Vector2D(sp1 - vm2[0].getN1(), sp2 - vm2[0].getN2());
        
        // berücksichtigung der plastizität bei den kollisionsvektoren
        vm1[0] = addPlast(vm1[0]);
        vm2[0] = addPlast(vm2[0]);
        
        // addieren der aufgeteilten Bewegungsvektoren zu einem Vektor
        final Vector2D newMoveVector1 = new Vector2D (
        		vm1[0].getN1()+vm1[1].getN1(),
        		vm1[0].getN2()+vm1[1].getN2()
        	);
        final Vector2D newMoveVector2 = new Vector2D (
        		vm2[0].getN1()+vm2[1].getN1(),
        		vm2[0].getN2()+vm2[1].getN2()
        	);
        
        p1.setMoveVector(newMoveVector1);
        p2.setMoveVector(newMoveVector2);
        
        // schiebe entlang des kolisionsvectors ein stück auseinander
        p1.setPosition(pV1.substraction(kV.mul(elapsedTime)));
        p2.setPosition(pV1.addition(kV.mul(elapsedTime)));
	}
	
	/**
	 * Verkürzte einen Vektor nach einer Plastizitätskonstande
	 * @param plast
	 * @param v
	 * @return den verkürzten Vektor
	 */
	private Vector2D addPlast(final Vector2D v) {
        final double plast = .8;
		return new Vector2D(v.getN1()*plast, v.getN2()*plast);
	}

	/**
	 * Stellt den Vektor mV mit den zwei Vektoren tV und kV da
	 * @param kV
	 * @param tV
	 * @param mV
	 * @return ein Vektor Array mit den zwei neuen Vektoren
	 */
	private Vector2D[] sliceVectors (final Vector2D kV, final Vector2D tV, final Vector2D mV) {
		final Matrix matrix = new Matrix(
			new Double[][] {
					{kV.getN1(), tV.getN1(), mV.getN1()},
					{kV.getN2(), tV.getN2(), mV.getN2()}
				}
			);
		final Double[] ab = matrix.getResult();
		
		final Vector2D[] mVm = new Vector2D[] {
        		new Vector2D(kV.getN1()*ab[0], kV.getN2()*ab[0]),
        		new Vector2D(tV.getN1()*ab[1], tV.getN2()*ab[1])
        };
		
		return mVm;
	}
	
	/**
	 * Calculate the distance between the end points of two Vector2D
	 * @param v1
	 * @param v2
	 * @return the distance
	 */
	private double calculateDistance (final Vector2D v1, final Vector2D v2) {
		final double distanceX = v1.getN1() - v2.getN1();
		final double distanceY = v1.getN2() - v2.getN2();
		final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
		return distance;
	}
}
