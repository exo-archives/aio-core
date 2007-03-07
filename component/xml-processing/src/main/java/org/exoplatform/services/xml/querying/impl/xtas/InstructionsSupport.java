package org.exoplatform.services.xml.querying.impl.xtas;
import org.exoplatform.services.xml.querying.InvalidStatementException;

/**
 * XTAS Statement's instructions 
 * life-cycle supported operations.
 * @version $Id: InstructionsSupport.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
public interface InstructionsSupport {
    Instruction pickNextInstruction() throws ForbiddenOperationException, InvalidStatementException;

    void addInstruction (String type, String match, UniFormTree newValue) throws InvalidStatementException;

    Instruction[] getInstructions( );
}
