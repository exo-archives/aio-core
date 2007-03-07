package org.exoplatform.services.xml.querying;

/** This exception is thrown when condition occurred 
 * @version $Id: QueryRunTimeException.java 5799 2006-05-28 17:55:42Z geaz $*/
public class QueryRunTimeException extends Exception {
    /** Constructs an Exception without a message. */
    public QueryRunTimeException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public QueryRunTimeException(String message) {
        super(message);
    }
}
