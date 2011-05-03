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

import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by The eXo Platform SAS Author : Sergey Karpenko
 * <sergey.karpenko@exoplatform.com.ua>
 * 
 * @version $Id: $
 */
public class MSOutlookDocumentReader extends BaseDocumentReader {

  /**
   * Get the application/msword mime type.
   * 
   * @return The application/msword mime type.
   */
  public String[] getMimeTypes() {
    return new String[] { "application/vnd.ms-outlook" };
  }

  /**
   * Force loading of dependent class.
   */
  static {
    MAPIMessage.class.getName();
  }

  public String getContentAsText(InputStream is) throws Exception {
    if (!isInputStreamValid(is)) {
      return "";
    }

    try {
      MAPIMessage message = new MAPIMessage(is);
      StringBuffer buffer = new StringBuffer();
      try {
        buffer.append(message.getDisplayFrom()).append('\n');
      } catch (ChunkNotFoundException e) {
        // "from" is empty
      }
      try {
        buffer.append(message.getDisplayTo()).append('\n');
      } catch (ChunkNotFoundException e) {
        // "to" is empty
      }
      try {
        buffer.append(message.getSubject()).append('\n');
      } catch (ChunkNotFoundException e) {
        // "subject" is empty
      }
      buffer.append(message.getTextBody());
      return buffer.toString();
    } catch (Exception e) {
      // e.printStackTrace();
      return "";
    } finally {
      is.close();
    }
  }

  public String getContentAsText(InputStream is, String encoding) throws Exception {
    // ignore encoding
    return getContentAsText(is);
  }

  public Properties getProperties(InputStream is) throws Exception {
    return new Properties();
  }

}
