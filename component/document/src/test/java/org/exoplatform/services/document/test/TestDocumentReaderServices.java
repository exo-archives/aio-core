/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
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
