package de.httpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import de.game.Battle;
import de.game.GameInterface;
import de.game.Robot;
import de.game.RobotPrototype;
import de.game.exceptions.NotInQueryException;
import de.game.weapon.LaserShot;
import de.game.weapon.WeaponPrototype;
import de.httpServer.ClientClasses.ChangeBehaviour;
import de.httpServer.ClientClasses.ClientNextRobot;
import de.httpServer.ClientClasses.ClientUser;
import de.logger.LogLevel;
import de.logger.LoggerAndExceptionHandlerFacadeIF;
import de.math.Vector2D;

/**
 * Handle requests to the server
 * 
 * @author ko
 *
 */
public class ServerRequest extends Request {
	/**
	 * http protokol data from client
	 */
	private final HttpExchange httpExchange;
	/**
	 * type of encoding
	 */
	private final String encoding = "UTF-8";
	/**
	 * json builder to build send json
	 */
	private final Gson gsonOut = new GsonBuilder()
			.excludeFieldsWithoutExposeAnnotation().create();
	/**
	 * builder to build java objects from json
	 */
	private final Gson gsonIn = new Gson();
	/**
	 * object will be send to client as a json
	 */
	private final Map<String, Object> replyJson = new HashMap<String, Object>();
	/**
	 * the user for this request
	 */
	private final User user;
	
	private final LoggerAndExceptionHandlerFacadeIF logFacade;

	/**
	 * handle the request
	 * 
	 * @param httpExchange the underlying http request
	 * @param userManager instance of the usermanager to handle user creation and login
	 * @param gameInterface instance of the gameinterface for game specific communication
	 * @param logFacade the LoggerAndExceptionHandlerFacade that is used to get the Logger
	 * @throws Exception invoked exceptions are thrown
	 */
	public ServerRequest(HttpExchange httpExchange, UserManager userManager, GameInterface gameInterface, final LoggerAndExceptionHandlerFacadeIF logFacade)
			throws Exception {

	    	this.logFacade = logFacade;
	    
		mediaType = "application/json";

		this.httpExchange = httpExchange;

		final String SID = getSessionID(httpExchange);
		user = userManager.getUser(null, SID);
		// was a new user created?
		if (SID == null || !SID.equals(user.sessionID)) {
			logFacade.getLoggerInstance().log("Send new SID", this.getClass().getName(), LogLevel.DEBUG);
			sendNewSID(user.sessionID);
		}

		final String uri = httpExchange.getRequestURI().toString();
		try{
			handleURICommand(uri, userManager, gameInterface);
		}
		catch(Throwable arg){
			logFacade.getExceptionHandlerInstance().handle(arg, replyJson);
		}

		replyJson.put("logedIn", user.isLogedIn());

		// convert serverReply to sendable content
		String json = gsonOut.toJson(replyJson);
		content = json.getBytes();
	}

	/**
	 * execute what the uri expacted
	 * 
	 * @param uri
	 * @param userManager
	 * @param gameInterface 
	 * @throws Exception
	 */
	private void handleURICommand(String uri, UserManager userManager, GameInterface gameInterface) throws Exception {

		if (uri.indexOf("game") != -1) {
			handleGameCommands(uri, user, gameInterface);
		} else if (uri.indexOf("registration") != -1) {
			final String registedKey = "registered";
			replyJson.put(registedKey, false);
			ClientUser clientUser = readClientUser();
			userManager.register(null, clientUser.name, clientUser.eMail, clientUser.password, user);
			replyJson.put(registedKey, true);
		} else if (uri.indexOf("logIn") != -1) {
			ClientUser clientUser = readClientUser();
			userManager.logIn(null, clientUser.eMail, clientUser.password, user);
			replyJson.put("username", user.getDBUser().getName());
			replyJson.put("email", user.getDBUser().getEMail());
		} else if (uri.indexOf("logOut") != -1) {
			userManager.logOut(null, user);
		} else if (uri.indexOf("remove") != -1) {
			// just work with a new login -> save
			final ClientUser clientUser = readClientUser();
			final String removeKey = "removed";
			replyJson.put(removeKey, false);
			userManager.removeUser(null, clientUser.eMail, clientUser.password, user);
			replyJson.put(removeKey, true);
		} else if (uri.indexOf("searchUser") != -1) {
			final ClientUser clientUser = readClientUser();
			final String[] searchResule = userManager.searchUser(null, clientUser.name);
			replyJson.put("searchResult", searchResule);
		} else if (uri.indexOf("changeUser") != -1) {
			final String userChangeKey = "userChanged";
			replyJson.put(userChangeKey, false);
			ClientUser clientUser = readClientUser();
			userManager.changeUser(null, user, clientUser.name, clientUser.eMail, clientUser.password);
			replyJson.put(userChangeKey, true);
		} else  {
			replyJson.put("Unexpectrd URI", uri);
		}

	}

