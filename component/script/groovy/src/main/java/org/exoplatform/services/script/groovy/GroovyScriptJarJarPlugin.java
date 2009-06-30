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
package org.exoplatform.services.script.groovy;

import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

/**
 * A plugin that retrieves a mapping from the init param named <i>mapping</i>. The param
 * is a multivalued string, each string has the format left->right where left and right
 * are package full qualified names.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class GroovyScriptJarJarPlugin extends BaseComponentPlugin {

  /** The mapping state. */
  private final Map<String, String> mapping = new HashMap<String, String>();

  /** Our logger. */
  private final Log log = ExoLogger.getLogger(GroovyScriptJarJarPlugin.class);

  @SuppressWarnings("unchecked")
  public GroovyScriptJarJarPlugin(InitParams params) {

    List values = params.getValuesParam("mapping").getValues();

    if (mapping == null) {
      log.warn("Was expecting a mapping init param");
    } else {
      for (Iterator i = values.iterator();i.hasNext();) {
        String rule = (String)i.next();

        String[] tmp = rule.split("\\-\\>");
        if (tmp.length == 2) {
          String left = tmp[0].trim();
          String right = tmp[1].trim();
          mapping.put(left, right);
          log.debug("Added mapping rule " + left + " -> " + right);
        } else {
          log.warn("Malformed mapping rule:" + rule);
        }
      }
    }
  }

  public Map<String, String> getMapping() {
    return mapping;
  }
}
