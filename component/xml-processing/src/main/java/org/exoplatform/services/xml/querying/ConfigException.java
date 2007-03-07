package org.exoplatform.services.xml.querying;

/** This exception is thrown when condition occurred */
public class ConfigException extends Exception {
    /** Constructs an Exception without a message. */
    public ConfigException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public ConfigException(String message) {
        super(message);
    }
}
