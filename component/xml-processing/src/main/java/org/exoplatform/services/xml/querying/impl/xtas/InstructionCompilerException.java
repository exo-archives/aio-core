package org.exoplatform.services.xml.querying.impl.xtas;

/** This exception is thrown when condition occurred */
public class InstructionCompilerException extends Exception {
    /** Constructs an Exception without a message. */
    public InstructionCompilerException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public InstructionCompilerException(String message) {
        super(message);
    }
}
