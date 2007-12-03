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
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogService;
import org.exoplatform.container.ExoContainerContext;


/**
 * Created by The eXo Platform SAS        .
 *
 * Entity Resolver for SimpleDir resolving service
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: SimpleResolver.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class SimpleResolver implements EntityResolver{
   private String localPath;
   private Log log;

   public SimpleResolver(String localPath)
   {
      this.localPath = localPath;

      LogService logService = (LogService) ExoContainerContext.getTopContainer().
                              getComponentInstanceOfType(LogService.class);
      log = logService.getLog(this.getClass());
   }

   public InputSource resolveEntity (String publicId, String systemId) throws IOException
   {
      log.debug("query for resolve entity publicId["+publicId+" systemId["+systemId+"]");
      int fileIndex = systemId.lastIndexOf('/');
      if(fileIndex == -1)
          return null;

      String dtdPath = localPath+systemId.substring(fileIndex);
      log.debug("local path is ["+dtdPath+"]");

      if(this.getClass().getResource(dtdPath) == null) {
          log.warn("Local entity definitions of '"+dtdPath+" not found in catalog. Trying to load from "+systemId+"..");
          return null;
      }
      InputSource source = new InputSource(this.getClass().getResourceAsStream(dtdPath));
      log.debug("Local entity definitions found in '" + dtdPath + "'");

      source.setSystemId(systemId);
      return source;
   }
}

