package de.httpServer;

public abstract class Request {
	protected String mediaType;
	protected byte[] content;
	
	public String getMediaType() {
		return mediaType;
	}
	
	public byte[] getContent() {
		return content;
	}
}
