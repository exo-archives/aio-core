package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exoplatform.services.xml.querying.InvalidDestinationException;
import org.exoplatform.services.xml.querying.InvalidSourceException;
import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.exoplatform.services.xml.querying.QueryRunTimeException;
import org.exoplatform.services.xml.querying.Statement;
import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.XMLData;
import org.exoplatform.services.xml.querying.XMLQuery;
import org.exoplatform.services.xml.querying.impl.xtas.resource.Resource;
import org.exoplatform.services.xml.querying.impl.xtas.resource.ResourceResolver;

/**
 * Encapsulates XTAS Query
 * Life Cycle of Query:
 * 1. Create (constructor)
 * 2. setInputStream() (optional if not in the Content) 
 * 3. execute()
 * 4. serialize() or getResult()
 * For the time only one instruction will be executed
 * @version $Id: Query.java 5799 2006-05-28 17:55:42Z geaz $
 *
 */
public class Query implements XMLQuery {

    private static Log _log = LogFactory.getLog(Query.class);

    private BaseStatement statement;
    private Object resourceContext;

    private UniFormTree input;
    private UniFormTree result = null;

//    private Resource destination;
    private boolean validate=false;

    private Instruction executedInstruction;

    /** @link dependency 
     * @stereotype use*/
    /*# QueryType lnkQueryType; */

    /** @link dependency 
     * @stereotype use*/
    /*# ResourceResolver lnkResourceResolver; */

    public Query() 
    {
    }

    /**
     * The main Constructor.
     * <code>content</code> must be with source - attribute
     */
    public Query(Statement statement) throws InvalidSourceException
    {
       prepare ( statement );
    }

    /**
     * Constructor for external source (inputStream)
     * NOTE: stream must contain the well-formed XML!
     */
    public Query(Statement statement, InputStream inputStream) throws InvalidSourceException
    {
         this(statement);
         setInputStream(inputStream);
    }

    /**
     * Executes this query
     * For the time only one instruction will executed
     */
    public void execute() throws InvalidSourceException, QueryRunTimeException
    {

       if( this.statement == null) 
           throw new QueryRunTimeException("Query execution Error: XTAS Statement Can Not be NULL. Call prepare() first !");

       Instruction curInstruction = null;

       int count = statement.getInstructions().length;
       if( count == 0) 
           throw new QueryRunTimeException("Query execution Error: XTAS Statement has not instructions to execute !");

       if( count > 1) 
           throw new QueryRunTimeException("TEMPORARY Query execution Error: XTAS Statement does not support more than 1 instructions!");


       for(int i=0; i<count; i++) {

          try {

                curInstruction = statement.pickNextInstruction();

                curInstruction.compile();

                curInstruction.setContext(resourceContext);

                result = curInstruction.execute(input);

           } catch (InvalidStatementException e) {

               throw new QueryRunTimeException(e.toString());

           } catch (ForbiddenOperationException e) {

               throw new QueryRunTimeException(e.toString());

           }

           executedInstruction = curInstruction;
       }

    }

    /**
     * Serializes (stores) querie's result into <destination> 
     */
    public void serialize() throws IOException, InvalidDestinationException
    {

       Resource destination;
       if( result == null ) // DO NOTHING!
            return;

        String destId = statement.getDestinationId();


        if( destId != null ) {
           try {

               destination = ResourceResolver.getInstance().getResource( destId );

           } catch (Exception e) {

                throw new InvalidDestinationException("Query.serialize() Can not prepare query's destination. Reason: " + e);

           } 

        } else {
                throw new InvalidDestinationException("Query.serialize() Can not prepare query's destination. Destination ID is NULL ");
        }
    
       if( destination != null ) {
           try {

              destination.setContext(resourceContext);
              WellFormedUniFormTree _xml = UniFormConverter.toWellForm(this.result);

              _xml.setOmitXmlDeclaration(false);
              _xml.setIndentOutput(true);

              String sysId = null;
              String pubId = null;
              if( input != null ) {

                  WellFormedUniFormTree _inxml = UniFormConverter.toWellForm(this.input);
                  sysId = _inxml.getValidationHandler().getSystemId();
                  pubId = _inxml.getValidationHandler().getPublicId();

              } 

              if( sysId != null ) 
                  _xml.setDTDSystemId(sysId);
              if( pubId != null ) 
                  _xml.setDTDPublicId(pubId);

              if( sysId != null || pubId != null ) 
                  _xml.setValidate( true );
              else 
                  _xml.setValidate( false );

              if(result != null) //DROP returns null
                  destination.save( _xml );

           } catch (UniFormTransformationException e) {

               throw new InvalidDestinationException("Can not save the query result Reason: " + e);

           } catch (IOException e) {

               throw new IOException("Query Serialization I/O error: "+ e);
           } catch (Exception e) {

               throw new IOException("Query Serialization error: "+ e);
           } finally {
               destination.close();
           }


       } else
           throw new InvalidDestinationException("XTAS Destination not described!");

    }
    /**
     * Prepares this query
     */
    public void prepare( Statement statement ) throws InvalidSourceException
    {
       prepare( statement, null );
    }

