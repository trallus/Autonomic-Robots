package de.httpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import de.game.GameControler;
import de.logger.Log;


public class HTTPServer {
	final GameControler gc;
	
	public HTTPServer ( GameControler gc, int portNumber ) throws IOException {
		this.gc = gc;
		
		
	    HttpServer server = HttpServer.create( new InetSocketAddress( portNumber ), 0 );
	    server.createContext( "/", new DateHandler( gc ) );
	    server.start();

	    Log.log( "HTTP Server gestartet an port: " + portNumber );
	}
}
