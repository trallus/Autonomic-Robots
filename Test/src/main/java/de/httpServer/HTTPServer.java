package de.httpServer;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import de.logger.Log;


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
	 * @throws Exception
	 */
	public HTTPServer(String keyURI, String httpPath,
		    int portNumber) throws Exception {

		UserManager plainUserManager = new UserManagerImpl();
		UserManager proxydUserManager = ( UserManager )
				Proxy.newProxyInstance(
						plainUserManager.getClass().getClassLoader(), 
						plainUserManager.getClass().getInterfaces(),
						new TranactionHandler ( plainUserManager ));

		HttpServer httpServer = HttpServer.create(new InetSocketAddress(
			portNumber), 0);
		httpServer.createContext("/", new DateHandler(proxydUserManager, httpPath, keyURI));
		httpServer.start();

		Log.log("HTTP Server gestartet an port: " + portNumber);
		
	}
}
