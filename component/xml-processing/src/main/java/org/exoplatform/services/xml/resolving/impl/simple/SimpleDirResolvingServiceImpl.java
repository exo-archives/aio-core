/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.xml.resolving.impl.simple;

import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.xml.resolving.SimpleResolvingService;

/**
 * Created by The eXo Platform SAS        .
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
