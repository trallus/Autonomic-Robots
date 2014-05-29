package de.httpServer;

import java.util.Date;

public class SessionManager {
    private int sessionNumber = 0;
    
    public String getSessionID () {
	if ( sessionNumber == Integer.MAX_VALUE )
	    sessionNumber = 0;
	
	sessionNumber ++;
	
	Date d = new Date();
	String id = ((int) (Math.random() * 1000000000)) + "" + d.getTime() + "" + sessionNumber;
	
	return ( id );
    }
}
