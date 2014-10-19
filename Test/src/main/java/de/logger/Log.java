package de.logger;
/**
 * To control the logging
 * @author ko
 * @deprecated use ExceptionHandler instead
 */
public class Log {
	/**
	 * Los standard messages
	 * @param message
	 */

	public static void log (String string) {
		System.out.println ( string );
	}

	public static void info (String string) {
		log ( string );
	}

	public static void errorLog (String string) {
		log ( string );
	}

	public static void errorLog (Exception e) {
		e.printStackTrace();
	}

	public static void debugLog (String string) {
		log ( string );
	}
}
