package de.game;

import java.util.ArrayList;
import java.util.List;
import de.game.exceptions.NotInQueryException;
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
	public long joinBattleQuery(final User user) throws InterruptedException {
		if (battleQerry.size() > 0) { // two player
		//if (battleQerry.size() > 0 || true) {
			final List<User> users = new ArrayList<User>();
			users.add(user);
			for (final User u : battleQerry) {
				users.add(u);
			}
			for(final User u : users) {
				battleQerry.remove(u);
			}
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
			final Battle b = user.getBattle();
			if (b != null) {
				return b.getID();
			} else {
				return -1;
			}
		}
	}

	@Override
	public void leaveBattleQuery(final User user) throws NotInQueryException {
		battleQerry.remove(user);
	}

	@Override
	public void setNextRobot(final User user, final RobotPrototype rp) {
		user.setNextRobot(rp);
	}

	@Override
	public Battle getGameSituation(final User user) {
		return user.getBattle();
	}

	@Override
	public List<String> getBehaviours() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBehaviour(final long robotID, final String behaviour, final User user) {
		user.setBehavior(robotID, behaviour);
	}
}
