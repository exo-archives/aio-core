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
package org.exoplatform.services.document.test;

import junit.framework.TestCase;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.document.DCMetaData;
import org.exoplatform.services.document.DocumentReader;
import org.exoplatform.services.document.DocumentReaderService;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class TestPropertiesExtracting extends TestCase {

  DocumentReaderService service_;

  public TestPropertiesExtracting(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance();
    service_ = (DocumentReaderService) pcontainer.getComponentInstanceOfType(DocumentReaderService.class);
  }

  public void testPDFDocumentReaderService() throws Exception {
    InputStream is = TestPropertiesExtracting.class.getResourceAsStream("/test.pdf");
    DocumentReader rdr = service_.getDocumentReader("application/pdf");
    Properties props = rdr.getProperties(is);
    assertTrue(props.isEmpty());

    // Properties etalon = new Properties();
    // etalon.put(DCMetaData.PUBLISHER, "FOP 0.20.4");
    // evalProps(etalon, props, false);
  }

  public void testPDFDocumentReaderServiceXMPMetadata() throws Exception {
    InputStream is = TestPropertiesExtracting.class.getResourceAsStream("/MyTest.pdf");
    DocumentReader rdr = service_.getDocumentReader("application/pdf");

    Properties testprops = rdr.getProperties(is);
    // printProps(testprops);

    Properties etalon = new Properties();
    etalon.put(DCMetaData.TITLE, "Test de convertion de fichier tif");
    etalon.put(DCMetaData.CREATOR, "Christian Klaus");
    etalon.put(DCMetaData.SUBJECT, "20080901 TEST Christian Etat OK");
    // Calendar c = ISO8601.parseEx("2008-09-01T08:01:10+00:00");
    // etalon.put(DCMetaData.DATE, c);

    evalProps(etalon, testprops, false);
  }

  public void testPDFDocumentReaderServiceBrokenFile() throws Exception {
    InputStream is = TestPropertiesExtracting.class.getResourceAsStream("/pfs_accapp.pdf");
    try {

      DocumentReader rdr = service_.getDocumentReader("application/pdf");
      Properties testprops = rdr.getProperties(is);
      Properties etalon = new Properties();
      etalon.put(DCMetaData.TITLE, "Personal Account Opening Form VN");
      etalon.put(DCMetaData.CREATOR, "mr");
      evalProps(etalon, testprops, false);
    } finally {
      is.close();
    }
  }

  public void testPDFDocumentReaderServiceMetro() throws Exception {
    InputStream is = TestPropertiesExtracting.class.getResourceAsStream("/metro.pdf");
    try {

      DocumentReader rdr = service_.getDocumentReader("application/pdf");
      Properties testprops = rdr.getProperties(is);
      Properties etalon = new Properties();
      etalon.put(DCMetaData.TITLE, "metro");
      etalon.put(DCMetaData.CREATOR, "Preview");
      evalProps(etalon, testprops, false);
    } finally {
      is.close();
    }
  }

  public void testWordDocumentReaderService() throws Exception {
    InputStream is = TestPropertiesExtracting.class.getResourceAsStream("/test.doc");
    Properties props = service_.getDocumentReader("application/msword").getProperties(is);
    printProps(props);
  }

  public void testPPTDocumentReaderService() throws Exception {
    InputStream is = TestPropertiesExtracting.class.getResourceAsStream("/test.ppt");
    Properties props = service_.getDocumentReader("application/powerpoint").getProperties(is);
    printProps(props);
  }

  public void testExcelDocumentReaderService() throws Exception {
    InputStream is = TestPropertiesExtracting.class.getResourceAsStream("/test.xls");
    Properties props = service_.getDocumentReader("application/excel").getProperties(is);
    printProps(props);
  }

  public void testOODocumentReaderService() throws Exception {
    InputStream is = TestPropertiesExtracting.class.getResourceAsStream("/test.odt");
    Properties props = service_.getDocumentReader("application/vnd.oasis.opendocument.text")
                               .getProperties(is);
    printProps(props);
  }

  private void printProps(Properties props) {
    Iterator it = props.entrySet().iterator();
    props.toString();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      System.out.println(" " + entry.getKey() + " -> [" + entry.getValue() + "]");
    }
  }

  private void evalProps(Properties etalon, Properties testedProps, boolean testSize) {
    Iterator it = etalon.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry prop = (Map.Entry) it.next();
      Object tval = testedProps.get(prop.getKey());
      assertNotNull(prop.getKey() + " property not founded. ", tval);
      assertEquals(prop.getKey() + " property value is incorrect", prop.getValue(), tval);
    }
    if (testSize) {
      assertEquals("size is incorrect", etalon.size(), testedProps.size());
    }
  }

}
