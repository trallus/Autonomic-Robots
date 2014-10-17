package de.game;

import java.util.ArrayList;
import java.util.List;

import de.game.exceptions.BattleNotFoundException;
import de.game.exceptions.BehaviorNotFoundException;
import de.game.exceptions.NotInQueryException;
import de.game.exceptions.RobotNotFoundException;
import de.httpServer.User;

/**
 * Controls the Game
 * @author ko
 *
 */
public class GameControler implements GameInterface {
	
	private final List<User> battleQerry;
	private final List<Battle> battles;
	private long battleID;
	
	public GameControler ( ) throws Exception{
		battleQerry = new ArrayList<User>();
		battles = new ArrayList<Battle>();
		battleID = 0;
	}

	@Override
	public long joinBattleQuery(User user) throws InterruptedException {
		if (battleQerry.size() > 0) {
			final List<User> users = new ArrayList<User>();
			users.add(battleQerry.get(0));
			battleQerry.remove(0);
			final Battle battle = new Battle( battleID, users );
			battles.add(battle);
			battle.start();	// start battle
			battleID ++;
			if (battleID < 0) { // max float
				battleID = 0;
			}
			return battleID;
		} else {
			battleQerry.add(user);
			// entweder eröffnet ein anderer user das spiel oder dieser user ruft leaveBattleQuery auf
			while (battleQerry.indexOf(user) != -1) {
				Thread.sleep(100);
			}
			final Battle b = getBattle(user);
			if (b != null) {
				return b.getID();
			} else {
				return -1;
			}
		}
	}

	@Override
	public void leaveBattleQuery(User user) throws NotInQueryException {
		battleQerry.remove(user);
	}

	@Override
	public void setNextRobot(User user, Robot robot) {
		final Battle battle = getBattle(user);
		battle.setNextRobot(robot, user);
	}

	@Override
	public Battle getGameSituation(long battleID) {
		final Battle battle = getBattle(battleID);
		return battle;
	}

	@Override
	public List<String> getBehaviours() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBehaviour(long robotID, String behaviour, long battleID) {
		final Battle battle = getBattle(battleID);
		battle.setBehaviour(robotID, behaviour);
	}
	
	private Battle getBattle (final User user) {
		
		for (Battle b : battles) {
			final List<User> ul = b.getUsers();
			for (User u : ul) {
				if (u.getDBUser().getId() == user.getDBUser().getId()) {
					return b;
				}
			}
		}
		
		return null;
	}
	
	private Battle getBattle (final long id) {
		
		for (Battle b : battles) {
			if (b.getID() == id) return b;
		}
		
		return null;
	}
}
