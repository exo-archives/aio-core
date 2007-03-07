package org.exoplatform.services.xml.querying.impl.xtas.object.plugins;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.exoplatform.services.xml.querying.impl.xtas.object.ObjectMarshaller;
import org.exoplatform.services.xml.querying.impl.xtas.xml.Utils;
import org.exoplatform.services.xml.querying.object.ObjectMappingException;
import org.exoplatform.services.xml.querying.object.ObjectMarshalException;


import java.util.Collection;
import java.util.ArrayList;

/**
 * Marshaller with mapping based on java class intercaption
 * (internal mapping)
 * @version $Id: CastorClassMarshaller.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class CastorClassMarshaller implements ObjectMarshaller {

    private Class mapping;

    /**
     * Loads Class that will be introspected for mapping 
     */
    public void loadMapping(Object source) throws ObjectMappingException
    {
       if( source instanceof Class )
          // Explicit mapping
          this.mapping =  (Class) source;
       else
          throw new ObjectMappingException("ObjectMappingException: Data Source ("+source.getClass().getName()+") for mapping is invalid !");

    }

    /**
     * Does Castor specific marshalling 
     */
    public Document marshal(Object obj) throws ObjectMarshalException, ObjectMappingException
    {

       if( mapping == null )
            throw new ObjectMappingException("ObjectMarshaller's mapping can not be NULL. Call loadMapping() first ! ");

       try {

            Document doc = Utils.createDocument();
            Marshaller.marshal( obj, (Node) doc);

            return doc;

        } catch (Exception e) {
//            e.printStackTrace();
            throw new ObjectMarshalException("ObjectMarshalException occured! Reason: "+e.getMessage());

        }

    }

    /**
     * Does Castor specific unmarshalling 
     */
    public Collection unmarshal(Document source) throws  ObjectMarshalException, ObjectMappingException
    {
        if( mapping == null )
            throw new ObjectMappingException("ObjectMarshaller's mapping can not be NULL. Call loadMapping() first ! ");

        try {

             ArrayList list = new ArrayList();

            // Get List of ELEMENT! Nodes casted to forClass

            NodeList nl = source.getDocumentElement().getChildNodes();
            for (int i=0 ;i< nl.getLength(); i++) {
               Node child = nl.item(i);
               if( child.getNodeType() == Node.ELEMENT_NODE )
                   list.add(Unmarshaller.unmarshal( mapping, child ) );
            }

           return list;

        } catch (MarshalException e) {

            throw new ObjectMarshalException(e.getMessage());
        } catch (ValidationException e) {

            throw new ObjectMarshalException(e.getMessage());
        }

    }
}
