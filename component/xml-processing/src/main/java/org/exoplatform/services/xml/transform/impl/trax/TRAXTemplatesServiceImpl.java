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

package org.exoplatform.services.xml.transform.impl.trax;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.xml.transform.NotSupportedIOTypeException;
import org.exoplatform.services.xml.transform.trax.TRAXTemplates;
import org.exoplatform.services.xml.transform.trax.TRAXTemplatesService;
import org.exoplatform.services.xml.transform.trax.TRAXTransformerService;
import org.picocontainer.Startable;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class TRAXTemplatesServiceImpl implements TRAXTemplatesService, Startable {
  
  private static final Log LOGGER = ExoLogger.getLogger("TRAXTemplatesServiceImpl");
  private Map<String, TRAXTemplates> templates_;
  private TRAXTransformerService traxTransformerService_;
  
  public TRAXTemplatesServiceImpl(TRAXTransformerService traxTransformerService) {
    traxTransformerService_ = traxTransformerService;
    templates_ = new HashMap<String, TRAXTemplates>();
  }
  
  /* (non-Javadoc)
   * @see org.exoplatform.services.xml.transform.trax.TRAXTemplatesService#getTemplates(
   * java.lang.String)
   */
  public TRAXTemplates getTemplates(String key) {
    return templates_.get(key);
  }
  
  /* (non-Javadoc)
   * @see org.exoplatform.services.xml.transform.trax.TRAXTemplatesService#addTRAXTemplates(
   * java.lang.String, org.exoplatform.services.xml.transform.trax.TRAXTemplates)
   */
  public void addTRAXTemplates(String key,
      TRAXTemplates templates) throws IllegalArgumentException {
    if (templates_.get(key) != null) {
      throw new IllegalArgumentException("Templates with key '" + key + "' already exists!");
    }
    templates_.put(key, templates);
  }
  
  /* (non-Javadoc)
   * @see org.exoplatform.services.xml.transform.trax.TRAXTemplatesService#addTRAXTemplates(
   * java.lang.String, javax.xml.transform.Source)
   */
  public void addTRAXTemplates(String key,
      Source source) throws IllegalArgumentException {
    if (templates_.get(key) != null) {
      throw new IllegalArgumentException("Templates with key '" + key + "' already exists!");
    } try {
      templates_.put(key, traxTransformerService_.getTemplates(source));
    } catch (NotSupportedIOTypeException e) {
      throw new IllegalArgumentException("Source has unsupported context." + e);
    } catch (TransformerException e) {
      throw new IllegalArgumentException("Can't get templates from source." + e);
    }
  }

  public void addPlugin(ComponentPlugin plugin) {
    if (plugin instanceof TRAXTemplatesLoaderPlugin) {
      Map<String, String> m = ((TRAXTemplatesLoaderPlugin) plugin).getTRAXTemplates();
      Set<String> keys = m.keySet();
      for (String key : keys) {
        String xsltSchema = m.get(key);
        try {
          if (Thread.currentThread().getContextClassLoader().getResource(xsltSchema) != null) {
            LOGGER.info("XSLT schema found by relative path: " + xsltSchema);
            addTRAXTemplates(key, traxTransformerService_.getTemplates(new StreamSource(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(xsltSchema))));
          } else 
            LOGGER.error("XSLT schema not found: " + xsltSchema);
         } catch(Exception e) {
           LOGGER.error("Add new TRAXTemplates failed : " + e);
         }
      }
    }
  }
  // ------ Startable -------
  
  /* (non-Javadoc)
   * @see org.picocontainer.Startable#start()
   */
  public void start(){
  }
  
  /* (non-Javadoc)
   * @see org.picocontainer.Startable#stop()
   */
  public void stop() {
  }

}

