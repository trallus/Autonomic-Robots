package de.HTTPServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;


public class HTTPServer {

	public static void main(String[] args) throws IOException
	  {
		Robot robot = new Robot();
	    HttpServer server = HttpServer.create( new InetSocketAddress( 80 ), 0 );
	    server.createContext( "/", new DateHandler( robot ) );
	    server.start();
	  }

}
