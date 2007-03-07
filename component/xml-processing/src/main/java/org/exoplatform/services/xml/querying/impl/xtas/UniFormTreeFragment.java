package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.SequenceInputStream;
import java.io.ByteArrayInputStream;
import java.util.Collection;

import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.parsers.ParserConfigurationException;

import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.XMLFragmentData;
import org.exoplatform.services.xml.querying.impl.xtas.object.MappingType;
import org.exoplatform.services.xml.querying.impl.xtas.object.ObjectMarshaller;
import org.exoplatform.services.xml.querying.impl.xtas.object.ObjectMarshallerFactory;
import org.exoplatform.services.xml.querying.impl.xtas.xml.Utils;
import org.exoplatform.services.xml.querying.object.MarshallerCreateException;
import org.exoplatform.services.xml.querying.object.ObjectMappingException;
import org.exoplatform.services.xml.querying.object.ObjectMarshalException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;


/**
 * Conceptually it is a DocumentFragment (in DOM terms)
 * May be realised as Result Tree Fragment (in XSLT terms)
 * 
 * @version $Id: UniFormTreeFragment.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class UniFormTreeFragment extends UniFormTree implements XMLFragmentData {


    public UniFormTreeFragment() //throws UniFormTransformationException
    {
    }

    /**
     * Initializes this with the io.InputStream 
     */
    public void init(InputStream stream) throws UniFormTransformationException
    {

        // Reinitialize thisStream 
        try {
             thisStream = new ByteArrayOutputStream();
        } catch (Exception e) {
            throw new UniFormTransformationException("UniformTree.Init(InputStream): Can not Transform Reason: " + e);
        }

        SequenceInputStream s = new SequenceInputStream(
             new ByteArrayInputStream( "<synthetical-root>".getBytes() ), stream );
        s = new SequenceInputStream( 
             s, new ByteArrayInputStream( "</synthetical-root>".getBytes() ) );

        try {

               transformer.transform( new StreamSource(s), 
                                      new StreamResult( thisStream ) );

        } catch (Exception e) {

            throw new UniFormTransformationException("UniformTree.Init(inputStream): Can not parse InputStream Reason: " + e);

        }

    }

    /**
     * Initializes this with the DOM NodeList 
     * DOES NOT WORK properly!
     */

    public void init(NodeList list) throws UniFormTransformationException
    {

        try {

            thisStream.close();
            thisStream.write("<synthetical-root>".getBytes(),0, "<synthetical-root>".length() );

            ByteArrayOutputStream tmpStream = null;

            for(int i=0; i< list.getLength(); i++) {

                 transformer.transform( new DOMSource(list.item(i)) , 
                                        new StreamResult( tmpStream ) );


                 if (tmpStream!=null)
                    tmpStream.writeTo(thisStream);

             }

             thisStream.write("</synthetical-root>".getBytes(),0, "</synthetical-root>".length() );

        //FactoryConfigurationError,FactoryConfigurationError,SAXException
        } catch (Exception e) {


            throw new UniFormTransformationException("UniformTree.Init(NodeList): Can not Transform Reason: " + e);

        }
    }


    public byte[] getAsByteArray() //throws UniFormTransformationException
    {
       ByteArrayOutputStream bos = new ByteArrayOutputStream();
       StreamResult sr = new StreamResult(bos);
       convert( sr );

       byte[] buffer = bos.toByteArray();
       if (buffer.length <= "<synthetical-root/>".length() )
           return "".getBytes();
       String inStr = new String(buffer);
       String result = inStr.substring( inStr.indexOf("<synthetical-root>")+
                       "<synthetical-root>".length(),
                       inStr.indexOf("</synthetical-root>") );

       return result.getBytes();
    }

    /**
     * Returns nested objects As Collection
     * after marshalling with implicit (Class reflection based) mapping 
     */

    public Collection getAsCollection(Class forClass) throws ObjectMarshalException
    {

       try {

           ObjectMarshaller unmarshaller = ObjectMarshallerFactory.getInstance().getMarshaller( MappingType.INTERNAL );
           unmarshaller.loadMapping( forClass );

           Document doc = Utils.createDocument();
           convert( new DOMResult( doc ) );

           return unmarshaller.unmarshal( doc );

        } catch (ObjectMappingException e) {

            throw new ObjectMarshalException("Mappling exception thrown ! Reason: " + e);

        } catch (MarshallerCreateException e) {

            throw new ObjectMarshalException("Can not create marshaller ! Reason: " + e);

        } catch (Exception e) {

            throw new ObjectMarshalException("UniFormTree.getAsCollection(Class forClass) Exception: " + e);

        } 

    }

    /**
     * Returns mapping for the own dom-tree 
     * with SyntheticalRoot as root and
     * returns nested objects As Abstract collection 
     */

    public Collection getAsCollection(Object mappingSource) throws ObjectMarshalException //, UniFormTransformationException
    {
        try {

            ObjectMarshaller unmarshaller = ObjectMarshallerFactory.getInstance().getMarshaller( MappingType.CUSTOM );
            unmarshaller.loadMapping( mappingSource );

            Document doc = Utils.createDocument();

            convert( new DOMResult( doc ) );

            return unmarshaller.unmarshal( doc );

        } catch (ObjectMappingException e) {

            throw new ObjectMarshalException("Mappling exception thrown ! Reason: " + e);

        } catch (MarshallerCreateException e) {

            throw new ObjectMarshalException("Can not create marshaller ! Reason: " + e);

        } catch (Exception e) {

            throw new ObjectMarshalException("UniFormTree.getAsCollection(Object mappingSource) Exception: " + e);

        } 
    }

    public NodeList getAsNodeList() {

       try {

           Document doc = Utils.createDocument();
           convert( new DOMResult( doc ) );

           return doc.getDocumentElement().getChildNodes();

        } catch (ParserConfigurationException e) {
            throw new RuntimeException("UniFormTree.getAsNodeList(): Can not create DOM Reason: " + e);
        }

    }


    /**
     * Is this object empty?
     * **/
    public boolean isEmpty()
    {
        return new String(getAsByteArray()).trim().length() == 0;
    }

    /* Debugging method... */ 
    public String getRawContent()
    {
        return thisStream.toString();
    }


}


