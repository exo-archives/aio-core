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
package org.exoplatform.services.resources;

import java.net.URL;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.Properties;

/**
 * A {@link ClassLoader} extension that will retrieve resources from the parent classloader. For each resource having a
 * ".properties" suffix it the classloader will try first to locate a corresponding resource using the same base name
 * but with an ".xml" suffix. If such a resource is found, it will be loaded using {@link XMLResourceBundleParser}
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class PropertiesClassLoader extends ClassLoader {

  public PropertiesClassLoader(ClassLoader parent) {
    super(parent);
  }

  public PropertiesClassLoader() {
  }

  protected URL findResource(String name) {
    if (name.endsWith(".properties")) {
      String xmlName = name.substring(0, name.length() - ".properties".length()) + ".xml";
      URL xmlURL = super.getResource(xmlName);
      if (xmlURL != null) {
        try {
          // Load XML
          InputStream in = xmlURL.openStream();
          Properties props = XMLResourceBundleParser.asProperties(in);

          // Now serizalize as binary
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          props.store(out, null);
          out.close();
          in = new ByteArrayInputStream(out.toByteArray());

          //
          return new URL(xmlURL, "", new InputStreamURLStreamHandler(in));
        }
        catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }
    }

    //
    return super.findResource(name);
  }
  
}
