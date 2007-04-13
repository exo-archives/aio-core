/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.document.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.document.DocumentReaderService;
import org.exoplatform.test.BasicTestCase;
/**
 * Web, Oct 19, 2005 @
 * @author: Phung Hai Nam
 * @email: phunghainam@gmail.com
 */
public class DocumentReaderServiceTest extends BasicTestCase {

  DocumentReaderService service_;

  public DocumentReaderServiceTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    service_ =
      (DocumentReaderService) pcontainer.getComponentInstanceOfType(DocumentReaderService.class) ;
  }

  public void testPDFDocumentReaderService() throws Exception {
    InputStream is = null;
    File f = new File("src/test/resources/test.pdf");
    is = new FileInputStream(f);
    String text = service_.getDocumentReader("application/pdf").getContentAsText( is);
    System.out.println(text);
  }

  public void testWordDocumentReaderService() throws Exception {
    InputStream is = null;
    File f = new File("src/test/resources/test.doc");
    is = new FileInputStream(f);
    String text = service_.getDocumentReader("application/msword").getContentAsText(is);
    System.out.println(text);
  }

  public void testPPTDocumentReaderService() throws Exception {
    InputStream is = null;
    File f = new File("src/test/resources/test.ppt");
    is = new FileInputStream(f);
    String text = service_.getDocumentReader("application/powerpoint").getContentAsText(is);
    System.out.println(text);
  }

  public void testExcelDocumentReaderService() throws Exception {
    InputStream is = null;
    File f = new File("src/test/resources/test.xls");
    is = new FileInputStream(f);
    String text = service_.getDocumentReader("application/excel").getContentAsText(is);
    System.out.println(text);
  }

  public void testHTMLDocumentReaderService() throws Exception {
    InputStream is = null;
    File f = new File("src/test/resources/test.html");
    is = new FileInputStream(f);
    String text = service_.getDocumentReader("text/html").getContentAsText(is);
    System.out.println(text);
  }

  public void testXMLDocumentReaderService() throws Exception {
    InputStream is = null;
    File f = new File("src/test/resources/test.xml");
    is = new FileInputStream(f);
    String text = service_.getDocumentReader("text/xml").getContentAsText(is);
    System.out.println(text);
  }

  public void testTextPlainDocumentReaderService() throws Exception {
    InputStream is = null;
    File f = new File("src/test/resources/test.txt");
    is = new FileInputStream(f);
    String text = service_.getDocumentReader("text/plain").getContentAsText(is);
    System.out.println(text);
  }

  public void testOODocumentReaderService() throws Exception {
    File f = new File("src/test/resources/subscription.odt");
    InputStream is = new FileInputStream(f);
    String text = service_.getDocumentReader("application/vnd.oasis.opendocument.text").getContentAsText( is);
    System.out.println(text);
  }

}
