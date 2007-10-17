/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.document.impl;

import java.io.InputStream;
import java.util.Properties;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

/**
 * Created by The eXo Platform SARL
 * 
 * A parser of Microsoft Word files.
 * 
 * @author <a href="mailto:phunghainam@gmail.com">Phung Hai Nam</a>
 * @author Gennady Azarenkov
 * @version Oct 19, 2005
 */
public class MSWordDocumentReader extends BaseDocumentReader {

  /**
   * Get the application/msword mime type.
   * 
   * @return The application/msword mime type.
   */
  public String[] getMimeTypes() {
    return new String[] {
        "application/msword",
        "application/msworddoc",
        "application/msworddot"
    };
  }

  /**
   * Returns only a text from .doc file content.
   * 
   * @param is
   *          an input stream with .doc file content.
   * @return The string only with text from file content.
   * @throws Exception
   */
  public String getContentAsText(InputStream is) throws Exception {
    String text = "";
    try {
      HWPFDocument doc = new HWPFDocument(is);
      Range range = doc.getRange();
      text = range.text();
    } catch (Exception e) {
    }
    return text;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.exoplatform.services.document.DocumentReader#getProperties(java.io.InputStream)
   */
  public Properties getProperties(InputStream is) throws Exception {
    POIPropertiesReader reader = new POIPropertiesReader();
    reader.readDCProperties(is);
    return reader.getProperties();
  }

}
