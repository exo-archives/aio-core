/**
 * Copyright 2001-2005 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.xml.transform;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.SAXResult;

import org.exoplatform.container.PortalContainer;

import org.exoplatform.services.xml.transform.html.HTMLTransformer;
import org.exoplatform.services.xml.transform.html.HTMLTransformerService;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import java.util.Properties;

import org.exoplatform.services.log.LogService;
import org.exoplatform.container.RootContainer;
import org.apache.commons.logging.Log;


/**
 * Created by the Exo Development team.
 */
public class TestTidy extends BaseTest {
    private HTMLTransformer htmlTransformer;

  private Log log;

  public void setUp() throws Exception {
      log = getLog();
        HTMLTransformerService htmlService =
                (HTMLTransformerService) PortalContainer.getInstance().
                getComponentInstanceOfType(HTMLTransformerService.class);
        assertNotNull("htmlService", htmlService);
        htmlTransformer =  htmlService.getTransformer();

    }


  public void testTidy() throws Exception {
    try {
      String OUTPUT_FILENAME = "tmp/rss-out_"+getTimeStamp()+"_xhtml.xhtml";

      FileInputStream inputFileInputStream =
                new FileInputStream("tmp/rss-out.html");

      //output file
      OutputStream outputFileOutputStream =
                new FileOutputStream(OUTPUT_FILENAME);

      htmlTransformer.initResult(new StreamResult(outputFileOutputStream));
      htmlTransformer.transform(new StreamSource(inputFileInputStream));

      outputFileOutputStream.close();

      //read the output file
      FileInputStream outputFileInputStream =
                new FileInputStream(OUTPUT_FILENAME);

      assertTrue("Output is empty", outputFileInputStream.available() > 0);

      //validate output xml
      validateXML(outputFileInputStream);

    } catch (Exception e) {
      fail("testTidy() ERROR: " + e.toString());
    }
  }

  public void testSAXResultType() throws Exception {

      FileInputStream inputFileInputStream =
              new FileInputStream("tmp/rss-out.html");
      assertTrue("Empty input file",inputFileInputStream.available() > 0);

      //create empty transformation
      TransformerHandler transformHandler = //a copy of the source to the result
              ((SAXTransformerFactory) SAXTransformerFactory.newInstance()).
              newTransformerHandler();

      String  OUTPUT_FILENAME = "tmp/rss-out_"+getTimeStamp()+"_html2sax.xhtml";
      OutputStream output = new FileOutputStream(OUTPUT_FILENAME);

      transformHandler.setResult(new StreamResult(output));

      SAXResult saxResult = new SAXResult(transformHandler);
      htmlTransformer.initResult(saxResult);
      htmlTransformer.transform(new StreamSource(inputFileInputStream));


      output.flush();
      output.close();
      //read the output file
      FileInputStream outputFileInputStream =
                new FileInputStream(OUTPUT_FILENAME);
      assertTrue("Output is empty", outputFileInputStream.available() > 0);
      //validate output xml
      validateXML(outputFileInputStream);
  }


  public void testProps() throws Exception {
      try {
          Properties props = htmlTransformer.getOutputProperties();

          assertEquals(props.getProperty("quiet"), "true");
          props.setProperty("quiet", "false");

          htmlTransformer.setOutputProperties(props);
          assertEquals(htmlTransformer.getOutputProperties().getProperty(
                  "quiet"), "false");

      } catch (Exception e) {
          fail("testProps() ERROR: " + e.toString());
      }

  }


}
