package de.httpServer;

import de.data.DBUser;

public class User {
    private DBUser dbUser;
    public final String sessionID;
    private boolean logedIn = false;

    public User(String sessionID) {
	this.sessionID = sessionID;
    }
    
    public void setDBUser ( DBUser dbUser ) {
	this.dbUser = dbUser;
    }

    public void logIn(DBUser dbUser) {
	this.dbUser = dbUser;
	logedIn = true;
    }

    public void logOut() {
	logedIn = false;
    }

    public boolean isLogedIn() {
	return logedIn;
    }

    public DBUser getDBUser() {
	return (dbUser);
    }

    @Override
    public String toString() {
	return "User [sessionID=" + sessionID + ", logedIn=" + logedIn + "]";
    }

}
