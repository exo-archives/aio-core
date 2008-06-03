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

import java.io.InputStream;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.document.DocumentReaderService;
import org.exoplatform.test.BasicTestCase;
/**
 * Web, Oct 19, 2005 @
 * @author: Phung Hai Nam
 * @email: phunghainam@gmail.com
 */
public class TestDocumentReaderServices extends BasicTestCase {

  DocumentReaderService service_;

  public TestDocumentReaderServices(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    service_ =
      (DocumentReaderService) pcontainer.getComponentInstanceOfType(DocumentReaderService.class) ;
  }

  public void testPDFDocumentReaderService() throws Exception {
    InputStream is = TestDocumentReaderServices.class.getResourceAsStream("/test.pdf");
    String text = service_.getDocumentReader("application/pdf").getContentAsText( is);
    System.out.println(text);
  }

  public void testWordDocumentReaderService() throws Exception {
    InputStream is = TestDocumentReaderServices.class.getResourceAsStream("/test.doc");
    String text = service_.getDocumentReader("application/msword").getContentAsText(is);
    System.out.println(text);
  }

  public void testWordTemplateReaderService() throws Exception {
    InputStream inStream = TestDocumentReaderServices.class.getResourceAsStream("/test.dot");
    String text = service_.getDocumentReader("application/msworddot").getContentAsText(inStream);
    System.out.println("TEXT: " + text);
  }

  public void testPPTDocumentReaderService() throws Exception {
    InputStream is = TestDocumentReaderServices.class.getResourceAsStream("/test.ppt");
    String text = service_.getDocumentReader("application/powerpoint").getContentAsText(is);
    System.out.println(text);
  }

  public void testExcelDocumentReaderService() throws Exception {
    InputStream is = TestDocumentReaderServices.class.getResourceAsStream("/test.xls");
    String text = service_.getDocumentReader("application/excel").getContentAsText(is);
    System.out.println(text);
  }

  public void testHTMLDocumentReaderService() throws Exception {
    InputStream is = TestDocumentReaderServices.class.getResourceAsStream("/test.html");
    String text = service_.getDocumentReader("text/html").getContentAsText(is);
    System.out.println(text);
  }

  public void testXMLDocumentReaderService() throws Exception {
    InputStream is = TestDocumentReaderServices.class.getResourceAsStream("/test.xml");
    String text = service_.getDocumentReader("text/xml").getContentAsText(is);
    System.out.println(text);
  }

  public void testTextPlainDocumentReaderService() throws Exception {
    InputStream is = TestDocumentReaderServices.class.getResourceAsStream("/test.txt");
    String text = service_.getDocumentReader("text/plain").getContentAsText(is);
    System.out.println(text);
  }

  public void testOODocumentReaderService() throws Exception {
    InputStream is = TestDocumentReaderServices.class.getResourceAsStream("/test.odt");
    String text = service_.getDocumentReader("application/vnd.oasis.opendocument.text").getContentAsText( is);
    System.out.println(text);
  }

}
