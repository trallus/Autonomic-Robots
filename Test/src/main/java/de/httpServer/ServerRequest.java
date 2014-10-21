package de.httpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.omg.PortableInterceptor.ClientRequestInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import de.game.Battle;
import de.game.GameInterface;
import de.game.Robot;
import de.game.RobotPrototype;
import de.game.WeaponPrototype;
import de.game.exceptions.NotInQueryException;
import de.httpServer.ClientClasses.ClientNextRobot;
import de.httpServer.ClientClasses.ClientUser;
import de.logger.ExceptionHandlerFacade;
import de.logger.Log;
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

	/**
	 * handle the request
	 * 
	 * @param httpExchange
	 * @param userManager
	 * @throws Exception
	 */
	public ServerRequest(HttpExchange httpExchange, UserManager userManager, GameInterface gameInterface)
			throws Exception {

		mediaType = "application/json";

		this.httpExchange = httpExchange;

		final String SID = getSessionID(httpExchange);
		user = userManager.getUser(SID);
		// was a new user created?
		if (SID == null || !SID.equals(user.sessionID)) {
			Log.debugLog("Send new SID");
			sendNewSID(user.sessionID);
		}

		final String uri = httpExchange.getRequestURI().toString();
		try{
			handleURICommand(uri, userManager, gameInterface);
		}
		catch(Throwable arg){
			ExceptionHandlerFacade.getExceptionHandler().handle(arg, replyJson);
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
			ClientUser clientUser = readClientUser();
			String failReason = null;
			try {
				userManager.register(clientUser.name, clientUser.eMail, clientUser.password, user);
			} catch (EmailInUseException e) {
				failReason = "Email";
			} catch (NameInUseException e) {
				failReason = "Name";
			}
			if (failReason != null) {
				replyJson.put("registered", false);
				replyJson.put("registration message", failReason + " already in use");
				return;
			}
			
			replyJson.put("registered", true);
		} else if (uri.indexOf("logIn") != -1) {
			ClientUser clientUser = readClientUser();
			try {
				userManager.logIn(clientUser.eMail, clientUser.password, user);
			} catch (EmailNotFoundException e) {
				replyJson.put("logIn message", "Wrong e-mail or password");
			}
		} else if (uri.indexOf("logOut") != -1) {
			userManager.logOut(user);
		} else if (uri.indexOf("remove") != -1) {
			// just work with a new login -> save
			final ClientUser clientUser = readClientUser();
			try {
				userManager.removeUser(clientUser.eMail, clientUser.password, user);
			} catch (EmailNotFoundException e) {
				replyJson.put("logIn message", "Wrong e-mail or password");
				replyJson.put("removed", false);
				return;
			}
			replyJson.put("removed", true);
		} else if (uri.indexOf("searchUser") != -1) {
			final ClientUser clientUser = readClientUser();
			final String[] searchResule = userManager.searchUser(clientUser.name);
			replyJson.put("searchResult", searchResule);
		} else if (uri.indexOf("changeUser") != -1) {
			ClientUser clientUser = readClientUser();
			String failReason = null;
			try {
				userManager.changeUser(user, clientUser.name, clientUser.eMail, clientUser.password);
			} catch (EmailInUseException e) {
				failReason = "Email";
			} catch (NameInUseException e) {
				failReason = "Name";
			}
			if (failReason != null) {
				replyJson.put("userChanged", false);
				replyJson.put("userChanged message", failReason + " already in use");
				return;
			}
			
			replyJson.put("userChanged", true);
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
				final String un = u.getDBUser().getName();
				final ArrayList<Map<String, Object>> robotList = new ArrayList<Map<String, Object>>();
				gameSituation.put(un, robotList);
				final List<Robot> rl = u.getBattleRobots();
				for (Robot r : rl) {
					final Map<String, Object> robot = new HashMap<String, Object>();
					final Vector2D position = r.getPosition();
					final double[] posLong = {position.getN1(), position.getN2()};
					robot.put("position", posLong);
					robot.put("id", r.getID());
					robot.put("hitPoints", r.getHitPoints());
					robotList.add(robot);
				}
			}
			replyJson.put("gameSituation", gameSituation);
		} else if (uri.indexOf("setNextRobot") != -1) {
			final String jsonString = readInputStream();
			final ClientNextRobot cnr = gsonIn.fromJson(jsonString, ClientNextRobot.class);
			final WeaponPrototype wp = new WeaponPrototype(cnr.range, cnr.rateOfFire, cnr.damage);
			final RobotPrototype rp = new RobotPrototype(wp, cnr.armor, cnr.enginePower, cnr.behaviour);
			user.setNextRobot(rp);
		} else if (uri.indexOf("getBehaviours") != -1) {
			final String[] behaviours = {"gibts noch nicht", "und das auch nicht"};
			replyJson.put("behaviours", behaviours);
		} else if (uri.indexOf("leaveBattleQuery") != -1) {
			gameInterface.leaveBattleQuery(user);
		} else if (uri.indexOf("joinBattleQuery") != -1) {
			gameInterface.joinBattleQuery(user);
		}
	}

	/**
	 * convert json to ClientUser Object
	 * 
	 * @param jsonString
	 * @return
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
	 * @return
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
	 * @return 
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
