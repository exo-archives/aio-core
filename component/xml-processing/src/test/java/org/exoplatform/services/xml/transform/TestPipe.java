/**
 * Copyright 2001-2005 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.xml.transform;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.xml.transform.trax.TRAXTransformer;
import org.exoplatform.services.xml.transform.trax.TRAXTransformerService;
import org.exoplatform.services.xml.transform.trax.TRAXTemplates;
import org.exoplatform.services.xml.transform.html.HTMLTransformerService;
import org.exoplatform.services.xml.transform.html.HTMLTransformer;
import org.exoplatform.services.xml.transform.trax.Constants;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import org.apache.commons.logging.Log;

/**
 * Created by the Exo Development team.
 */
public class TestPipe extends BaseTest {

    private HTMLTransformer htmlTransformer;
    private TRAXTemplates traxTemplates;
    private TRAXTransformerService traxService;
    private Log log;

    public void setUp() throws Exception {
        log = getLog();

      //html transformer
      HTMLTransformerService htmlService =
              (HTMLTransformerService) PortalContainer.getInstance().
              getComponentInstanceOfType(HTMLTransformerService.class);
      assertNotNull("htmlService", htmlService);
      htmlTransformer = htmlService.getTransformer();
      assertNotNull("get html transformer", htmlTransformer);

      //trax transformer
      traxService = (TRAXTransformerService) PortalContainer.getInstance().
              getComponentInstanceOfType(TRAXTransformerService.class);

      //get xsl
      String XSL_URL = Constants.XSLT_DIR + "/html-url-rewite.xsl";
      InputStream xslInputStream = Thread.currentThread().
                                   getContextClassLoader().getResourceAsStream(
                                           XSL_URL);
      assertNotNull("empty xsl", xslInputStream);
      Source xslSource = new StreamSource(xslInputStream);
      assertNotNull("get xsl source", xslSource);
      //init transformer
      traxTemplates = traxService.getTemplates(xslSource);
      assertNotNull("get trax Templates", traxTemplates);
  }

  public void testTidyAndXsl() throws Exception {
      //input
      FileInputStream inputFileInputStream =
            new FileInputStream("tmp/rss-out.html");
      assertTrue("Empty input file",inputFileInputStream.available() > 0);


      //output
      String OUTPUT_FILENAME = "tmp/rss-out_"+getTimeStamp()+"_tydy_xsl.xml";
      OutputStream outputFileOutputStream =
                new FileOutputStream(OUTPUT_FILENAME);
      TRAXTransformer traxTransformer = traxTemplates.newTransformer();

      //construct pipe
      htmlTransformer.initResult(
            traxTransformer.getTransformerAsResult());
      traxTransformer.initResult(
            new StreamResult(outputFileOutputStream));
      htmlTransformer.transform(new StreamSource(inputFileInputStream));

      inputFileInputStream.close();

      //read the output file
      FileInputStream outputFileInputStream =
                new FileInputStream(OUTPUT_FILENAME);

      assertTrue("Output is empty", outputFileInputStream.available() > 0);
      outputFileInputStream.close();
  }

  public void testXslAndXsl() throws Exception {
      //input
      FileInputStream inputFileInputStream =
            new FileInputStream("tmp/rss-out.xhtml");
      assertTrue("Empty input file",inputFileInputStream.available() > 0);

      //output
      String OUTPUT_FILENAME = "tmp/rss-out_"+getTimeStamp()+"_xsl_xsl.xml";
      OutputStream outputFileOutputStream =
                new FileOutputStream(OUTPUT_FILENAME);

      TRAXTransformer traxTransformer1 = traxTemplates.newTransformer();
      TRAXTransformer traxTransformer2 = traxTemplates.newTransformer();

      assertNotNull("pipe supported ",
                    traxTransformer2.getTransformerAsResult());

      //construct pipe
      traxTransformer1.initResult(
            traxTransformer2.getTransformerAsResult());

      traxTransformer2.initResult(
            new StreamResult(outputFileOutputStream));

      traxTransformer1.transform(new StreamSource(inputFileInputStream));

      inputFileInputStream.close();
      outputFileOutputStream.flush();
      outputFileOutputStream.close();

      //read the output file
      FileInputStream outputFileInputStream =
                new FileInputStream(OUTPUT_FILENAME);

      assertTrue("Output is empty", outputFileInputStream.available() > 0);
      //validate output
      validateXML(outputFileInputStream);
      outputFileInputStream.close();

  }

  public void testTidyAndXslWithEmptySource() throws Exception {


      java.io.ByteArrayOutputStream output =
                new java.io.ByteArrayOutputStream();
      InputStream input = new java.io.ByteArrayInputStream(new byte[]{});

      TRAXTransformer traxTransformer = traxTemplates.newTransformer();

      //construct pipe
      htmlTransformer.initResult(
            traxTransformer.getTransformerAsResult());
      traxTransformer.initResult(
            new StreamResult(output));
      htmlTransformer.transform(new StreamSource(input));

      assertTrue("Output is not empty", output.toByteArray().length == 0);
  }



}