    /**
     * Prepares this query
     */
    public void prepare( Statement statement, Object resourceContext ) throws InvalidSourceException
    {

        String srcId = statement.getSourceId();
      
        _log.debug("Prepare query: "+statement.toString()+" Context: "+resourceContext);

//        S ystem.out.println("Prepare query: "+statement.toString()+" Context: "+resourceContext);

        Resource source = null;

        try {

           if( srcId != null ) {

              source = ResourceResolver.getInstance().getResource( srcId );
              source.setContext(resourceContext);
              loadSource(source);
           }

        } catch (Exception e) {
             throw new InvalidSourceException("Can not prepare query's source. Source: "+srcId+" Reason: " + e);

        } finally {

           if( source != null ) 
              source.close();
        }  

        this.statement = (BaseStatement)statement;
        this.resourceContext = resourceContext; 

    }

    /**
     * Prepares this query
     * Statement's source ID will be ignored
     * Input for the query is result of previous execution.
     */

    public void prepareNext( Statement statement ) throws InvalidSourceException
    {

        if(result == null) 
             throw new InvalidSourceException("Query.prepareNext("+statement+") Query's result is NULL!");
        this.input = this.result;
        this.statement = (BaseStatement)statement;

    }

   public void setInputStream(InputStream inputStream) throws InvalidSourceException
    {
        if (inputStream == null) 
            throw new InvalidSourceException("Can not init query inputStream with NULL value!");

        // It is not necessary to be Well formed here
        try {

            this.input = new UniFormTreeFragment ();
            ((UniFormTreeFragment)this.input).init( inputStream );

        } catch (UniFormTransformationException e) {

            throw new InvalidSourceException("Can not init query's inputStream. Reason: " + e);

        }
    }

    /**
     * Laizy loader for the XML source load in run-time
     */
    public void loadSource(Resource source) throws InvalidSourceException
    {

         // From here the input MUST be Well Formed!
         try {

             this.input = (UniFormTree)source.load();

         } catch (UniFormTransformationException e) {

             throw new InvalidSourceException("Can not Load the source for the Query. Reason: " + e);

         } catch (IOException e) {

               throw new InvalidSourceException("Query Source I/O error: "+ e);
         }
    }

   public XMLData getInput()
   { 
        return input; 
   }

   public void setInput(XMLData input)
    {
        this.input = (UniFormTree)input;
    }

    public XMLData getResult()
    { 
        return result; 
    }

   public void setResult(XMLData result)
    {
        this.result = (UniFormTree)result;
    }

    /**
     * For debugging only! 
     */
    public Instruction getExecutedInstruction() throws ForbiddenOperationException
    {
        if (executedInstruction != null)

           return executedInstruction;/*.toString();*/
        else 

           return null;
    }

    public void setDestination(String destinationId)
    {
        this.statement.setDestinationId(destinationId);
    }

    /**
     * Constructor For chain of queries
     * Applicable only for well-formed result !!!!
     * @deprecated Use prepareNext instead.
     */
    public Query createNext(Statement statement)
    {
        Query q = null;

        try {

            q = new Query( statement );
            q.setInput( UniFormConverter.toFragment( result ) );

        // Impossible situation!
        } catch (Exception e) {
            _log.fatal("Query.createNext STRANGE Exception!", e);
        }
        return q;

    }

}
