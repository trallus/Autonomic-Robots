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
			battleID ++;
			if (battleID < 0) {
				battleID = 0;
			}
			return battleID;
		} else {
			battleQerry.add(user);
			// entweder eröffnet ein anderer user das spiel oder dieser user ruft leaveBattleQuery auf
			while (battleQerry.indexOf(user) != -1) {
				Thread.sleep(100);
			}
			// suche das battle in welchem user ist
			for (Battle b : battles) {
				final List<User> ul = b.getUsers();
				for (User u : ul) {
					if (u.getDBUser().getId() == user.getDBUser().getId()) {
						return b.getID();
					}
				}
			}
			return -1; // user hat leaveBattleQuery aufgerufen
		}
	}

	@Override
	public void leaveBattleQuery(User user) throws NotInQueryException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNextRobot(User user, Robot robot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Battle getGameSituation(long battleID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getBehaviours() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBehaviour(long robotID, String behaviour, long battleID)
			throws RobotNotFoundException, BehaviorNotFoundException,
			BattleNotFoundException {
		// TODO Auto-generated method stub
		
	}
}
