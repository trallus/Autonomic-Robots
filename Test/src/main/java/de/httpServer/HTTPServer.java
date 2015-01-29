package de.httpServer;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import de.game.GameControler;
import de.game.GameInterface;
import de.logger.LogLevel;
import de.logger.LoggerAndExceptionHandlerFacadeIF;


/**
 * Represents a http Server
 * 
 * @author ko
 *
 */
public class HTTPServer {
	
	/**
	 * @param keyURI if the destination of the request (URI) contains this key, it will be a server request
	 * @param httpPath the root path of the http files
	 * @param portNumber the server listen on this port
	 * @param logFacade the LoggerAndExceptionHandlerFacade that is used to get the Logger
	 * @throws Exception every invoked exception is thrown
	 */
	public HTTPServer(String keyURI, String httpPath,
		    int portNumber, final LoggerAndExceptionHandlerFacadeIF logFacade) throws Exception {

		final GameInterface gameInterface = new GameControler(logFacade);

		UserManager plainUserManager = new UserManagerImpl(logFacade);
		UserManager proxydUserManager = ( UserManager )
				Proxy.newProxyInstance(
						plainUserManager.getClass().getClassLoader(), 
						plainUserManager.getClass().getInterfaces(),
						new TranactionHandler ( plainUserManager ));

		HttpServer httpServer = HttpServer.create(new InetSocketAddress(
			portNumber), 0);
		
		httpServer.setExecutor(Executors.newCachedThreadPool());
		
		httpServer.createContext("/", new DateHandler(gameInterface, proxydUserManager, httpPath, keyURI, logFacade));
		httpServer.start();

		logFacade.getLoggerInstance().log("HTTP Server gestartet an port: " + portNumber, this.getClass().getName(), LogLevel.NORMAL);
		
	}
}
