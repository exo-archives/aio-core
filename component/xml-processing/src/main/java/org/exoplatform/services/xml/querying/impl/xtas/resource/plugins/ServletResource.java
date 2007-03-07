package org.exoplatform.services.xml.querying.impl.xtas.resource.plugins;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;


import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.XMLWellFormedData;
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.resource.Resource;
import org.xml.sax.InputSource;

import javax.servlet.ServletContext;

/**
 * Represents XML resource mapped to a ServletContext's specified path as XTAS resource
 * @version $Id: ServletResource.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
public class ServletResource extends Resource {

    private static String SERVLET_RESOURCE = "servlet:/";
    private ServletContext context = null;


    public ServletResource() 
    {
    }

    /**
     * Inits resource named <code> resourceId </code> 
     */
    public void init(String resourceId) 
    {
       this.resourceId = resourceId.substring(SERVLET_RESOURCE.length());
//       this.resourceId = resourceId;
    }

    public void setContext(Object context) 
    {
       if( !(context instanceof ServletContext) )
         throw new ClassCastException("ServletResource.setContext():"+context.getClass().getName()+" not subclass of javax.servlet.ServletContext!");
       this.context = (ServletContext) context;


    }

    /**
     * Loads XML from the Servlet Resource
     */
    public XMLWellFormedData load() throws UniFormTransformationException, IOException
    {

//        URL path = context.getResource(resourceId);

        try {

            WellFormedUniFormTree tree = new WellFormedUniFormTree ();
            tree.init ( new InputSource (getContext().getRealPath(resourceId)) ); 

            return tree;

        } catch (Exception e) {

            throw new UniFormTransformationException("ServletResource: Can not create WellFormedUniFormTree (XML) Reason: " + e);
        }
    }

    public void save(XMLWellFormedData tree) throws IOException
    {


        FileOutputStream fos = new FileOutputStream( getContext().getRealPath(resourceId) );

        byte[] b = tree.getAsByteArray();

        fos.write( b, 0, b.length );

        fos.flush();
        fos.close();

    }

    /**
     * Creates resource by Id 
     */
    public void create(XMLWellFormedData initTree) throws IOException
    {

        File res = new File (getContext().getRealPath(resourceId));

        if( !res.createNewFile() )
            throw new IOException (" Resource '"+context.getResource(resourceId).toString()+"' already exists.");

//        save( initTree );

    }

    /**
     * drops resource 
     */
    public void drop() throws IOException
    {
//        File res = new File (resourceId);

        File res = new File (getContext().getRealPath(resourceId));

        if( !res.delete() )
            throw new IOException (" Resource '"+context.getResource(resourceId).toString()+"' can not be deleted.");

//            throw new IOException (" File '"+res.getAbsolutePath()+"' can not be deleted.");
    }

    private ServletContext getContext() throws IOException
    {
       if( context == null )
          throw new IOException("ServletContext in ServletResource '"+resourceId+"' IS NULL!");
       return context;
    }

}
