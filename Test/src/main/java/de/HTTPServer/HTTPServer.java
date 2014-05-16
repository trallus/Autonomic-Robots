package de.HTTPServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import de.game.GameControler;
import de.general.Log;


public class HTTPServer {
	final GameControler gc;
	
	public HTTPServer ( GameControler gc ) throws IOException {
		this.gc = gc;
		
		int portNumber = 80;
		
	    HttpServer server = HttpServer.create( new InetSocketAddress( portNumber ), 0 );
	    server.createContext( "/", new DateHandler( gc ) );
	    server.start();

	    Log.log( "HTTP Server gestartet an port: " + portNumber );
	}
}
