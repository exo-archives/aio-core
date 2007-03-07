package org.exoplatform.services.xml.querying.object;

/** This exception is thrown when condition occurred 
 * @version $Id: ObjectMarshalException.java 5799 2006-05-28 17:55:42Z geaz $*/
public class ObjectMarshalException extends Exception {
    /** Constructs an Exception without a message. */
    public ObjectMarshalException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public ObjectMarshalException(String message) {
        super(message);
    }
}
