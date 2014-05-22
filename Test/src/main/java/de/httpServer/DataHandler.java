package de.httpServer;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.game.GameControler;

class DateHandler implements HttpHandler {
	private final GameControler gc;
	
	public DateHandler( GameControler gc ) {
		this.gc = gc;
	}
	
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		String uri = httpExchange.getRequestURI().toString();
		System.out.println(uri);

		Request request;

		if (uri.indexOf("serverFrameRequest") != -1) {
			request = new FrameRequest(httpExchange, gc.robot);
		} else {
			request = new DocumentRequest(httpExchange);
		}
		
		httpExchange.getResponseHeaders().add("Content-type", request.getMediaType());

		httpExchange.sendResponseHeaders(200, request.getContent().length);

		OutputStream os = httpExchange.getResponseBody();
		os.write( request.getContent() );
		os.close();
	}
}