package org.exoplatform.services.xml.querying;

/** This exception is thrown when 
 * invalid data for the statement occurs
 * @version $Id: InvalidStatementException.java 5799 2006-05-28 17:55:42Z geaz $*/
public class InvalidStatementException extends Exception {
    /** Constructs an Exception without a message. */
    public InvalidStatementException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public InvalidStatementException(String message) {
        super(message);
    }
}
