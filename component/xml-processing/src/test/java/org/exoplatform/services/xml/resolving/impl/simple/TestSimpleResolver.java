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

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.xml.BaseTest;
import org.exoplatform.services.xml.resolving.SimpleResolvingService;

/**
 * Created by the Exo Development team.
 */
public class TestSimpleResolver extends BaseTest {

  private SimpleResolvingService service;

  public void setUp() throws Exception {
    if (service == null) {
      PortalContainer manager = PortalContainer.getInstance();
      service = (SimpleResolvingService) manager
          .getComponentInstanceOfType(SimpleResolvingService.class);
    }
  }

  public void testLookupFailed() throws Exception {
      javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory
          .newInstance();
      factory.setNamespaceAware(true);
      javax.xml.parsers.SAXParser jaxpParser = factory.newSAXParser();
      org.xml.sax.XMLReader reader = jaxpParser.getXMLReader();

      reader.setEntityResolver(service.getEntityResolver());
      try {
        reader.parse(resourceURL("tmp/dtd-not-found.xml").getPath());
//            .toString());

      } catch (Throwable e) {
        return;
      }
      fail("Lookup should have been Failed as there is not such local DTD.");
  }

  public void testWebXmlResolving() throws Exception {
    try {

      javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory
          .newInstance();
      factory.setNamespaceAware(true);
      javax.xml.parsers.SAXParser jaxpParser = factory.newSAXParser();
      org.xml.sax.XMLReader reader = jaxpParser.getXMLReader();

      reader.setEntityResolver(service.getEntityResolver());
      reader.parse(resourceURL("web.xml").toString());

    } catch (Exception e) {

      fail("testWebXmlResolving() ERROR: " + e.toString());
    }

  }

}
