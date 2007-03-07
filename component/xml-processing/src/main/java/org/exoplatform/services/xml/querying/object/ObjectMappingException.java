package org.exoplatform.services.xml.querying.object;

/** This exception is thrown when condition occurred 
 * @version $Id: ObjectMappingException.java 5799 2006-05-28 17:55:42Z geaz $*/
public class ObjectMappingException extends Exception {
    /** Constructs an Exception without a message. */
    public ObjectMappingException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public ObjectMappingException(String message) {
        super(message);
    }
}
