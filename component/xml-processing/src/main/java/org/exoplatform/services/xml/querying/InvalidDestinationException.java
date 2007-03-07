package org.exoplatform.services.xml.querying;

/** This exception is thrown when 
 * something wrong with destination resource
 * @version $Id: InvalidDestinationException.java 5799 2006-05-28 17:55:42Z geaz $*/
public class InvalidDestinationException extends Exception {
    /** Constructs an Exception without a message. */
    public InvalidDestinationException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public InvalidDestinationException(String message) {
        super(message);
    }
}
