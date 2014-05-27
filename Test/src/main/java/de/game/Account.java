package de.game;

public class Account {
	
	private String name;
	private String email; 
	private long pwHash;
	private TypePoint startPoint;
	
	/**
	 * Account constructor with all members as parameters. 
	 * @param name
	 * @param email
	 * @param pwHash
	 * @param startPoint
	 */
	public Account(String name, String email, long pwHash, TypePoint startPoint) {
		this.name = name;
		this.email = email;
		this.pwHash = pwHash;
		this.startPoint = startPoint;
	}
	
	//Getter und Setter: 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getPwHash() {
		return pwHash;
	}
	public void setPwHash(long pwHash) {
		this.pwHash = pwHash;
	}
	public TypePoint getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(TypePoint startPoint) {
		this.startPoint = startPoint;
	}
	
	

}
