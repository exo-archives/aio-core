/**
 * Copyright 2001-2007 The eXo platform SAS All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.xml.resolving.impl.simple;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.xml.resolving.SimpleResolvingService;
import junit.framework.TestCase;

/**
 * Created by the Exo Development team.
 */
public class TestSimpleResolver extends TestCase {

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
        reader.parse(getClass().getClassLoader().getResource("tmp/dtd-not-found.xml")
            .toString());

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
      reader.parse(getClass().getClassLoader().getResource("web.xml")
          .toString());

    } catch (Exception e) {

      fail("testWebXmlResolving() ERROR: " + e.toString());
    }

  }

}
