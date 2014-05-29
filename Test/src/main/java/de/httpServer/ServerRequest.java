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
import de.logger.Log;

public class ServerRequest extends Request {

	private String jsonString;
	private final HttpExchange httpExchange;
	private final String encoding = "UTF-8";
	private final Gson gsonOut = new GsonBuilder()
			.excludeFieldsWithoutExposeAnnotation().create();
	private final Gson gsonIn = new Gson();
	Map<String, Object> replyJson = new HashMap<String, Object>();
	private User user;

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
		handleURICommand(uri, userManager);

		replyJson.put("logedIn", user.isLogedIn());

		// convert serverReply to sendable content
		String json = gsonOut.toJson(replyJson);
		content = json.getBytes();
	}

	private void handleURICommand(String uri, UserManager userManager)
			throws Exception {

		if (uri.indexOf("registration") != -1) {
			readInputStream();
			ClientUser clientUser = convertToClientUser(jsonString);
			userManager.register(clientUser.name, clientUser.eMail, clientUser.password, user);
		} else if (uri.indexOf("logIn") != -1) {
			readInputStream();
			ClientUser clientUser = convertToClientUser(jsonString);
			userManager.logIn(clientUser.name, clientUser.password, user);
		} else if (uri.indexOf("logOut") != -1) {
			userManager.logOut(user);
		} else {
			replyJson.put("Unexpectrd URI", uri);
		}

	}

	private ClientUser convertToClientUser(String jsonString) {
		return (gsonIn.fromJson(jsonString, ClientUser.class));
	}

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

	private void sendNewSID(String SID) {
		// send the sessionID to browser
		httpExchange.getResponseHeaders().add("Set-Cookie", "SessionID=" + SID);
	}

	private void readInputStream() throws IOException {

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
	}
}
