package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.XMLWellFormedData;
import org.exoplatform.services.xml.querying.impl.xtas.object.MappingType;
import org.exoplatform.services.xml.querying.impl.xtas.object.ObjectMarshaller;
import org.exoplatform.services.xml.querying.impl.xtas.object.ObjectMarshallerFactory;
import org.exoplatform.services.xml.querying.impl.xtas.xml.DtdTracer;
import org.exoplatform.services.xml.querying.impl.xtas.xml.Utils;
import org.exoplatform.services.xml.querying.object.MarshallerCreateException;
import org.exoplatform.services.xml.querying.object.ObjectMappingException;
import org.exoplatform.services.xml.querying.object.ObjectMarshalException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;


/**
 * Well-formed UniFormTree - "natural" XML
 * Can be validated, serialized etc.
 * 
 * @version $Id: WellFormedUniFormTree.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class WellFormedUniFormTree extends UniFormTree implements XMLWellFormedData {

    private DtdTracer validationHandler;
    protected XMLReader reader=null;
    protected TransformerHandler handler;
    protected static SAXTransformerFactory sFactory;
    protected static SAXParserFactory parserFactory;


    static {

        try {

             parserFactory =  SAXParserFactory.newInstance();
             sFactory = (SAXTransformerFactory) tFactory;

        } catch (Exception e) {
            // @todo Log system!
//            System .out.println("Can not INSTANTIATE WellFormedUniFormTree Object Reason: " + e);
        }

    }


    /**
     * Creates an empty UniFormTree 
     */
    public WellFormedUniFormTree() throws UniFormTransformationException
    {
         validationHandler = new DtdTracer();

         try {

        // If this feature is set to true, the document must specify a
        // grammar. By default, validation will occur against DTD.
        // If this feature is set to false, and document specifies a
        // grammar that grammar might be parsed but no validation
        // of the document contents will be performed.


             handler = sFactory.newTransformerHandler();
             handler.setResult(new StreamResult( this.thisStream ));

             handler.getTransformer().setOutputProperty(
                 javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");

             reader = parserFactory.newSAXParser().getXMLReader();

             reader.setContentHandler(handler);

             setValidate(false);

             reader.setProperty("http://xml.org/sax/properties/lexical-handler",
                                validationHandler);
             reader.setProperty("http://xml.org/sax/properties/declaration-handler",
                                validationHandler);


         } catch (Exception e) {
             // @todo Log system!
             throw new UniFormTransformationException (" WellFormedUniFormTree(InputStream stream) ERROR: Reason: " + e);
         }


    }

    /**
     * Initializes this with the SAX's InputSource 
     */
    public void init(InputSource src) throws UniFormTransformationException
    {

       try {

           reader.parse( src );

        } catch (Exception e) {

           throw new UniFormTransformationException("UniformTree.Init(inputStream): Can not parse InputSource Reason: " + e);
        }
    }

    /**
     * Initializes this with the DOM's Node 
     */
    public void init(Node node) throws UniFormTransformationException
    {

        try {

             // Reinitialize thisStream 
             thisStream = new ByteArrayOutputStream();

             transformer.transform( new DOMSource(node) , 
                                    new StreamResult( thisStream ) );

        //FactoryConfigurationError,FactoryConfigurationError,SAXException
        } catch (Exception e) {

            throw new UniFormTransformationException("UniformTree.Init(Node): Can not Transform Reason: " + e);

        }
    }

    /**
     * Marshalls Java object using obj.getClass() as mapping and creates UniFormTree object
     * WARNING: Will be used for all objects except  NodeList, InputStream, DOM Document, InputSource
     */
    public void init(Object obj) throws ObjectMarshalException, MarshallerCreateException, ObjectMappingException
    {
        if ( (obj instanceof InputStream) || (obj instanceof NodeList) || (obj instanceof Document) || (obj instanceof InputSource) )
            throw new ObjectMappingException( "DEV-ERROR! init(Object) Parameter MUST NOT BE "+
                       obj.getClass().getName()+" TYPE!");
        init( obj.getClass(), obj );
    }

    /**
     * Marshalls Java object using <code> mapping </code> and creates UniFormTree object
     */
    public void init(Object mapping, Object obj) throws ObjectMarshalException, MarshallerCreateException, ObjectMappingException
    {
        // Reinitialize thisStream 
        thisStream = new ByteArrayOutputStream();


        int mappingType = MappingType.INTERNAL;
        if( !(mapping instanceof Class) )
            mappingType =  MappingType.CUSTOM;

        ObjectMarshaller marshaller = ObjectMarshallerFactory.getInstance().getMarshaller( mappingType );
        marshaller.loadMapping( mapping );

        try {

            Document doc = Utils.createDocument();
            doc = marshaller.marshal( obj );

            transformer.transform( new DOMSource(doc) , 
                                   new StreamResult( thisStream ) );
        } catch (Exception e) {

            throw new ObjectMarshalException("UniFormTree.init(Object mapping, Object obj) Exception: " + e);
        }
    }

    /**
     * Initializes with root node 
     * Note: it is impossible to insert without child
     * adding " " for as a text child - Statement.append("root/text()",.....)
     */
    public void initRoot(String nodeName) throws UniFormTransformationException
    {

       try {

           init ( new InputSource (new ByteArrayInputStream(("<"+nodeName+"> </"+nodeName+">").getBytes() ) ) );

        } catch (Exception e) {

           throw new UniFormTransformationException("UniformTree.Init(inputStream): Can not parse InputSource Reason: " + e);
        }
    }

    /**
     * This object as ByteArray (is required by superclass)
     */
    public byte[] getAsByteArray()
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StreamResult sr = new StreamResult(bos);
        convert( sr );
        return bos.toByteArray();
    }

    /**
     * This object as DOM Node
     */
    public Node getAsDOM()
    {

        Document tmpDoc = null;
        try {

            tmpDoc = Utils.createDocument();

        } catch (ParserConfigurationException e) {
//            throw new UniFormTransformationException("Can not create DOM Reason: " + e);
//            System .out.println("UniFormTree.getAsDOM(): Can not create DOM Reason: " + e);
        }

        convert( new DOMResult(tmpDoc) );
        return tmpDoc;
    }

    /**
     * Parse this XML using ContentHandler
     */
    public void processAsSAX(ContentHandler handler) throws SAXException
    {
        try {
           convert( new SAXResult(handler), true );

        } catch (Exception e) {

           throw new SAXException("WellFormedUniFormTree.processAsSAX() failed. Reason: "+e);
        }
    }

    /**
     * Parse this XML using ContentHandler
     */
    public void transformWithXSL(Result result, Source xslTemplate) throws TransformerException
    {

        try {
             if(xslTemplate == null)
                 convert( result, true );
             else {
                Transformer trans;
                trans = tFactory.newTransformer(xslTemplate);
                trans.transform( new StreamSource( new ByteArrayInputStream( 
                                     thisStream.toByteArray() ) ), result  );
             }

        } catch (Exception e) {

           throw new TransformerException("WellFormedUniFormTree.transformWithXSL() failed. Reason: "+e);
        }
    }

    /**
     * Will output be wit the xml declaration?
     */
    public void setOmitXmlDeclaration(boolean yesNo)
    {
        transformer.setOutputProperty(
            javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, (yesNo?"yes":"no"));
    }

    /**
     * Will output be indent?
     */
    public void setIndentOutput(boolean yesNo)
    {
        transformer.setOutputProperty(
            javax.xml.transform.OutputKeys.INDENT, (yesNo?"yes":"no"));
    }

    /**
     * SystemId for output
     */
    public void setDTDSystemId(String systemId)
    {
        transformer.setOutputProperty(
             javax.xml.transform.OutputKeys.DOCTYPE_SYSTEM, systemId);
    }

    /**
     * PublicId for output
     */
    public void setDTDPublicId(String publicId)
    {
        transformer.setOutputProperty(
             javax.xml.transform.OutputKeys.DOCTYPE_PUBLIC, publicId);
    }

    public DtdTracer getValidationHandler() 
    {
       return validationHandler;
    }

    /**
     * Will this object be validated?
     */
    public void setValidate(boolean yesNo)
    {
         try {
             reader.setFeature("http://xml.org/sax/features/validation", yesNo);

         } catch (Exception e) {
             // @todo Log system!
//            System .out.println("WellFormedUniFormTree.setValidate(): " + e);
         }
    }
}
