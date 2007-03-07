package org.exoplatform.services.xml.querying.impl.xtas;
import org.exoplatform.services.xml.querying.InvalidSourceException;
import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.exoplatform.services.xml.querying.QueryRunTimeException;

/**
 * Data processing based instruction
 * @version $Id: XMLInstruction.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
public class XMLInstruction extends Instruction {

     private static QueryProcessor processor = QueryProcessorFactory.getInstance().getProcessor();
     Command command;

    /**
     * Copy constructor 
     */
    public XMLInstruction(Instruction instr)
    {
        super(instr);
    }

    public XMLInstruction(String type, String match, UniFormTree newValue) throws InvalidStatementException
    {
        super(type, match, newValue);
    }

    public String getAsString()
    {

        String matchStr = "";
        if( match != null )
            matchStr = " xpath=\"" + match + "\"";

        return  "<" + type + matchStr + ">" + 
                newValue.getAsString() + 
               "</" + type + ">";
    }

    /**
    *   Compiles instruction
    *
    * */
    public void compile() throws InvalidStatementException
    {
        this.command = compiler.compile( getAsString() );
        processor.init(command);
//        processor.setParameter("sourceId", sourceId);
//        processor.setParameter("destinationId", destinationId);
    }

    /**
    *  Debug
    * */
    public String getCommandAsString()
    {
       return command.toString();
    }

    /**
    *   Executes instruction
    *   For Resource Instruction must return null.
    * */
    public UniFormTree execute(UniFormTree input) throws InvalidSourceException, QueryRunTimeException
    {
           if( input == null )
               throw new InvalidSourceException("Query execution Error: XTAS Source Can Not be NULL !");
           if( command == null )
               throw new QueryRunTimeException("Query execution Error: Command has not be created, call compile() first!");

           return processor.process( command, input );
    }

    public void setContext(Object resourceContext)
    {}

}
