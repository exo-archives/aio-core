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

import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.PropertiesParam;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class TRAXTemplatesLoaderPlugin extends BaseComponentPlugin {

  private Map<String, String> templates_ = new HashMap<String, String>();

  public TRAXTemplatesLoaderPlugin(InitParams params) throws Exception {
    if (params != null) {
      PropertiesParam pparams = params.getPropertiesParam("xsl-source-urls");
      if (pparams != null)
        templates_ = pparams.getProperties();
    }
  }

  public Map<String, String> getTRAXTemplates() {
    return templates_;
  }

}
