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

import java.io.InputStream;
import java.util.Properties;

import org.apache.poi.hslf.extractor.PowerPointExtractor;

/**
 * Created by The eXo Platform SAS A parser of Microsoft PowerPoint files.
 * Parses the files with application/powerpoint mime type.
 * 
 * @author <a href="mailto:phunghainam@gmail.com">Phung Hai Nam</a>
 * @author Gennady Azarenkov
 * @version Oct 19, 2005
 */
public class PPTDocumentReader extends BaseDocumentReader {

  /**
   * Get the application/powerpoint mime type.
   * 
   * @return The application/powerpoint mime type.
   */
  public String[] getMimeTypes() {
    return new String[] { "application/powerpoint", "application/ppt" };
  }

  /**
   * Returns only a text from .ppt file content.
   * 
   * @param is an input stream with .ppt file content.
   * @return The string only with text from file content.
   * @throws Exception
   */
  public String getContentAsText(InputStream is) throws Exception {
    PowerPointExtractor ppe = new PowerPointExtractor(is);
    return ppe.getText(true, true);
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
    POIPropertiesReader reader = new POIPropertiesReader();
    reader.readDCProperties(is);
    return reader.getProperties();
  }

}
