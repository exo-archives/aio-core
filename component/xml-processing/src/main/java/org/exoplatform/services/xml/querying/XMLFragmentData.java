package org.exoplatform.services.xml.querying;

import java.util.Collection;
import java.io.InputStream;
import org.exoplatform.services.xml.querying.object.ObjectMarshalException;
import org.w3c.dom.NodeList;

 
public interface XMLFragmentData extends XMLData {
    /**
     * Returns mapping for the own dom-tree 
     * with SyntheticalRoot as root and
     * returns nested objects As Abstract collection 
     */
    Collection getAsCollection(Object mappingSource) throws ObjectMarshalException;

    /**
     * Returns nested objects As Collection
     * after marshalling with implicit (Class reflection based) mapping 
     */
    Collection getAsCollection(Class forClass) throws ObjectMarshalException;

    /**
     * Initializes this with the DOM NodeList 
     * DOES NOT WORK properly!
     */
//    void init(NodeList list) throws UniFormTransformationException;

    /**
     * Initializes this with the io.InputStream 
     */
    void init(InputStream stream) throws UniFormTransformationException;

    byte[] getAsByteArray();

    NodeList getAsNodeList();

    /**
     * Is this object empty?
     * **/
    boolean isEmpty();
}
