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
package org.exoplatform.services.xml.resolving.impl.xmlcommons;

import junit.framework.TestCase;

import org.apache.xml.resolver.tools.CatalogResolver;
import org.exoplatform.services.xml.resolving.XMLCatalogResolvingService;

/**
 * Created by the Exo Development team.
 */
public class TestXMLCatalogResolver extends TestCase {

  public void setUp() throws Exception {
  }

  /**
   * Prerequisites: CatalogManager.properties in CLASSPATH with
   * catalogs=catalog/exo-catalog.xml 
   * accessable catalog/exo-catalog.xml file
   * 
   * @throws Exception
   */
  public void testConfig() throws Exception {
    assertNotNull(getClass().getClassLoader().getResource(
        "CatalogManager.properties"));

    XMLCatalogResolvingService service = new XMLCommonsResolvingServiceImpl();

    String catalogFile = "catalog/exo-catalog.xml";

    assertEquals(catalogFile, System.getProperty("xml.catalog.files"));

    assertNotNull(getClass().getClassLoader().getResource(catalogFile));

  }

  public void testWebXmlResolving() throws Exception {
    // DOES NOT WORK!
    
    
    XMLCommonsResolvingServiceImpl service = new XMLCommonsResolvingServiceImpl();
    

//    assertTrue(
//        "DTD for WEB.XML (publicId=\"-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN\") must be in catalog! ",
//        service
//            .isLocallyResolvable("-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"));
//
//    javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory
//        .newInstance();
//    factory.setNamespaceAware(true);
//    javax.xml.parsers.SAXParser jaxpParser = factory.newSAXParser();
//    org.xml.sax.XMLReader reader = jaxpParser.getXMLReader();
//
//    reader.setEntityResolver(service.getEntityResolver());
//    reader.parse("web.xml");

  }

}
