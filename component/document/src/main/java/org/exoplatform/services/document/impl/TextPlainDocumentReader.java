/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.document.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by The eXo Platform SARL
 *
 * A reader of text files.
 * @author <a href="mailto:zagrebin_v@mail.ru">Victor Zagrebin</a>
 * @version March 04, 2006
 */
public class TextPlainDocumentReader extends BaseDocumentReader {

//  /**
//   * Initializes a newly created object for text/plain files format parsing.
//   * @param params the container parameters.
//   */
//  public TextPlainDocumentReader(InitParams params) {
//  }

  /**
   * Get the text/plain mime type.
   * @return The text/plain mime type.
   */
  public String[] getMimeTypes() {
    return new String[] {"text/plain"};
  }

  /**
   * Returns a text from file content.
   * @param is an input stream with a file content.
   * @return The string with text from file content.
   * @throws Exception
   */
  public String getContentAsText(InputStream is) throws Exception {
    
    byte[] buffer = new byte[2048];
    int len;
    ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
    while ((len = is.read(buffer)) > 0) 
      bos.write(buffer, 0, len);
    bos.close();
    return new String(bos.toByteArray());

//    String text = "";
//    try {
//      int size = is.available();
//      byte b[] = new byte[size];
//      is.read(b);
//      text = new String(b);
//    }
//    catch(Exception e)  {}
//    return text ;
  }
  
  /* (non-Javadoc)
   * @see org.exoplatform.services.document.DocumentReader#getProperties(java.io.InputStream)
   */
  public Properties getProperties(InputStream is) throws Exception {
    return new Properties();
  }

}
