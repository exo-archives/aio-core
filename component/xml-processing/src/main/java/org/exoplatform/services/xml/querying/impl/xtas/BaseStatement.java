package org.exoplatform.services.xml.querying.impl.xtas;

import java.util.ArrayList;
import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.exoplatform.services.xml.querying.Statement;

/**
 * Abstract XTAS Statement
 * Contains:
 * destinationId - Path|URI - Must be NULL if empty ! 
 * sourceId - Path|URI - Must be NULL if empty !
 * and FIFO-array of instructions to execute
 * @version $Id: BaseStatement.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
abstract public class BaseStatement implements InstructionsSupport, Statement {

    protected String destinationId;

    protected String sourceId;

    /**
     * @clientCardinality 1
     * @supplierCardinality 1..*
     * @labelDirection forward 
     */
    private ArrayList instructions = new ArrayList();

    /** @link dependency 
     * @stereotype include*/
    /*# Instruction lnkInstruction; */

    public final String getSourceId()
    {
        return sourceId;
    }

    public final void setSourceId(String sourceId)
    {
        this.sourceId = sourceId;
    }

    public final String getDestinationId()
    {
        return destinationId;
    }

    public final void setDestinationId(String destinationId)
    {
        this.destinationId = destinationId;
    }

    public Instruction[] getInstructions( ) 
    {
        Instruction[] ins = new Instruction[instructions.size()];
        for(int i=0; i<instructions.size(); i++)
            ins[i] = (Instruction)instructions.get(i);
        return ins;
//        return (Instruction[])instructions.toArray();
    }

    public void addInstruction (String type, String match, UniFormTree newValue) throws InvalidStatementException
    {

        if( QueryType.isAtResource( type ) ) {

            instructions.add( new ResourceInstruction(  type, match, newValue ) );
            this.destinationId = match;

        }  else if( QueryType.isAtXml( type ) ) 
            instructions.add( new XMLInstruction(  type, match, newValue ) );
        else
            throw new InvalidStatementException("Add Instruction Error: Invalid instruction type '"+type+"'!");

    }

    public Instruction pickNextInstruction() throws ForbiddenOperationException, InvalidStatementException
    {
        if( instructions.size() == 0 )
              throw new ForbiddenOperationException( "It is no more instructions!" );

//        Instruction instr = Instruction.create ( (Instruction)instructions.get( 0 ) );

        Instruction instr =  (Instruction)instructions.get( 0 );

//        instr.compile(sourceId, destinationId);

        instructions.remove(0);
        return instr;
    }

    public String toString()
    {
        String srcStr = "";
        String destStr = "";

        if( sourceId != null )
            srcStr = " source=\"" + sourceId + "\"";
        if( destinationId != null )
            destStr = " destination=\"" + destinationId + "\"";

        String instrString = "";
        for(int i=0; i<instructions.size(); i++)
             instrString += instructions.get(i); 

        return "<query" + srcStr + destStr + ">" + instrString + "</query>";
    }


}
