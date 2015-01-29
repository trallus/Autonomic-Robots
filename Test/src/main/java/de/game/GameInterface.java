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
	 * @param user which wants to enter battle
	 * @return return the ID of the Battle
	 * @throws InterruptedException when the query thread is interupted
	 */
	long joinBattleQuery (User user) throws InterruptedException;
	/**
	 * try to leave the beattle querry
	 * @param user which leaves the battle
	 * @throws NotInQueryException when invoked without being in a query
	 */
	void leaveBattleQuery (User user) throws NotInQueryException;
	/**
	 * set the next Robot for the running Battle
	 * @param user for which to set the robot configuration
	 * @param rp robotprototype for the next robot
	 */
	void setNextRobot ( User user, RobotPrototype rp );
	/**
	 * get a actual snapshot from the battlefald
	 * @param user to identify the battle
	 * @return the Batte from the User
	 */
	Battle getGameSituation ( User user );
	/**
	 * get a list of all possible robot behaviors
	 * @return the list of all behaviors
	 */
	String[] getBehaviours();
	/**
	 * set a behaviour to a exist robot
	 * @param robotID id of the robot
	 * @param behaviour name of the behaviour to set
	 * @param user to which the robot belongs
	 * @throws RobotNotFoundException when the robot is not found
	 * @throws BehaviorNotFoundException when the behaviour is not found
	 * @throws BattleNotFoundException when the battle is not found
	 */
	void setBehaviour(long robotID, String behaviour, User user) throws RobotNotFoundException, BehaviorNotFoundException, BattleNotFoundException;
}
