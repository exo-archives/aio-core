/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.resolving.impl.simple;

import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.xml.resolving.SimpleResolvingService;

/**
 * Created by The eXo Platform SARL        .
 *
 * Simple Catalog resolving service - all DTDs
 * in one local directory (/dtd).
 * Resolver just checks DTD file name.
 * To add new - just copy *.dtd to /dtd directory and rebuild service
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: SimpleDirResolvingServiceImpl.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class SimpleDirResolvingServiceImpl implements SimpleResolvingService
{
   private static final String DIR_NAME = "/dtd";
   private String dtdName;
   private Log log;

   public SimpleDirResolvingServiceImpl(LogService logService)
   {
        log = logService.getLog("SimpleDirResolvingServiceImpl");
   }

  /**
   * XmlResolvingService method
   * @return EntityResolver object if found or null (systemId will be used)
   */

   public EntityResolver getEntityResolver()
   {
       try {

          EntityResolver resolver = new SimpleResolver(DIR_NAME);
//          log.debug("resolver is null-"+(resolver==null));
//          if(resolver == null)
//             log.info("Local entity definitions not found in <"+DIR_NAME+">");
//          else
//             log.info("Local entity definitions found in <"+DIR_NAME+">");

          return resolver;

       } catch (Exception e) {
          log.info("Error on get SimpleResolver",e);
          return null;
       }
   }

}
