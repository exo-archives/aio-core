/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.document.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.document.DocumentReaderService;

public class PropertiesExtractTest extends TestCase {

  DocumentReaderService service_;

  public PropertiesExtractTest(String name) {
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
    //File f = new File("src/test/resources/portlet-1_0-prd-spec.pdf");
    
    is = new FileInputStream(f);
    Properties props = service_.getDocumentReader("application/pdf").getProperties(is);
    printProps(props);
  }

  
  public void testWordDocumentReaderService() throws Exception {
    InputStream is = null;
    File f = new File("src/test/resources/test.doc");
    is = new FileInputStream(f);
    Properties props = service_.getDocumentReader("application/msword").getProperties(is);
    printProps(props);
  }

  public void testPPTDocumentReaderService() throws Exception {
    InputStream is = null;
    File f = new File("src/test/resources/test.ppt");
    is = new FileInputStream(f);
    Properties props = service_.getDocumentReader("application/powerpoint").getProperties(is);
    printProps(props);

  }

  public void testExcelDocumentReaderService() throws Exception {
    InputStream is = null;
    File f = new File("src/test/resources/test.xls");
    is = new FileInputStream(f);
    Properties props = service_.getDocumentReader("application/excel").getProperties(is);
    printProps(props);
    
  }

  public void testOODocumentReaderService() throws Exception {
    File f = new File("src/test/resources/subscription.odt");
    InputStream is = new FileInputStream(f);
    Properties props = service_.getDocumentReader("application/vnd.oasis.opendocument.text").getProperties(is);
    printProps(props);

  }

/*
  public void testHTMLDocumentReaderService() throws Exception {
    InputStream is = null;
    File f = new File("src/resources/test.html");
    is = new FileInputStream(f);
    String text = service_.getDocumentReader("text/html").getContentAsText(is);
    System.out.println(text);
  }
*/
  private void printProps(Properties props) {
    Iterator it = props.entrySet().iterator();
    while(it.hasNext()) {
      Map.Entry entry = (Map.Entry)it.next();
      System.out.println(" "+entry.getKey()+" -> "+entry.getValue());
    }
  }
  
}
