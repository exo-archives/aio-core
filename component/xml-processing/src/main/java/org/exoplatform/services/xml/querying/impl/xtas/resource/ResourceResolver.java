package org.exoplatform.services.xml.querying.impl.xtas.resource;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Iterator;
import org.exoplatform.services.xml.querying.ConfigException;
import org.exoplatform.services.xml.querying.impl.xtas.XMLConfig;
import org.exoplatform.services.xml.querying.impl.xtas.resource.plugins.LocalFile;


/**
 * Singleton for finding and instantiate 
 * XML resource by its ID (fileName, URL etc) 
 * @version $Id: ResourceResolver.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class ResourceResolver {

    private static ResourceResolver resolver = null;
    private Collection descriptors;

    /** @link dependency 
     * @stereotype instantiate*/
    /*# Resource lnkResource; */

    protected ResourceResolver() throws ConfigException
    {
       descriptors = XMLConfig.getInstance().getResources();
    }

    public static ResourceResolver getInstance() throws ConfigException
    {
        if (resolver == null) {
            resolver = new ResourceResolver();
        }
        return resolver;
    }

    /**
     * Resolves resource by ID 
     */
    public Resource getResource(String resourceId) throws MalformedURLException, ConfigException
    {

        Iterator iter = descriptors.iterator();
        Resource res = null;

        while(iter.hasNext()) {
            ResourceDescriptor descr = (ResourceDescriptor) iter.next();
            String prefix = descr.getPrefix(); 

            if(prefix != null && prefix.length() <= resourceId.length()){

              if ( prefix.equals( resourceId.substring( 0, prefix.length() ) ) ) {
                  try {
                     res = (Resource)Class.forName(descr.getClassname()).newInstance();
                  } catch (Exception e) {
                     throw new ConfigException("XTAS Resource Config Exception - can not instantiate: "+e.getMessage());
                  }
                  res.init( resourceId );
                  return res;
              }
            }
        }
/*
        res = new LocalFile();
        res.init(resourceId);
        return res;
*/
        return new LocalFile( resourceId );
    }
}
