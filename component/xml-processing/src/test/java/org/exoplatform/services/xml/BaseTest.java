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

package org.exoplatform.services.xml;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import org.apache.commons.logging.Log;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.xml.resolving.XMLResolvingService;

/**
 * Created by the Exo Development team. Base transformer test
 */
public class BaseTest extends TestCase {
  private final String DATE_PATTERN = "yy-MM-DD_HH-mm-ss";

  private DateFormat   dateFormat;

  protected Log        log          = ExoLogger.getLogger("org.exoplatform.services.xml");

  protected String getTimeStamp() {
    return dateFormat.format(new Date());
  }

  protected URL resourceURL(String name) {
    return Thread.currentThread().getContextClassLoader().getResource(name);
  }

  protected InputStream resourceStream(String name) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
  }

  public BaseTest() {
    dateFormat = new SimpleDateFormat(DATE_PATTERN);
  }

  protected void validateXML(InputStream input) throws Exception {
    StandaloneContainer.setConfigurationPath(Thread.currentThread()
                                                   .getContextClassLoader()
                                                   .getResource("conf/standalone/test-configuration.xml")
                                                   .getPath());
    StandaloneContainer container = StandaloneContainer.getInstance();

    XMLResolvingService resolvingService = (XMLResolvingService) container.getComponentInstanceOfType(XMLResolvingService.class);
    assertNotNull("XMLResolvingService", resolvingService);

    XMLReader xmlReader = XMLReaderFactory.createXMLReader();
    xmlReader.setEntityResolver(resolvingService.getEntityResolver());

    assertNotNull("resolvingService.getEntityResolver()", resolvingService.getEntityResolver());

    log.info("resolvingService class is " + resolvingService.getClass().getName());

    InputSource src = resolvingService.getEntityResolver()
                                      .resolveEntity("-//W3C//DTD XHTML 1.0 Transitional//EN",
                                                     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");

    log.info(src.getSystemId());
    assertNotNull("Not resolved InputSource entity", src);

    xmlReader.setFeature("http://xml.org/sax/features/validation", true); // validation
    // on
    // transform
    try {
      xmlReader.parse(new InputSource(input));
    } catch (org.xml.sax.SAXParseException ex) {
      fail("Document is not valid XML. See: \n" + ex.getMessage());
    }
  }

}
