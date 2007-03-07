package org.exoplatform.services.xml.querying.impl.xtas;

import java.util.HashMap;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.exoplatform.services.xml.querying.InvalidSourceException;
import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.exoplatform.services.xml.querying.QueryRunTimeException;
import org.exoplatform.services.xml.querying.UniFormTransformationException;

/**
 * Statement instruction (action to do)
 * For now only one instruction for query will proccessed
 * @version $Id: Instruction.java 5799 2006-05-28 17:55:42Z geaz $
 */
abstract public class Instruction {

    private final static String XTAS_XSL = "/xtas.xsl";

    protected static InstructionCompiler compiler;

    protected String match;
    protected UniFormTree newValue;
    protected String type;

    static {

          try {

             String xtasXsl = System.getProperty("xtas.xsl");
             InputStream is = null;
             if( xtasXsl == null || xtasXsl.equals("") )
                 is = Instruction.class.getResourceAsStream(XTAS_XSL);
              else
                 is = new FileInputStream(xtasXsl);

              compiler = new InstructionCompiler( is );

         } catch (Exception e) {
           e.printStackTrace();
             // @ todo Log system!
//             System .out.println("Instruction() create ERROR: " + e);

         }

    }


    protected Instruction(String type, String match, UniFormTree newValue) throws InvalidStatementException
    {

        if( type == null || type.trim() == "")
            throw new InvalidStatementException( "Query Type can not be NULL !" );

        this.type = type.toLowerCase();
        if( ! QueryType.exists(this.type) )
            throw new InvalidStatementException("Illegal query type '"+this.type+"' !");

        if( match == null || match.trim() == "")
              throw new InvalidStatementException( "Match attribute is required for '"+type+"' statement");

        this.match = match;

        try {

            if (newValue == null) {

               this.newValue = new UniFormTreeFragment ();
               ((UniFormTreeFragment)this.newValue).init( new ByteArrayInputStream( "".getBytes()  ) );

            } else
               this.newValue = newValue;

        } catch (UniFormTransformationException e) {

            // @ todo Log system!
//            System .out.println("Instruction() create error(UniFormTransformationException) : " + e);

        }

    }

    /**
     * Copy constructor 
     */
    protected Instruction(Instruction instr)
    {

        this.type = instr.type;
        this.match = instr.match;
        this.newValue = instr.newValue;

    }

    public UniFormTree getNewValue()
    {
        return newValue;
    }

    public String getMatch()
    {
        return match;
    }

    public String getType()
    {
        return type;
    }

    public boolean isAtResource()
    {
        return QueryType.isAtResource( type );
    }

    public boolean isAtXml()
    {
        return QueryType.isAtXml( type );
    }

     /** 
     *   Gets query statement as String
     * */
    public String toString()
    {
        return getAsString();
    }


     /** 
     *   Gets instruction as String
     * */
    public abstract String getAsString();

     /**
     *   Executes instruction
     * */
    public abstract UniFormTree execute(UniFormTree input) throws InvalidSourceException, QueryRunTimeException;

     /**
     *   Compiles instruction
     *
     * */
    public abstract void compile() throws InvalidStatementException;


     /**
     *   Sets context
     *
     * */
    public abstract void setContext(Object resourceContext);

}
