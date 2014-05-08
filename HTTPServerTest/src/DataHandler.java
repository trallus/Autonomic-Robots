import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class DateHandler implements HttpHandler {
	private Robot robot;
	
	public DateHandler( Robot robot ) {
		this.robot = robot;
	}
	
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		String uri = httpExchange.getRequestURI().toString();
		System.out.println(uri);

		Request request;

		if (uri.indexOf("serverFrameRequest") != -1) {
			request = new FrameRequest(httpExchange, robot);
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