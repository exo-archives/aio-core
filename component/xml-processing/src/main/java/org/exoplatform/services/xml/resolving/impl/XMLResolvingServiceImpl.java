/**
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

package org.exoplatform.services.xml.resolving.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.xml.resolving.XMLResolvingService;
import org.xml.sax.EntityResolver;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class XMLResolvingServiceImpl implements XMLResolvingService {
  
  private static final Log LOGGER = ExoLogger.getLogger("XMLResolvingServiceImpl");
  private Map<String, String> publicIDs_ = new HashMap<String, String>();
  private Map<String, String> systemIDs_ = new HashMap<String, String>();
  
  public XMLResolvingServiceImpl() {
  }
  
  public EntityResolver getEntityResolver() {
    return new XMLResolver(publicIDs_, systemIDs_);
  }
  
  private void addEntityPublicID(String publicId, String uri) {
    if (publicIDs_.get(publicId) != null)
      throw new IllegalArgumentException("Entity whith publicId "
      		+ publicId + " already exists.");
    publicIDs_.put(publicId, uri);
    LOGGER.info("New entries to ResolvingService added (public) : "
        + publicId + " : " + uri);
  }

  private void addEntitySystemID(String systemId, String uri) {
    if (systemIDs_.get(systemId) != null)
      throw new IllegalArgumentException("Entity whith systemId "
            + systemId + " already exists.");
    systemIDs_.put(systemId, uri);
    LOGGER.info("New entries to ResolvingService added (system) : "
        + systemId + " : " + uri);
  }

  public void addPlugin(ComponentPlugin plugin) {
    if (plugin instanceof AddXMLResolvingContextPlugin) {
      AddXMLResolvingContextPlugin resolvingContextPlugin = (AddXMLResolvingContextPlugin) plugin;
      Map<String, String> t = resolvingContextPlugin.getPublicIDsResolvingtable();
      Set<String> keys = t.keySet();
      for (String key : keys) {
        addEntityPublicID(key, t.get(key));
      }
      t = resolvingContextPlugin.getSystemIDsResolvingtable();
      keys = t.keySet();
      for (String key : keys) {
        addEntitySystemID(key, t.get(key));
      }
    }
  }

}

