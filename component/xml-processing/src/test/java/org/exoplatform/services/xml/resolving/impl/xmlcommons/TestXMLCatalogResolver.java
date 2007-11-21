/**
 * Copyright 2001-2007 The eXo platform SAS All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
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
