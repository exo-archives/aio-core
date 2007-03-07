package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.ByteArrayInputStream;
import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.exoplatform.services.xml.querying.UniFormTransformationException;

/**
 * XTAS query statement 
 * with only one instruction inside
 * 
 * The queryStatement is the data for the XTAS-Query
 * queryType - see <code>QueryType</code>
 * match (XPath) expression - *match* attribute in XSLT's xsl:template element
 * (XSLT uses the expression language defined by XPath)
 *                          - or Path|URI for query at resource (create,drop)
 * value for update - convertable to Result Tree Fragment (in XSLT terms)
 * source - resource id for the XTAS source
 * destination - resource id for the XTAS destination
 * 
 * @version $Id: SimpleStatement.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class SimpleStatement extends BaseStatement {

    /** 
     *  Builds query statement from scratch
     * */
    public SimpleStatement(String type, String match, UniFormTree newValue, String source, String destination) throws InvalidStatementException 
    // ParserConfigurationException, SAXException, IOException, TransformerException,
    {

        try {

           if( newValue == null ) {
              newValue = new UniFormTreeFragment();
              ((UniFormTreeFragment)newValue).init( new ByteArrayInputStream("".getBytes()) );
           }

        } catch (Exception e) {
           throw new InvalidStatementException("Create SimpleStatement: could not create UniFormTree! Reason: "+e);
        }

        this.sourceId = source;
        this.destinationId = destination;

        addInstruction( type, match, newValue );

    }

    /** Builds query statement from scratch */
    public SimpleStatement(String type, String match, String value, String source, String destination) throws InvalidStatementException, UniFormTransformationException
    {
        UniFormTreeFragment newValue = new UniFormTreeFragment ();
        if (value != null)
            newValue.init( new ByteArrayInputStream(value.getBytes()) );
        else
            newValue.init( new ByteArrayInputStream("".getBytes()) );

        this.sourceId = source;
        this.destinationId = destination;

//        addInstruction( Instruction.create (type, match, newValue) );
        addInstruction( type, match, newValue );

    }

    /** Builds *update* - like (with updating value) query statement */
    public SimpleStatement(String queryType, String match, String value) throws InvalidStatementException, UniFormTransformationException
    {
         this(queryType, match, value, null, null);
    }

    /** Builds *select* - like query statement */
    public SimpleStatement(String queryType, String match) throws InvalidStatementException, UniFormTransformationException
    {
        this(queryType, match, (String)null, null, null);
    }

    /** Constructs 'select' - query with '/' xpath */
    public SimpleStatement() throws InvalidStatementException
    {
        this(QueryType.SELECT, "/", (UniFormTreeFragment)null, null, null);
    }

    public static SimpleStatement select(String match, String source) throws InvalidStatementException
    {
        return select(match, source, null);
    }

    public static SimpleStatement select(String match) throws InvalidStatementException
    {
        return select(match, null, null);
    }

    public static SimpleStatement select(String match, String source, String destination) throws InvalidStatementException
    {
        if( match == null || match.length() == 0 )
           match = "/";
        return new SimpleStatement( QueryType.SELECT, match, (UniFormTreeFragment)null, source, destination);
    }

    public static SimpleStatement create(String name, String initXml) throws InvalidStatementException, UniFormTransformationException
    {
        return new SimpleStatement( QueryType.CREATE, name, initXml, null, name);
    }

    public static SimpleStatement create(String name, UniFormTree  initXml) throws InvalidStatementException, UniFormTransformationException
    {
        return new SimpleStatement( QueryType.CREATE, name, initXml, null, name);
    }

    public static SimpleStatement drop(String name) throws InvalidStatementException
    {
        return new SimpleStatement( QueryType.DROP, name, (UniFormTreeFragment)null, null, name);
    }

    public static SimpleStatement append(String match, String destination, UniFormTree value) throws InvalidStatementException
    {
        return new SimpleStatement( QueryType.APPEND, match, value, destination, destination);
    }

    public static SimpleStatement append(String match, String destination, String value) throws InvalidStatementException, UniFormTransformationException
    {
        return new SimpleStatement( QueryType.APPEND, match, value, destination, destination);
    }

    public static SimpleStatement update(String match, String destination, UniFormTree value) throws InvalidStatementException
    {
        return new SimpleStatement( QueryType.UPDATE, match, value, destination, destination);
    }

    public static SimpleStatement update(String match, String destination, String value) throws InvalidStatementException, UniFormTransformationException
    {
        return new SimpleStatement( QueryType.UPDATE, match, value, destination, destination);
    }

    public static SimpleStatement delete(String match, String destination) throws InvalidStatementException
    {
        return new SimpleStatement( QueryType.DELETE, match, (UniFormTreeFragment)null, destination, destination);
    }

}
