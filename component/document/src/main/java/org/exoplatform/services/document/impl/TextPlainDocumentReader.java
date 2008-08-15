/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.document.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValuesParam;



/**
 * Created by The eXo Platform SAS
 *
 * A reader of text files.
 * @author <a href="mailto:zagrebin_v@mail.ru">Victor Zagrebin</a>
 * @version March 04, 2006
 */
public class TextPlainDocumentReader extends BaseDocumentReader {

  public static final String DEFAULT_ENCODING = "defaultEncoding";
  
  private String defaultEncoding;
  
  /**
   * Initializes a newly created object for text/plain files format parsing.
   * @param params the container parameters.
   */
  public TextPlainDocumentReader(InitParams params) {
    
    ValuesParam encoding = (ValuesParam) params.getParameter(DEFAULT_ENCODING);
    
    if( encoding!=null && encoding.getValue()!= null && !encoding.getValue().equalsIgnoreCase("")){
      defaultEncoding = encoding.getValue();
    }else{
      defaultEncoding=null;
    }
  }

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
 
    if(this.defaultEncoding!=null){
      return new String(bos.toByteArray(),defaultEncoding);
    }else{
      return new String(bos.toByteArray()); //system encoding will be used
    }
  }
  
  /**
   * Returns a text from file content.
   * @param is an input stream with a file content.
   * @param encoding file content encoding. 
   * @return The string with text from file content.
   * @throws Exception
   */
  public String getContentAsText(InputStream is, String encoding) throws Exception {
    byte[] buffer = new byte[2048];
    int len;
    ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
    while ((len = is.read(buffer)) > 0) 
      bos.write(buffer, 0, len);
    bos.close();
    return new String(bos.toByteArray(),encoding);
  }
  
  /* (non-Javadoc)
   * @see org.exoplatform.services.document.DocumentReader#getProperties(java.io.InputStream)
   */
  public Properties getProperties(InputStream is) throws Exception {
    return new Properties();
  }
  
  
}
