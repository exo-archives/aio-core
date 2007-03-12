/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.document.impl;

import java.io.InputStream;
import java.util.Properties;

import org.apache.poi.hslf.extractor.PowerPointExtractor;
/**
 * Created by The eXo Platform SARL
 *
 * A parser of Microsoft PowerPoint files.
 * Parses the files with application/powerpoint mime type.
 * @author <a href="mailto:phunghainam@gmail.com">Phung Hai Nam</a>
 * @author Gennady Azarenkov
 * @version Oct 19, 2005
 */
public class PPTDocumentReader extends BaseDocumentReader {


  /**
   * Get the application/powerpoint mime type.
   * @return The application/powerpoint mime type.
   */
  public String[] getMimeTypes() {
    return new String[] {"application/powerpoint", "application/ppt"} ;
  }

  /**
   * Returns only a text from .ppt file content.
   * @param is an input stream with .ppt file content.
   * @return The string only with text from file content.
   * @throws Exception
   */
  public String getContentAsText(InputStream is) throws Exception {
    String text = null;
    try {
      PowerPointExtractor ppe = new PowerPointExtractor(is);
      text = ppe.getText(true,true) ;
    }
    finally {
    }
    return text ;
  }
  
  /* (non-Javadoc)
   * @see org.exoplatform.services.document.DocumentReader#getProperties(java.io.InputStream)
   */
  public Properties getProperties(InputStream is) throws Exception {
    POIPropertiesReader reader = new POIPropertiesReader();
    reader.readDCProperties(is);
    return reader.getProperties();
  }

}
