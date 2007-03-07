package org.exoplatform.services.xml.querying;

/** This exception is thrown when UniFormTree object
 * can not be created
 * @version $Id: UniFormTransformationException.java 5799 2006-05-28 17:55:42Z geaz $*/
public class UniFormTransformationException extends Exception {
    /** Constructs an Exception without a message. */
    public UniFormTransformationException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public UniFormTransformationException(String message) {
        super(message);
    }
}
