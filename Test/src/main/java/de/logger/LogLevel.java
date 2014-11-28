package de.logger;

/**
 * A Marker class for the logLevel
 * @author mike
 * @version 0.1
 */
public class LogLevel implements Comparable<LogLevel>{

    /**
     * OFF LogLevel means that nothing will be logged
     */
    public static final LogLevel OFF = new LogLevel(0);
    /**
     * Normal LogLevel means that things will be logged everytime the logging is not off
     */
    public static final LogLevel NORMAL = new LogLevel(1);
    /**
     * Debug LogLevel means that things will logged only in DEBUG logging
     */
    public static final LogLevel DEBUG = new LogLevel(2);
    
    /**
     * The value of the log level
     */
    private final int level;

    /**
     * Creates a LogLevel instance
     */
    private LogLevel(final int i) {
	this.level = i;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + level;
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	LogLevel other = (LogLevel) obj;
	if (level != other.level)
	    return false;
	return true;
    }

    @Override
    public int compareTo(LogLevel o) {
	return this.level - o.level;
    }

    

}
