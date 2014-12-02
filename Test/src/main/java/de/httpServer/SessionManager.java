package de.httpServer;

import java.util.Date;

/**
 * Manage the sessionID`s
 * @author Christian Köditz
 * @version 0.1
 * @since 15.10.2014
 */
public class SessionManager {
    /**
     * die Nummer der nächsten Session
     */
    private int sessionNumber = 0;
    
    /**
     * Erzeugt die nächste Session ID
     * @return neue Session ID
     */
    public String getSessionID () {
	if ( sessionNumber == Integer.MAX_VALUE )
	    sessionNumber = 0;
	
	sessionNumber ++;
	
	Date d = new Date();
	String id = ((int) (Math.random() * 1000000000)) + "" + d.getTime() + "" + sessionNumber;
	
	return ( id );
    }
}
