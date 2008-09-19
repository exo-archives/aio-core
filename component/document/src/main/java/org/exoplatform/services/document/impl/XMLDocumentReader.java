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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by The eXo Platform SAS A parser of XML files.
 * 
 * @author <a href="mailto:zagrebin_v@mail.ru">Victor Zagrebin</a>
 * @version March 07, 2006
 */
public class XMLDocumentReader extends BaseDocumentReader {

  // /**
  // * Initializes a newly created object for text/xml files type parsing.
  // * @param params the container parameters.
  // */
  // public XMLDocumentReader(InitParams params) {
  // }

  /**
   * Get the text/xml mime type.
   * 
   * @return The string with text/xml mime type.
   */
  public String[] getMimeTypes() {
    return new String[] { "text/xml" };
  }

  /**
   * Returns a text from xml file content which situated between tags.
   * 
   * @param is an input stream with html file content.
   * @return The string only with text from file content.
   */
  public String getContentAsText(InputStream is) throws Exception {
    byte[] buffer = new byte[2048];
    int len;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    while ((len = is.read(buffer)) > 0)
      bos.write(buffer, 0, len);
    bos.close();
    String xml = new String(bos.toByteArray());
    return delete(xml);
  }

  public String getContentAsText(InputStream is, String encoding) throws Exception {
    // Ignore encoding
    return getContentAsText(is);
  }

  /*
   * (non-Javadoc)
   * @see
   * org.exoplatform.services.document.DocumentReader#getProperties(java.io.
   * InputStream)
   */
  public Properties getProperties(InputStream is) throws Exception {
    return new Properties();
  }

  /**
   * Cleans the string from tags.
   * 
   * @param str the string which contain a text with user's tags.
   * @return The string cleaned from user's tags and their bodies.
   */
  private String delete(String str) {
    try {
      StringBuffer input = new StringBuffer(str);
      String patternString = "<+[^>]*>+";
      Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
      Matcher matcher = pattern.matcher(input);
      while (matcher.find()) {
        int start = matcher.start();
        int end = matcher.end();
        input.delete(start, end);
        matcher = pattern.matcher(input);
      }
      return input.substring(0, input.length());
    } catch (PatternSyntaxException e) {
    }
    return "";
  }

}
