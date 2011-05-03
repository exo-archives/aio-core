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

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by The eXo Platform SAS A parser of Microsoft Word files.
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
    return new String[] { "application/msword", "application/msworddoc", "application/msworddot" };
  }

  /**
   * Returns only a text from .doc file content.
   * 
   * @param is an input stream with .doc file content.
   * @return The string only with text from file content.
   * @throws Exception
   */
  public String getContentAsText(InputStream is) throws Exception {
    if (!isInputStreamValid(is)) {
      return "";
    }

    String text = "";
    try {
      HWPFDocument doc = new HWPFDocument(is);
      // WordExtractor extr = new WordExtractor(doc);

      Range range = doc.getRange();
      text = range.text();
    } catch (IOException e) {
    }
    return text.trim();
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
    if (!isInputStreamValid(is)) {
      return new Properties();
    }

    POIPropertiesReader reader = new POIPropertiesReader();
    reader.readDCProperties(is);
    return reader.getProperties();
  }

}
