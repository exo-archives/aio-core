package org.exoplatform.services.xml.querying.impl.xtas;
import org.exoplatform.services.xml.querying.InvalidSourceException;
import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.exoplatform.services.xml.querying.QueryRunTimeException;
import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.impl.xtas.resource.Resource;
import org.exoplatform.services.xml.querying.impl.xtas.resource.ResourceResolver;

/**
 * Resource processing based instruction
 * @version $Id: ResourceInstruction.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
public class ResourceInstruction extends Instruction {

    private Object context;

    /**
     * Copy constructor 
     */
    public ResourceInstruction(Instruction instr)
    {
       super(instr);
    }

    public ResourceInstruction(String type, String match, UniFormTree newValue) throws InvalidStatementException
    {
        super(type, match, newValue);
    }

    public String getAsString()
    {
        String matchStr = "";
        if( match != null )
            matchStr = " resource=\"" + match + "\"";

        return  "<" + type + matchStr + ">" + 
                newValue.getAsString() + 
               "</" + type + ">";
    }

    /**
    * Compiles instruction (does nothing for ResourceInstruction)
    *
    */
    public void compile() throws InvalidStatementException
    {
       return;
    }

    /**
    *   Executes instruction
    *  
    * */
    public UniFormTree execute(UniFormTree input) throws InvalidSourceException, QueryRunTimeException
    {


       try {

          Resource res = ResourceResolver.getInstance().getResource( match );
          res.setContext( context );

          if ( type == QueryType.CREATE ) {

              // New Value for initial tree MUST be well-formed.
              // MAY BE <resource name="name"> - REQUIRED 
              //            <dtd systemId="systemId" publicId="publicId"> 
              //            [!CDATA]  
              //            </dtd>
              //        </resource>  


               WellFormedUniFormTree initTree = UniFormConverter.toWellForm(newValue);
//               initTree.setOmitXmlDeclaration(false);
               res.create( initTree );

               return newValue;

          }

          if ( type == QueryType.DROP ) {
             res.drop();
             return null;
          }
       } catch (UniFormTransformationException  e) {

            throw new InvalidSourceException("Bad Resource Instruction (possible newValue is not well-formed) : " + e);

       } catch (Exception e) {

            throw new QueryRunTimeException("Query Run Time Exception: " + e);
       }

       return null;

    }

    public void setContext(Object resourceContext)
    {
       this.context = resourceContext;
    }

}
