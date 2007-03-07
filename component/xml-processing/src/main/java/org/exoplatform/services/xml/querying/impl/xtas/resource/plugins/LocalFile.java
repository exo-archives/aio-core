package org.exoplatform.services.xml.querying.impl.xtas.resource.plugins;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.XMLWellFormedData;
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.resource.Resource;
import org.xml.sax.InputSource;

/**
 * Represents XML local file as XTAS resource
 * @version $Id: LocalFile.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
public class LocalFile extends Resource {
    /**
     * Creates resource as local file named <code> resourceId </code> 
     */
    public LocalFile(String resourceId) 
    {
       this.resourceId = resourceId;
    }

    public LocalFile() 
    {
    }

    public void setContext(Object context) 
    {
    }

    /**
     * Loads XML from the local file 
     * Will not validate... 
     */
    public XMLWellFormedData load() throws UniFormTransformationException, IOException
    {

        File file = new File(resourceId);

        try {

            WellFormedUniFormTree tree = new WellFormedUniFormTree ();
            tree.init ( new InputSource (file.getCanonicalPath()) ); 

            return tree;

        } catch (Exception e) {

            throw new UniFormTransformationException("Can not create WellFormedUniFormTree (XML) Reason: " + e);
        }
    }

    public void save(XMLWellFormedData tree) throws IOException
    {


        FileOutputStream fos = new FileOutputStream( resourceId );

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

        File res = new File (resourceId);
        if( !res.createNewFile() )
            throw new IOException (" File '"+res.getAbsolutePath()+"' already exists.");

//        save( initTree );

    }

    /**
     * drops resource 
     */
    public void drop() throws IOException
    {
        File res = new File (resourceId);
        if( !res.delete() )
            throw new IOException (" File '"+res.getAbsolutePath()+"' can not be deleted.");
    }


}