	private void handleGameCommands(String uri, User user,
			GameInterface gameInterface) throws InterruptedException, NotInQueryException, IOException {
		if (uri.indexOf("getGameSituation") != -1) {
			final Map<String, Object> gameSituation = new HashMap<String, Object>();
			final Battle b = user.getBattle();
			final List<User> ul = b.getUsers();
			for(User u : ul) {
				// add Robots
				final String un = u.getDBUser().getName();
				final ArrayList<Map<String, Object>> robotList = new ArrayList<Map<String, Object>>();
				gameSituation.put(un, robotList);
				final List<Robot> rl = u.getBattleRobots();
				final List<LaserShot> shotMap = b.getShotMap();
				for (Robot r : rl) {
					final Map<String, Object> robot = new HashMap<String, Object>();
					final Vector2D position = r.getPosition();
					final double[] posLong = {position.getN1(), position.getN2()};
					robot.put("position", posLong);
					robot.put("id", r.getID());
					robot.put("hitPoints", r.getHitPoints());
					for(final LaserShot ls : shotMap) {
						if (ls.shooter != r) continue;
						if (ls.haveSeenTheShot.contains(user)) continue;
						ls.haveSeenTheShot.add(user);
						robot.put("shotTarget", ls.target.getID());
					}
					robotList.add(robot);
				}
			}
			replyJson.put("gameSituation", gameSituation);
		} else if (uri.indexOf("setNextRobot") != -1) {
			final String jsonString = readInputStream();
			final ClientNextRobot cnr = gsonIn.fromJson(jsonString, ClientNextRobot.class);
			//balangcing
			final long baseVal = 50;
			final long multip = 10;
			final WeaponPrototype wp = new WeaponPrototype(
					balance(cnr.range, baseVal, multip),
					balance(cnr.rateOfFire, 10, 1),
					balance(cnr.damage, 10, 1));
			final RobotPrototype rp = new RobotPrototype(wp,
					balance(cnr.armor, baseVal, multip),
					balance(cnr.enginePower, baseVal, multip),
					cnr.behaviour,
					logFacade);
			user.setNextRobot(rp);
		} else if (uri.indexOf("getBehaviours") != -1) {
			replyJson.put("behaviours", gameInterface.getBehaviours());
		} else if (uri.indexOf("leaveBattleQuery") != -1) {
			gameInterface.leaveBattleQuery(user);
		} else if (uri.indexOf("joinBattleQuery") != -1) {
			gameInterface.joinBattleQuery(user);
		} else if (uri.indexOf("setBehaviour") != -1) {
 			final String jsonString = readInputStream();
 			final ChangeBehaviour cb = gsonIn.fromJson(jsonString, ChangeBehaviour.class);
 			for (final Robot r : user.getBattleRobots()) {
 				if (r.getID() == cb.robotID) {
 					r.setBehaviour(cb.behaviour);
 					return;
 				}
 			}
 		}
	}
	
	private long balance (final long val, final long baseVal, final long multip) {
		return (baseVal + (val * multip));
	}

	/**
	 * convert json to ClientUser Object
	 * 
	 * @param jsonString
	 * @return the User for this Client
	 * @throws IOException 
	 */
	private ClientUser readClientUser () throws IOException {
		final String jsonString = readInputStream();
		return (gsonIn.fromJson(jsonString, ClientUser.class));
	}

	/**
	 * get the SID from browser cookie
	 * 
	 * @param httpExchange
	 * @return return the session id in the cookie or null if no found
	 */
	private String getSessionID(HttpExchange httpExchange) {
		final String SID;

		// if browser send a sessionID, find user with this sessionID
		final String key = "SessionID=";
		Map m = httpExchange.getRequestHeaders();
		// get a list with all cookies from browser
		LinkedList<String> headerList = (LinkedList<String>) m.get("Cookie");

		if (headerList != null) { // no browser cookie
			for (String s : headerList) {
				if (s.indexOf(key) == 0) { // cookie has a SessionID=
					SID = s.substring(key.length());
					return (SID);
				}
			}
		}

		// no session id or browser cookie
		return (null);
	}

	/**
	 * add the SID do send object to write a (browser) session cookie
	 * 
	 * @param SID
	 */
	private void sendNewSID(String SID) {
		// send the sessionID to browser
		httpExchange.getResponseHeaders().add("Set-Cookie", "SessionID=" + SID);
	}

	/**
	 * read the datas how are send from client to jsonString
	 * @return the input stream as a String
	 * 
	 * @throws IOException
	 */
	private String readInputStream() throws IOException {
		final String jsonString;

		InputStream in = httpExchange.getRequestBody();

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte buf[] = new byte[4096];
			for (int n = in.read(buf); n > 0; n = in.read(buf)) {
				out.write(buf, 0, n);
			}
			jsonString = new String(out.toByteArray(), encoding);
		} finally {
			in.close();
		}
		
		return (jsonString);
	}
}
