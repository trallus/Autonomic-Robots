package de.game;

import java.util.List;

import de.game.exceptions.BattleNotFoundException;
import de.game.exceptions.BehaviorNotFoundException;
import de.game.exceptions.NotInQueryException;
import de.game.exceptions.RobotNotFoundException;
import de.httpServer.User;

/**
 * @author Christian Köditz
 * @version 0.1
 * @since 15.10.2014
 */
public interface GameInterface {
	/**
	 * try to enter a Battle
	 * @param user
	 * @return
	 * @throws InterruptedException 
	 */
	long joinBattleQuery (User user) throws InterruptedException;
	/**
	 * try to leave the beattle querry
	 * @param user
	 */
	void leaveBattleQuery (User user) throws NotInQueryException;
	/**
	 * set the next Robot for the running Battle
	 * @param user
	 * @param rp
	 */
	void setNextRobot ( User user, RobotPrototype rp );
	/**
	 * get a actual snapshot from the battlefald
	 * @param user
	 * @return
	 */
	Battle getGameSituation ( User user );
	/**
	 * get a list of all possible robot behaviours
	 * @return
	 */
	String[] getBehaviours();
	/**
	 * set a behaviour to a exist robot
	 * @param robotID
	 * @param behaviour
	 * @param user
	 */
	void setBehaviour(long robotID, String behaviour, User user) throws RobotNotFoundException, BehaviorNotFoundException, BattleNotFoundException;
}
