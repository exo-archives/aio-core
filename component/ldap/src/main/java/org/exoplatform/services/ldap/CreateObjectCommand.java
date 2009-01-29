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
package org.exoplatform.services.ldap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapContext;

import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.PropertiesParam;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Oct 16, 2005
 */
public class CreateObjectCommand extends BaseComponentPlugin {
  private Map<String, BasicAttributes> objects_;

  public CreateObjectCommand(InitParams params) {
    objects_ = new HashMap<String, BasicAttributes>();
    Iterator i = params.getPropertiesParamIterator();
    while (i.hasNext()) {
      PropertiesParam param = (PropertiesParam) i.next();
      Map<String, String> prop = param.getProperties();
      BasicAttributes attrs = new BasicAttributes();
      Iterator entries = prop.entrySet().iterator();
      while (entries.hasNext()) {
        Map.Entry entry = (Map.Entry) entries.next();
        String key = (String) entry.getKey();
        String value = (String) entry.getValue();
        Attribute attr = attrs.get(key);
        if (attr == null)
          attrs.put(new BasicAttribute(key, value));
        else
          attr.add(value);
      }
      objects_.put(param.getName(), attrs);
    }
  }

  public void addObjects(LdapContext context) {
    Iterator i = objects_.entrySet().iterator();
    while (i.hasNext()) {
      Map.Entry entry = (Map.Entry) i.next();
      String dn = (String) entry.getKey();
      Attributes attrs = (Attributes) entry.getValue();
      try {
        context.createSubcontext(dn, attrs);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
