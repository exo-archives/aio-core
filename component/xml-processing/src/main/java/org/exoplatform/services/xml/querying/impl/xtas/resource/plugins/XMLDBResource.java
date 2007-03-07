package org.exoplatform.services.xml.querying.impl.xtas.resource.plugins;

import java.net.MalformedURLException;

import java.io.IOException;
import java.io.ByteArrayInputStream;


import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.XMLWellFormedData;
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.resource.Resource;
import org.xml.sax.InputSource;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Collection;


/**
 * Represents XML resource mapped to XML:DB compatible database (see www.xmldb.org)
 * Such as Apache's Xindice or eXist(exist-db.org)
 * NOTE: We use XMLDB collection or resource to execute XTAS query on it,
 * we does not use query related (XUpdate or XMLDB-XPath) services... 
 * URL have to look like: xmldb:database-id://host-address-part/db/collection#resourceId
 * @version $Id: XMLDBResource.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
public class XMLDBResource extends Resource {


    // XMLDB Database defined as Resource's context
    private Database context = null;

    private String xmldbCollectionId = null;
    private String xmldbResourceId = null;

    /**
     * Creates resource named <code> resourceId </code> 
     */
    public XMLDBResource(String resourceId) throws MalformedURLException
    {
       this();
       this.resourceId = resourceId;
    }

    public XMLDBResource() throws MalformedURLException
    {
       int resIndex = resourceId.indexOf("#")+1;
       if (resIndex > 0)
          xmldbResourceId = resourceId.substring(resIndex);
       else
          throw new MalformedURLException("Could not create XMLDBResource. Indefinite XML:DB resource-id (#resourceId) in'"+resourceId+"'");

       xmldbCollectionId = resourceId.substring(0,resIndex-1);

    }

    public void setContext(Object context) 
    {

       if( context instanceof Database )
          this.context = (Database) context;
/*
       else if( context instanceof String ) {

          Class cl = Class.forName((String) context);			
          this.context = (Database)cl.newInstance();
          DatabaseManager.registerDatabase(this.context);
}*/

        else
          throw new ClassCastException("XmlDbResource.setContext():"+context.getClass().getName()+" not subclass of org.xmldb.api.base.Database!");

    }

    /**
     * Loads XML from the Servlet Resource
     */
    public XMLWellFormedData load() throws UniFormTransformationException, IOException
    {

        try {

            Collection col = DatabaseManager.getCollection(xmldbCollectionId);

            XMLResource res = (XMLResource)col.getResource(xmldbResourceId);

            if (res == null)
               throw new Exception("XMLDB:XMLResource <"+xmldbResourceId+"> not found");

            String content = (String) res.getContent();

            WellFormedUniFormTree tree = new WellFormedUniFormTree ();
            tree.init ( new InputSource(new ByteArrayInputStream(content.getBytes()) )  );

            return tree;

        } catch (Exception e) {

            throw new UniFormTransformationException("XmldbResource: Can not create WellFormedUniFormTree (XML) Reason: " + e);
        }
    }

    public void save(XMLWellFormedData tree) throws IOException
    {
        try {

           Collection col = DatabaseManager.getCollection(xmldbCollectionId);
           String content = tree.getAsString();

           XMLResource res=(XMLResource)col.getResource(xmldbResourceId);

           if (res == null)
               throw new Exception("XMLDB:XMLResource <"+xmldbResourceId+"> not found!");

           res.setContent(content);
           col.storeResource( res );

        } catch (Exception e) {

            throw new IOException("XmldbResource: Can not save WellFormedUniFormTree (XML) Reason: " + e);
        }

    }

    /**
     * Creates resource by Id 
     */
    public void create(XMLWellFormedData initTree) throws IOException
    {

        try {

           Collection col = DatabaseManager.getCollection(xmldbCollectionId);

           // For XML Resource only!
           if ((XMLResource)col.getResource(xmldbResourceId) != null)
               throw new Exception("XMLDB:XMLResource <"+xmldbResourceId+"> already exist!");

           XMLResource res = (XMLResource)col.createResource(xmldbResourceId,"XMLResource");
           res.setContent("<null/>");

           col.storeResource( res );


        } catch (Exception e) {

            throw new IOException("XmldbResource: Can not create Resource:"+resourceId+ " Reason: " + e);
        }

    }

    /**
     * drops resource 
     */
    public void drop() throws IOException
    {
        try {

           Collection col = DatabaseManager.getCollection(xmldbCollectionId);
           Resource res=(Resource)col.getResource(xmldbResourceId);

           if (res == null)
               throw new Exception("XMLDB:XMLResource <"+xmldbResourceId+"> not found!");

           col.removeResource(col.getResource(xmldbResourceId));

        } catch (Exception e) {

            throw new IOException("XmldbResource: Can not drop Resource:"+resourceId+ " Reason: " + e);
        }
    }

}
