package de.httpServer;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.game.GameControler;
import de.game.GameInterface;
import de.logger.Log;
import de.logger.LogLevel;
import de.logger.LoggerAndExceptionHandlerFacadeIF;

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
	private final GameInterface gameInterface;
	
	/**
	 * Instance of LoggerAndExceptionHandlerFacadeIF given in the handle Methode to the Server Request
	 */
	private final LoggerAndExceptionHandlerFacadeIF logFacade;

	/**
	 * @param gameControler 
	 * @param userManager
	 * @param httpPath
	 * @param keyURI
	 * @param logFacade the LoggerAndExceptionHandlerFacade that is used to get the Logger
	 */
	public DateHandler(GameInterface gameInterface, UserManager userManager, String httpPath, String keyURI, final LoggerAndExceptionHandlerFacadeIF logFacade) {
		this.userManager = userManager;
		this.httpPath = httpPath;
		this.keyURI = keyURI;
		this.gameInterface = gameInterface;
		this.logFacade = logFacade;
	}

	/* 
	 * switch between server and file requests and sent the answare
	 *
	 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
	 */
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		String uri = httpExchange.getRequestURI().toString();
		logFacade.getLoggerInstance().log("Angefragte URI: " + uri, this.getClass().getName(), LogLevel.DEBUG);

		Request request = null;

		try {
			if (uri.matches(".*" + keyURI + ".*")) {

				request = new ServerRequest(httpExchange, userManager, gameInterface, logFacade);
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