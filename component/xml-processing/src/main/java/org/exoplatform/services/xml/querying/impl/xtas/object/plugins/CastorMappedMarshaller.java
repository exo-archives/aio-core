package org.exoplatform.services.xml.querying.impl.xtas.object.plugins;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.URL;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.exoplatform.services.xml.querying.Statement;
import org.exoplatform.services.xml.querying.impl.xtas.Query;
import org.exoplatform.services.xml.querying.impl.xtas.QueryType;
import org.exoplatform.services.xml.querying.impl.xtas.SimpleStatement;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormConverter;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormTreeFragment;
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.object.ObjectMarshaller;
import org.exoplatform.services.xml.querying.impl.xtas.xml.Utils;
import org.exoplatform.services.xml.querying.object.ObjectMappingException;
import org.exoplatform.services.xml.querying.object.ObjectMarshalException;

import org.xml.sax.InputSource;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import java.util.Collection;
import java.util.Iterator;


/**
 * Exolab's Castor XML implementation of ObjectMarshaller
 * based on Castor mapping (custom mapping)
 * @version $Id: CastorMappedMarshaller.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class CastorMappedMarshaller implements ObjectMarshaller {

    private Mapping mapping;

    /** @link dependency 
     * @stereotype use*/
    /*# SyntheticalRoot lnkSyntheticalRoot; */

    public CastorMappedMarshaller()
    {
    }

    /**
     * Loads mapping
     * Castor Mapping supports 3 formats of incoming data: 
     * sax.InputSource, String (with URL) and URL
     * contains mapping XML
     * Every castor based custom mapping has structure like:
     * <p>&lt;mapping><br>
     *   &lt;!-- Standard part for root xtas.object.plugins.SyntheticalRoot class ! --><br>
     *   &lt;class name="xtas.object.plugins.SyntheticalRoot"><br>
     *          &lt;map-to xml="synthetical-root"/><br>
     *          &lt;field name="result"<br>
     *                  collection="collection"><br>
     *                  &lt;!-- Put your mapping for the top-level custom beans here ! --><br>
     *                  &lt;bind-xml name=&quot;<b>top-level-custom-bean-name</b>&quot;/&gt;<br>
     *                  &lt;!-- --><br>
     *          &lt;/field><br>
     *   &lt;/class><br>
     *   &lt;!-- Put your beans' descriptions here --><br>
     *   &lt;class name=&quot;<b>custom-package.CustomBeanName</b>&quot;&gt;<br>
     *     &lt;map-to xml=&quot;<b>top-level- custom-bean-name</b>&quot;/&gt;<br>
     *     &lt;field name=&quot;<b>field-name</b>&quot; type=&quot;<b>field-type</b>&quot; ><br>
     *          &lt;bind-xml name=&quot;<b>field-name</b>&quot; node=&quot;<b>attribute|element</b>&quot;/&gt;<br>
     *     &lt;/field><br>
     * ....<br>
     *   &lt;/class><br>
     * &lt;/mapping><br>
     * See Castor-XML documentation for details.
     */
    public void loadMapping(Object source) throws ObjectMappingException
    {

       this.mapping = new Mapping( this.getClass().getClassLoader() );

       InputSource src;

       try {
           if( source instanceof InputSource ) {
                src = (InputSource) source;
           }
           else if( source instanceof String ) {
                src = new InputSource( (String) source );
           }
           else if( source instanceof URL ) {
                src = new InputSource( ((URL) source).toString() );
           }
           else
             throw new ObjectMappingException("ObjectMappingException: Data Source ("+source.getClass().getName()+") for mapping is invalid !");

           mapping.loadMapping( createMapping(src) );

        } catch (IOException e) {

            throw new ObjectMappingException("ObjectMappingException (I/O) thrown! Reason: "+e.getMessage());

        } catch (MappingException e) {

            throw new ObjectMappingException("ObjectMappingException thrown! Reason: "+e.getMessage());

        }

    }

    /**
     * does Castor specific marshalling 
     */
    public Document marshal(Object obj) throws ObjectMarshalException, ObjectMappingException
    {

       if( mapping == null )
            throw new ObjectMappingException("ObjectMarshaller's mapping can not be NULL. Call loadMapping() first ! ");

       try {

            Document doc = Utils.createDocument();
            Marshaller marshaller = new Marshaller(doc);
            marshaller.setMapping( mapping );
            marshaller.marshal(obj);
            return doc;

        } catch (Exception e) {

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

             Unmarshaller unmar = new Unmarshaller(mapping);
             SyntheticalRoot oo = (SyntheticalRoot) unmar.unmarshal( source );
             return oo.getObjects();

        } catch (MappingException e) {

            throw new ObjectMappingException(e.getMessage());
       } catch (MarshalException e) {

            throw new ObjectMarshalException(e.getMessage());
       } catch (ValidationException e) {

            throw new ObjectMarshalException(e.getMessage());
       }


    }

    private InputSource createMapping(InputSource source)  throws ObjectMappingException
    {

     try {

        WellFormedUniFormTree mapTree =  new WellFormedUniFormTree ();
        mapTree.init( source ); 

        // Get Low-lewel class (will be only one!)
        Query q1 = new Query(SimpleStatement.select(
              "/mapping/class/map-to"
                 ,source.getSystemId() ));
        q1.execute(); 
        UniFormTreeFragment f = UniFormConverter.toFragment((UniFormTree)q1.getResult());
        Iterator elements = f.getAsCollection(CastorMapToElement.class).iterator();

        // Prepare SyntheticalRoot to add to the Mapping
        String str = 
               "<class name=\"org.exoplatform.services.xml.querying.impl.xtas.object.plugins.SyntheticalRoot\"><map-to xml=\"synthetical-root\"/><field name=\"objects\" collection=\"collection\">";
        String name = ((CastorMapToElement)elements.next()).getXml();
        str += "<bind-xml name=\""+name+"\"/>";
        str+="</field></class>";

        // Add SyntheticalRoot collection 
        Statement add = new SimpleStatement( QueryType.APPEND, "mapping/class[last()]", str );
        Query q = new Query(add);
        q.setInputStream( mapTree.getAsInputStream() ); 
        q.execute();

        return new InputSource( q.getResult().getAsInputStream() );

     } catch (Exception e) {

        throw new ObjectMappingException("Exception while create mapping thrown! Reason: "+e.getMessage());

     }
   }

}
