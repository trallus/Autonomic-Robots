package de.httpServer;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.game.GameControler;
import de.logger.Log;

class DateHandler implements HttpHandler {

	private final UserManager userManager;
	private final String httpPath;
	private final String keyURI;

	public DateHandler(UserManager userManager, String httpPath, String keyURI) {
		this.userManager = userManager;
		this.httpPath = httpPath;
		this.keyURI = keyURI;
	}

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