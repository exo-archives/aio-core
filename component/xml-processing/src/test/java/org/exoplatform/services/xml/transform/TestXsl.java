/**
 * Copyright 2001-2005 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.xml.transform;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.xml.BaseTest;
import org.exoplatform.services.xml.transform.trax.TRAXTemplates;
import org.exoplatform.services.xml.transform.trax.TRAXTransformer;
import org.exoplatform.services.xml.transform.trax.TRAXTransformerService;

/**
 * Created by the Exo Development team.
 */
public class TestXsl extends BaseTest {
  private TRAXTransformerService traxService;
  private Log log;

  public void setUp() throws Exception {
    log = getLog();
    traxService = (TRAXTransformerService) PortalContainer.getInstance()
        .getComponentInstanceOfType(TRAXTransformerService.class);
    assertNotNull("traxService", traxService);
    // dateFormat = new SimpleDateFormat(DATE_PATTERN);

  }

  public void testSimpleXslt() throws Exception {
    
    InputStream res = resourceStream("rss-in.xhtml");
    String OUTPUT_FILENAME = resourceURL("rss-out.xml").getPath();


    // FileInputStream inputFileInputStream =
    // new FileInputStream("tmp/rss-out.xhtml");

    assertTrue("Empty input file", res.available() > 0);

    // output file
    OutputStream outputFileOutputStream = new FileOutputStream(OUTPUT_FILENAME);

    // get xsl
    //String XSL_URL = Constants.XSLT_DIR + "/html-url-rewite.xsl";
    InputStream xslInputStream = resourceStream("html-url-rewite.xsl");
    assertNotNull("empty xsl", xslInputStream);
    Source xslSource = new StreamSource(xslInputStream);
    assertNotNull("get xsl source", xslSource);

    // init transformer
    TRAXTransformer traxTransformer = traxService.getTransformer(xslSource);
    assertNotNull("get transformer", traxTransformer);

    traxTransformer.initResult(new StreamResult(outputFileOutputStream));
    traxTransformer.transform(new StreamSource(res));

    res.close();
    outputFileOutputStream.close();

    // read the output file
    FileInputStream outputFileInputStream = new FileInputStream(OUTPUT_FILENAME);

    assertTrue("Output is empty", outputFileInputStream.available() > 0);
    outputFileInputStream.close();

  }

  public void testXsltUseTemplates() throws Exception {
//    String OUTPUT_FILENAME = resourceURL("rss-in.xml").getPath();
    InputStream res = resourceStream("rss-in.xhtml");

    assertTrue("Empty input file", res.available() > 0);

    // output
    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

    // get xsl
    //String XSL_URL = Constants.XSLT_DIR + "/html-url-rewite.xsl";
    InputStream xslInputStream = resourceStream("html-url-rewite.xsl");
    assertNotNull("empty xsl", xslInputStream);
    Source xslSource = new StreamSource(xslInputStream);
    assertNotNull("get xsl source", xslSource);

    // init templates
    TRAXTemplates traxTemplates = traxService.getTemplates(xslSource);
    assertNotNull("get templates", traxTemplates);

    // get transformer
    TRAXTransformer traxTransformer = traxTemplates.newTransformer();
    assertNotNull("get transformer", traxTransformer);

    // transform
    traxTransformer.initResult(new StreamResult(byteOutputStream));
    traxTransformer.transform(new StreamSource(res));
    res.close();

    assertTrue("Output is empty", byteOutputStream.size() > 0);

    // other transformer from same templates

    TRAXTransformer traxOtherTransformer = traxTemplates.newTransformer();
    assertNotNull("get Other transformer", traxOtherTransformer);

    res = resourceStream("rss-in.xhtml");

    assertTrue("Empty input other file",
        res.available() > 0);

    ByteArrayOutputStream byteOtherOutputStream = new ByteArrayOutputStream();

    traxOtherTransformer.initResult(new StreamResult(byteOtherOutputStream));
    traxOtherTransformer.transform(new StreamSource(res));
    res.close();
    assertTrue("Output other is empty", byteOutputStream.size() > 0);

  }

}
