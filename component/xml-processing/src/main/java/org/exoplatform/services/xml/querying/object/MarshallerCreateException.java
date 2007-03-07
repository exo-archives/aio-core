package org.exoplatform.services.xml.querying.object;

/** This exception is thrown when condition occurred 
 * @version $Id: MarshallerCreateException.java 5799 2006-05-28 17:55:42Z geaz $*/
public class MarshallerCreateException extends Exception {
    /** Constructs an Exception without a message. */
    public MarshallerCreateException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public MarshallerCreateException(String message) {
        super(message);
    }
}
