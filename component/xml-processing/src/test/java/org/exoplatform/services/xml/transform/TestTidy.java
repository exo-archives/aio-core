/**
 * Copyright 2001-2005 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.xml.transform;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.xml.transform.html.HTMLTransformer;
import org.exoplatform.services.xml.transform.html.HTMLTransformerService;


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
      String OUTPUT_FILENAME = "target/rss-out_"+getTimeStamp()+"_xhtml.xhtml";

      InputStream res = getClass().getClassLoader().getResourceAsStream(
      "rss-out.html");

      //output file
      OutputStream outputFileOutputStream =
                new FileOutputStream(OUTPUT_FILENAME);

      htmlTransformer.initResult(new StreamResult(outputFileOutputStream));
      htmlTransformer.transform(new StreamSource(res));

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
    InputStream res = getClass().getClassLoader().getResourceAsStream(
    "rss-out.xhtml");

      assertTrue("Empty input file",res.available() > 0);

      //create empty transformation
      TransformerHandler transformHandler = //a copy of the source to the result
              ((SAXTransformerFactory) SAXTransformerFactory.newInstance()).
              newTransformerHandler();

      String  OUTPUT_FILENAME = "target/rss-out_"+getTimeStamp()+"_html2sax.xhtml";
      OutputStream output = new FileOutputStream(OUTPUT_FILENAME);

      transformHandler.setResult(new StreamResult(output));

      SAXResult saxResult = new SAXResult(transformHandler);
      htmlTransformer.initResult(saxResult);
      htmlTransformer.transform(new StreamSource(res));


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
