/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.xml.resolving.impl.xmlcommons;

import junit.framework.TestCase;

import java.io.File;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.xml.resolving.XMLCatalogResolvingService;
import org.exoplatform.services.xml.resolving.impl.xmlcommons.XMLCommonsResolvingServiceImpl;

/**
 * Created by the Exo Development team.
 */
public class TestXMLCatalogResolver extends TestCase {

   public void setUp() throws Exception
   {
   }

   public void testConfig() throws Exception
   {
       try {
//          PortalContainer manager = PortalContainer.getInstance();
//          XMLCatalogResolvingService service = (XMLCatalogResolvingService) manager.
//              getComponentInstanceOfType(XMLCommonsResolvingServiceImpl.class);
           XMLCatalogResolvingService service =  new XMLCommonsResolvingServiceImpl();
          assertEquals( "./catalog/exo-catalog.xml", System.getProperty("xml.catalog.files"));

          assertTrue( "File ./catalog/exo-catalog.xml not found!", new File("./catalog/exo-catalog.xml").exists() );

       } catch ( Exception e) {

            fail( "testConfig() ERROR: "+e.toString());
        }

   }

   public void testWebXmlResolving() throws Exception
   {
       try {
//          PortalContainer manager = PortalContainer.getInstance();
//          XMLCommonsResolvingServiceImpl service = (XMLCommonsResolvingServiceImpl) manager.
//              getComponentInstanceOfType(XMLCommonsResolvingServiceImpl.class);
           XMLCommonsResolvingServiceImpl service =  new XMLCommonsResolvingServiceImpl();

          assertTrue("DTD for WEB.XML (publicId=\"-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN\") must be in catalog! ",
                      service.isLocallyResolvable("-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN") );


	  javax.xml.parsers.SAXParserFactory factory=javax.xml.parsers.SAXParserFactory.newInstance();
	  factory.setNamespaceAware( true );
	  javax.xml.parsers.SAXParser jaxpParser=factory.newSAXParser();
	  org.xml.sax.XMLReader reader=jaxpParser.getXMLReader();

          reader.setEntityResolver(service.getEntityResolver());
          reader.parse("tmp/web.xml");

       } catch ( Exception e) {

            fail( "testWebXmlResolving() ERROR: "+e.toString());
        }

   }


}
