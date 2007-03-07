package org.exoplatform.services.xml.querying.impl.xtas.resource;

import java.io.IOException;
import org.exoplatform.services.xml.querying.ConfigException;
import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.XMLWellFormedData;

/**
 * Abstract class encapsulated Persistence storage for the XML data 
 * @version $Id: Resource.java 5799 2006-05-28 17:55:42Z geaz $
 */
abstract public class Resource
{
    protected String resourceId;

    /**
     * Loads XML from some resource 
     */
    public abstract XMLWellFormedData load() throws UniFormTransformationException, IOException ;

    /**
     * Stores XML into some resource 
     */
    public abstract void save(XMLWellFormedData tree) throws UniFormTransformationException, IOException ;

    /**
     * Creates resource 
     */
    public abstract void create(XMLWellFormedData initTree) throws IOException ;

    /**
     * drops resource 
     */
    public abstract void drop() throws IOException;

    /**
     * Initializes Resource's context (if any)
     */
    public abstract void setContext(Object context);

    /**
     * Initializes Resource
     */
    public void init(String resourceId) throws ConfigException
    {
       this.resourceId = resourceId;
    }

    public String toString()
    {
       return this.resourceId;
    }

    /**
     * Returns first element will be insert to any query on this resource
     */
    public String getXPathPrefix()
    {
       return null;
    }

    /**
     * Closes Resource
     */
    public void close()
    {
    }

}
