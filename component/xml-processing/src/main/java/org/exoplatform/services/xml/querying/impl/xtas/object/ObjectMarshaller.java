package org.exoplatform.services.xml.querying.impl.xtas.object;

import org.exoplatform.services.xml.querying.object.ObjectMappingException;
import org.exoplatform.services.xml.querying.object.ObjectMarshalException;
import org.w3c.dom.Document;
import java.util.Collection;

/**
 * Abstract Java-Object/ XML marshaller/unmarshaller
 * Every concrete marshaller must implement this.
 * @version $Id: ObjectMarshaller.java 5799 2006-05-28 17:55:42Z geaz $ */
public interface ObjectMarshaller
{

    /**
     * Loads mapping for marshalling/unmarshalling.
     * As it is does not be realised for now constraints for 
     * mapping's format - incoming object is ANY subclass 
     * of Java Object
     */
    void loadMapping(Object source) throws ObjectMappingException ;

    /**
     * Unmarshals XML (as DOM Document) to Collection of Objects using loaded mapping
     * Note: the loadMapping() must be called first
     */
    Collection unmarshal(Document source) throws  ObjectMarshalException, ObjectMappingException;

    /**
     * Marshals Object to XML (as DOM Document) using loaded mapping
     * Note: the loadMapping() must be called first
     */
    Document marshal(Object obj) throws ObjectMarshalException, ObjectMappingException;

}
