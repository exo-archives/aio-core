/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.resolving.impl.xmlcommons;

import java.util.Vector;
import java.io.IOException;
import java.io.File;

import org.xml.sax.EntityResolver;

import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.exoplatform.services.xml.resolving.XMLCatalogResolvingService;


/**
 * Created by The eXo Platform SARL        .
 *
 * XML Catalog resolving service - based on Apache XML commons Resolver.
 * is implementation of OASIS Entity Resolution Technical Committee definitions
 * and supports OASIS XML Catalogs, OASIS TR9401 Catalogs, XCatalogs (supported by Apache)
 * catalog formats. 
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: XMLCommonsResolvingServiceImpl.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class XMLCommonsResolvingServiceImpl implements XMLCatalogResolvingService 
{
   private CatalogResolver resolver;

   public XMLCommonsResolvingServiceImpl()
   {
      resolver = new CatalogResolver();
      String catalogs = System.getProperty("xml.catalog.files");
      if(catalogs == null) {
         Vector catalogFiles = new CatalogManager().getCatalogFiles();
         String files = "";
         for (int count = 0; count < catalogFiles.size(); count++) {
            String file = (String) catalogFiles.elementAt(count);
            if(files != "")
               files+=";"; 
            files+=file; 
         }
         System.setProperty("xml.catalog.files", files);
      }
   }

  /**
   * XmlResolvingService method
   * @return EntityResolver object
   */
   public EntityResolver getEntityResolver()
   {
       return resolver;
   }

  /**
   * @return is this PublicID resolvable locally?
   * @param publicId 
   */
   public boolean isLocallyResolvable(String publicId)
   {
      Catalog catalog = resolver.getCatalog();
      String result = null;
      try {
          result = catalog.resolveDoctype(null, publicId, null);
      } catch (Exception e) {
      }

      if(result == null)
         return false;
      else 
         return true;
   }

  /**
   * Adds new local-file based catalog
   * @param path - path+name to a local catalog
   */
   public void addCatalog(String path) throws IOException
   {
      if( ! new File(path).exists() )
         throw new IOException("XmlCommonsResolvingServiceImpl.addCatalog( "+path+") failed! Reason: file not found.");
      String catalogs = System.getProperty("xml.catalog.files");
      if(catalogs == null)
         catalogs = path;
      else 
         catalogs+=";"+path;
      System.setProperty("xml.catalog.files", path);
   }

}
