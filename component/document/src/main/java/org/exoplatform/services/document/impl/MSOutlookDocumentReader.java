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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.exoplatform.services.document.DocumentReadException;

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

  public String getContentAsText(InputStream is) throws IOException, DocumentReadException {
    if (is == null) {
      throw new NullPointerException("InputStream is null.");
    }
    try {
      MAPIMessage message;
      try{
        message = new MAPIMessage(is);
      }catch(IOException e){
        return "";
      }
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
      try {
        buffer.append(message.getTextBody());
      } catch (ChunkNotFoundException e) {
        // "textBody" is empty
      }
      return buffer.toString();

    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
        }
      }
    }
  }

  public String getContentAsText(InputStream is, String encoding) throws IOException,
                                                                 DocumentReadException {
    // ignore encoding
    return getContentAsText(is);
  }

  public Properties getProperties(InputStream is) throws IOException, DocumentReadException {
    try {
      is.close();
    } catch (IOException e) {
    }
    return new Properties();
  }

}
