package de.httpServer;

/**
 * Abstract Request
 * 
 * @author ko
 *
 */
public abstract class Request {
	/**
	 * The name of the http media type
	 */
	protected String mediaType;
	/**
	 * The content of the Request
	 */
	protected byte[] content;
	
	/**
	 * @return the media mype
	 */
	public String getMediaType() {
		return mediaType;
	}
	
	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}
}
