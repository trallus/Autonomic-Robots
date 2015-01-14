package de.game.weapon;

import de.game.Battle;
import de.game.Robot;
import de.math.Vector2D;

public class WeaponLaser extends Weapon {
	
	private long nextPosibleShotTime;

	public WeaponLaser(int range, int rateOfFire, int damage) {
		super(range, rateOfFire, damage);
		nextPosibleShotTime = 0;
	}

	public void laserShoot(final Robot shooter, final Robot target, final Battle battle) {
		// check rate of fire
		final long actualTime = System.currentTimeMillis();
		if (nextPosibleShotTime <= actualTime) {
			//real shot
			nextPosibleShotTime = actualTime + (60 / rateOfFire) * 1000;
			battle.addLaserShot(shooter, target);
			target.laserHit(damage);
		}
	}

	@Override
	public void shoot(Vector2D targetPosition, Battle battle,
			double elapsedTime, Vector2D startingPosition) {
		// TODO Auto-generated method stub
		
	}
}
