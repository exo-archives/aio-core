package org.exoplatform.services.xml.querying;

import org.exoplatform.services.xml.querying.object.MarshallerCreateException;
import org.exoplatform.services.xml.querying.object.ObjectMappingException;
import org.exoplatform.services.xml.querying.object.ObjectMarshalException;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.Source;
import javax.xml.transform.Result;

public interface XMLWellFormedData extends XMLData {
    /**
     * PublicId for output
     */
    void setDTDPublicId(String DTDPublicId);

    /**
     * SystemId for output
     */
    void setDTDSystemId(String DTDSystemId);

    /**
     * This object as ByteArray (is required by superclass)
     */
    byte[] getAsByteArray();

    /**
     * This object as DOM Node
     */
    Node getAsDOM();

    /**
     * Will output be indent?
     */
    void setIndentOutput(boolean indentOutput);

    /**
     * Will output be wit the xml declaration?
     */
    void setOmitXmlDeclaration(boolean omitXmlDeclaration);

    /**
     * Will this object be validated?
     */
    void setValidate(boolean validate);

    /**
     * Marshalls Java object using <code> mapping </code> and creates UniFormTree object
     */
    void init(Object mapping, Object obj) throws ObjectMarshalException, MarshallerCreateException, ObjectMappingException;

    /**
     * Marshalls Java object using obj.getClass() as mapping and creates UniFormTree object
     * WARNING: Will be used for all objects except  NodeList, InputStream, DOM Document, InputSource
     */
    void init(Object obj) throws ObjectMarshalException, MarshallerCreateException, ObjectMappingException;

    /**
     * Initializes this with the DOM's Node 
     */
    void init(Node node) throws UniFormTransformationException;

    /**
     * Initializes this with the SAX's InputSource 
     */
    void init(InputSource src) throws UniFormTransformationException;

    /**
     * Initializes with root node 
     * Note: it is impossible to insert without child
     * adding " " for as a text child - Statement.append("root/text()",.....)
     */
    void initRoot(String nodeName) throws UniFormTransformationException;

    /**
     * Parse this XML using ContentHandler
     */
    void processAsSAX(ContentHandler handler) throws SAXException;

    /**
     * Parse this XML using ContentHandler
     */
    void transformWithXSL(Result result, Source xslTemplate) throws TransformerException;
}
