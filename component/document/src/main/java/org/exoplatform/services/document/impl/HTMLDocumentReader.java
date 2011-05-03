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

import org.exoplatform.container.xml.InitParams;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by The eXo Platform SAS A parser of HTML files.
 * 
 * @author <a href="mailto:zagrebin_v@mail.ru">Victor Zagrebin</a>
 * @version March 04, 2006
 */
public class HTMLDocumentReader extends BaseDocumentReader {

  /**
   * Initializes a newly created object for text/html files format parsing.
   * 
   * @param params the container parameters.
   */
  public HTMLDocumentReader(InitParams params) {
  }

  /*
   * public HTMLDocumentReader() { }
   */

  /**
   * Get the text/html mime type.
   * 
   * @return The string with text/html mime type.
   */
  public String[] getMimeTypes() {
    return new String[] { "text/html" };
  }

  /**
   * Returns a text from html file content without user's tags and their bodies.
   * 
   * @param is an input stream with html file content.
   * @return The string only with text from file content.
   */
  public String getContentAsText(InputStream is) throws Exception {
    if (!isInputStreamValid(is)) {
      return "";
    }

    String refined_text = new String();
    try {
      byte[] buffer = new byte[2048];
      int len;
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      while ((len = is.read(buffer)) > 0)
        bos.write(buffer, 0, len);
      bos.close();

      String html = new String(bos.toByteArray());

      Parser parser = Parser.createParser(html, null);
      StringBean sb = new StringBean();

      // read links or not
      // sb.setLinks(true); //TODO make this configurable

      // extract text
      parser.visitAllNodesWith(sb);

      String text = sb.getStrings();

      refined_text = (text != null) ? text : ""; // delete(text);

    } catch (IOException e) {
      return new String("");
    } catch (ParserException e) {
      return new String("");
    }

    return refined_text;
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
   * Cleans the string from users's tags and their bodies.
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
