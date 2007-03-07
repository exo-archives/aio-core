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
import org.exoplatform.services.xml.transform.trax.Constants;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;

/**
 * Created by the Exo Development team.
 */
public class TestXsl extends BaseTest {
    private TRAXTransformerService traxService;
    private Log log;

    public void setUp() throws Exception {
        log = getLog();
        traxService =
                (TRAXTransformerService) PortalContainer.getInstance().
                 getComponentInstanceOfType(TRAXTransformerService.class);
        assertNotNull("traxService", traxService);
//        dateFormat = new SimpleDateFormat(DATE_PATTERN);


    }


  public void testSimpleXslt() throws Exception {


      String OUTPUT_FILENAME = "tmp/rss-out_"+getTimeStamp()+"_xsl.xml";

      FileInputStream inputFileInputStream =
                new FileInputStream("tmp/rss-out.xhtml");
      assertTrue("Empty input file",inputFileInputStream.available() > 0);

      //output file
      OutputStream outputFileOutputStream =
                new FileOutputStream(OUTPUT_FILENAME);


      //get xsl
      String XSL_URL = Constants.XSLT_DIR+"/html-url-rewite.xsl";
      InputStream xslInputStream = Thread.currentThread().
                   getContextClassLoader().getResourceAsStream(XSL_URL);
      assertNotNull("empty xsl",xslInputStream);
      Source xslSource = new StreamSource(xslInputStream);
      assertNotNull("get xsl source",xslSource);

      //init transformer
      TRAXTransformer traxTransformer = traxService.getTransformer(xslSource);
      assertNotNull("get transformer",traxTransformer);

      traxTransformer.initResult(new StreamResult(outputFileOutputStream));
      traxTransformer.transform(new StreamSource(inputFileInputStream));

      inputFileInputStream.close();
      outputFileOutputStream.close();

      //read the output file
      FileInputStream outputFileInputStream =
                new FileInputStream(OUTPUT_FILENAME);

      assertTrue("Output is empty", outputFileInputStream.available() > 0);
      outputFileInputStream.close();


  }

  public void testXsltUseTemplates() throws Exception {
    //input
    FileInputStream inputFileInputStream =
              new FileInputStream("tmp/rss-out.xhtml");
    assertTrue("Empty input file",inputFileInputStream.available() > 0);

    //output
    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

    //get xsl
    String XSL_URL = Constants.XSLT_DIR+"/html-url-rewite.xsl";
    InputStream xslInputStream = Thread.currentThread().
                 getContextClassLoader().getResourceAsStream(XSL_URL);
    assertNotNull("empty xsl",xslInputStream);
    Source xslSource = new StreamSource(xslInputStream);
    assertNotNull("get xsl source",xslSource);

    //init templates
    TRAXTemplates traxTemplates = traxService.getTemplates(xslSource);
    assertNotNull("get templates",traxTemplates);

    //get transformer
    TRAXTransformer traxTransformer = traxTemplates.newTransformer();
    assertNotNull("get transformer",traxTransformer);

    //transform
    traxTransformer.initResult(new StreamResult(byteOutputStream));
    traxTransformer.transform(new StreamSource(inputFileInputStream));
    inputFileInputStream.close();

    assertTrue("Output is empty", byteOutputStream.size() > 0);

    //other transformer from same templates

    TRAXTransformer traxOtherTransformer = traxTemplates.newTransformer();
    assertNotNull("get Other transformer",traxOtherTransformer);

    FileInputStream inputOtherFileInputStream =
            new FileInputStream("tmp/rss-out.xhtml");
    assertTrue("Empty input other file",
               inputOtherFileInputStream.available() > 0);

    ByteArrayOutputStream byteOtherOutputStream = new ByteArrayOutputStream();

    traxOtherTransformer.initResult(new StreamResult(byteOtherOutputStream));
    traxOtherTransformer.transform(new StreamSource(inputOtherFileInputStream));
    inputOtherFileInputStream.close();
    assertTrue("Output other is empty", byteOutputStream.size() > 0);

}




}
