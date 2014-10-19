package de.httpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import de.httpServer.ClientClasses.ClientUser;
import de.logger.ExceptionHandlerFacade;
import de.logger.Log;

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
	public ServerRequest(HttpExchange httpExchange, UserManager userManager)
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
			handleURICommand(uri, userManager);
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
	 * @throws Exception
	 */
	private void handleURICommand(String uri, UserManager userManager) throws Exception {

		if (uri.indexOf("registration") != -1) {
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
