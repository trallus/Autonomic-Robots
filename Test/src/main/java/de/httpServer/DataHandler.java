package de.httpServer;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.game.GameControler;
import de.logger.Log;

/**
 * handle the requests
 * switch between server and file requests
 * 
 * @author ko
 *
 */
class DateHandler implements HttpHandler {

	/**
	 * For managing the User
	 */
	private final UserManager userManager;
	/**
	 *  the root path of the http files
	 */
	private final String httpPath;
	/**
	 * if the destination of the request (URI) contains this key, it will be a server request
	 */
	private final String keyURI;

	/**
	 * @param userManager
	 * @param httpPath
	 * @param keyURI
	 */
	public DateHandler(UserManager userManager, String httpPath, String keyURI) {
		this.userManager = userManager;
		this.httpPath = httpPath;
		this.keyURI = keyURI;
	}

	/* 
	 * switch between server and file requests and sent the answare
	 *
	 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
	 */
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		String uri = httpExchange.getRequestURI().toString();
		Log.debugLog("Angefragte URI: " + uri);

		Request request = null;

		try {
			if (uri.matches(".*" + keyURI + ".*")) {

				request = new ServerRequest(httpExchange, userManager);
			} else {
				request = new DocumentRequest(httpExchange, httpPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		httpExchange.getResponseHeaders().add("Content-type",
				request.getMediaType());

		httpExchange.sendResponseHeaders(200, request.getContent().length);

		OutputStream os = httpExchange.getResponseBody();
		os.write(request.getContent());
		os.close();
	}
}