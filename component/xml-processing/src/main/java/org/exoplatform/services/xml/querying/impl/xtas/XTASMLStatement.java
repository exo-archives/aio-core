package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.io.InputStream;

import org.xml.sax.SAXException;

import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

/**
 * Native (XTAS query syntax) Statement
 * See 'xtas-query.dtd' for more info
 * 
 * @version $Id: XTASMLStatement.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class XTASMLStatement extends BaseStatement 

{
    /**
     * Builds query from the local file 
     */
    public XTASMLStatement(String fileName) throws IOException, InvalidStatementException, SAXException  
    {
         this( new FileInputStream( fileName ) );
    }

    /**
     * Builds query from the URI resource 
     */
    public XTASMLStatement(URL url) throws IOException, InvalidStatementException, SAXException  
    {
         this( url.openStream() );
    }

    /**
     * Builds query from the input stream (main constructor for
     * the query builded from the *external source* like file,URI ) 
     */
    public XTASMLStatement(InputStream statStream) throws InvalidStatementException, SAXException 
    {

        XMLReader reader = null;

        try {
	  javax.xml.parsers.SAXParserFactory factory=
	      javax.xml.parsers.SAXParserFactory.newInstance();
	  factory.setNamespaceAware( true );
	  javax.xml.parsers.SAXParser jaxpParser=
	      factory.newSAXParser();
	  reader=jaxpParser.getXMLReader();
	  
        } catch( javax.xml.parsers.ParserConfigurationException ex ) {
	  throw new org.xml.sax.SAXException( ex );
        } catch( javax.xml.parsers.FactoryConfigurationError ex1 ) {
	  throw new org.xml.sax.SAXException( ex1.toString() );
        } catch( NoSuchMethodError ex2 ) {
        }

        XTASMLContentHandler handler = new XTASMLContentHandler();
        reader.setContentHandler( handler );


        try {

            reader.parse( new InputSource(statStream) );

        } catch( IOException e ) {

           throw new org.xml.sax.SAXException( e );
        }
        try {

           sourceId = handler.getSourceId();
           destinationId = handler.getDestinationId();

           UniFormTreeFragment nv = new UniFormTreeFragment();
           nv.init( new ByteArrayInputStream(handler.getNewValue().getBytes()) ); 
           addInstruction( handler.getType(), handler.getMatch(), nv);

        } catch( Exception e ) {

           throw new InvalidStatementException( "XTASMLStatement Create ERROR:"+e );
        }

    }

    public XTASMLStatement(byte[] stat) throws InvalidStatementException, SAXException 
    {
       this( new ByteArrayInputStream( stat ) );
    }
}
