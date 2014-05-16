package de.HTTPServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;


public class FrameRequest extends Request {
	
	public FrameRequest ( HttpExchange httpExchange, Robot robot ) {
		mediaType = "application/json";
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Gson gson = new Gson();
		content = gson.toJson(robot.getNextPosition()).getBytes();
	}
}
