package de.httpServer;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import de.logger.Log;


public class HTTPServer {
	
	public HTTPServer(String keyURI, String httpPath,
		    int portNumber) throws Exception {

		UserManager userManager = new UserManager();

		HttpServer httpServer = HttpServer.create(new InetSocketAddress(
			portNumber), 0);
		httpServer.createContext("/", new DateHandler(userManager, httpPath, keyURI));
		httpServer.start();

		Log.log("HTTP Server gestartet an port: " + portNumber);
		
	    }
}
