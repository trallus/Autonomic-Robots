package de.HTTPServer;

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
