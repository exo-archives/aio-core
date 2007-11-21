/**
 * Copyright 2001-2005 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.xml.transform;

import junit.framework.TestCase;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import java.io.InputStream;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import org.apache.commons.logging.Log ;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.xml.resolving.XMLResolvingService;


/**
 * Created by the Exo Development team.
 *
 * Base transformer test
 */
public class BaseTest extends TestCase {
    private final String DATE_PATTERN = "yy-MM-DD_HH-mm-ss";
    private DateFormat dateFormat;

//    protected Log log;

  protected String getTimeStamp(){
      return dateFormat.format(new Date());
  }

  protected Log getLog(){
      Log log= null;
      try {
          LogService service = (LogService) RootContainer.getInstance().
                               getComponentInstanceOfType(LogService.class);
          log = service.getLog("org.exoplatform.services.xml");
          service.setLogLevel("org.exoplatform.services.xml",
                              LogService.DEBUG, true);
          log = service.getLog(this.getClass());
//          log.debug("Test class name is "+this.getClass().getName());
      } catch (Exception ex) {

      }
      return log;
}


  public BaseTest(){
      dateFormat = new SimpleDateFormat(DATE_PATTERN);
  }


    protected void validateXML(InputStream input) throws Exception {
        XMLResolvingService resolvingService =
                (XMLResolvingService) PortalContainer.getInstance().
                getComponentInstanceOfType(XMLResolvingService.class);
        assertNotNull("XMLResolvingService", resolvingService);

        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setEntityResolver(resolvingService.getEntityResolver());
        assertNotNull("resolvingService.getEntityResolver()",
                      resolvingService.getEntityResolver());
        getLog().debug("resolvingService class is "+resolvingService.getClass().getName());

        InputSource src = resolvingService.getEntityResolver().
                     resolveEntity("-//W3C//DTD XHTML 1.0 Transitional//EN",
                     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        assertNotNull("Not resolved InputSource entity", src);

        xmlReader.setFeature("http://xml.org/sax/features/validation",
                             true); //validation on
        //transform
        try {
            xmlReader.parse(new InputSource(input));
        } catch (org.xml.sax.SAXParseException ex) {
            fail("Document is not valid XML. See: \n" + ex.getMessage());
        }
    }

}
