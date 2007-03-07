package org.exoplatform.services.xml.querying.impl.xtas.object;
import org.exoplatform.services.xml.querying.object.MarshallerCreateException;

/**
 * Object Factory for ObjectMarshaller creating
 * Analyzes <code> xtas.marshaller.internal </code>
 * or <code> xtas.marshaller.custom </code> system property 
 * depending on type of mapping.
 * @version $Id: ObjectMarshallerFactory.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class ObjectMarshallerFactory {

    static {
        System.setProperty("xtas.marshaller.internal", "org.exoplatform.services.xml.querying.impl.xtas.object.plugins.CastorClassMarshaller");
        System.setProperty("xtas.marshaller.custom", "org.exoplatform.services.xml.querying.impl.xtas.object.plugins.CastorMappedMarshaller");
    }                                                                

    /** @link dependency 
     * @stereotype instantiate*/
    /*# ObjectMarshaller lnkObjectMarshaller; */

    /** @link dependency 
     * @stereotype use*/
    /*# MappingType lnkMappingType; */

    public static ObjectMarshallerFactory getInstance()
    {
        return new ObjectMarshallerFactory();
    }

    /**
     * ObjectMarshaller instantiator
     * Returns implementation of ObjectMarshaller
     * for INTERNAL=0 (for Java Class introspection)
     * or CUSTOM=1
     * implementations of XML/Java mapping 
     */
    public ObjectMarshaller getMarshaller( int mappingType ) throws MarshallerCreateException
    {
        String implProperty;

        if( mappingType == MappingType.CUSTOM )
            implProperty = "xtas.marshaller.custom";
        else if ( mappingType == MappingType.INTERNAL )
            implProperty = "xtas.marshaller.internal";
        else
            throw new MarshallerCreateException("Mapping Type is invalid! ");

        try {

            ObjectMarshaller impl = (ObjectMarshaller)Class.forName( System.getProperty( implProperty ) ).newInstance();
            return impl;

        } catch (Exception e) {
//            e.printStackTrace();
            throw new MarshallerCreateException(e.getMessage());
        }
    }

}
