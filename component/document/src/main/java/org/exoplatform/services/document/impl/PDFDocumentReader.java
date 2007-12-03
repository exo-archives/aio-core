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
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.exoplatform.services.document.DCMetaData;
import org.exoplatform.services.log.ExoLogger;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentInformation;
import org.pdfbox.util.PDFTextStripper;

/**
 * Created by The eXo Platform SAS
 * 
 * A parser of Adobe PDF files.
 * 
 * @author Phung Hai Nam
 * @author Gennady Azarenkov
 * @version Oct 19, 2005
 */
public class PDFDocumentReader extends BaseDocumentReader {

  protected static Log log = ExoLogger.getLogger("platform.PDFDocumentReader");

  /**
   * Get the application/pdf mime type.
   * 
   * @return The application/pdf mime type.
   */
  public String[] getMimeTypes() {
    return new String[] { "application/pdf" };
  }

  /**
   * Returns only a text from .pdf file content.
   * 
   * @param is
   *          an input stream with .pdf file content.
   * @return The string only with text from file content.
   * @throws Exception
   */
  public String getContentAsText(InputStream is) throws Exception {
    PDDocument pdDocument = PDDocument.load(is);
    StringWriter sw = new StringWriter();
    try {
      PDFTextStripper stripper = new PDFTextStripper();
      stripper.setStartPage(1);
      stripper.setEndPage(Integer.MAX_VALUE);
      stripper.writeText(pdDocument, sw);
    } finally {
      if (pdDocument != null)
        pdDocument.close();
    }
    return sw.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.exoplatform.services.document.DocumentReader#getProperties(java.io.InputStream)
   */
  public Properties getProperties(InputStream is) throws Exception {

    PDDocument pdDocument = PDDocument.load(is);
    PDDocumentInformation docInfo = pdDocument.getDocumentInformation();
    Properties props = new Properties();
    
    try {
      try {
        if (docInfo.getAuthor() != null)
          props.put(DCMetaData.CONTRIBUTOR, docInfo.getAuthor());
      } catch (Exception e) {
        log.warn("getAuthor failed: " + e);
      }
      try {

        if (docInfo.getCreationDate() != null)
          props.put(DCMetaData.DATE, docInfo.getCreationDate());
      } catch (Exception e) {
        log.warn("getCreationDate failed: " + e);
      }
      try {

        if (docInfo.getCreator() != null)
          props.put(DCMetaData.CREATOR, docInfo.getCreator());
      } catch (Exception e) {
        log.warn("getCreator failed: " + e);
      }
      try {

        if (docInfo.getKeywords() != null)
          props.put(DCMetaData.SUBJECT, docInfo.getKeywords());
      } catch (Exception e) {
        log.warn("getKeywords failed: " + e);
      }
      try {

        if (docInfo.getModificationDate() != null)
          props.put(DCMetaData.DATE, docInfo.getModificationDate());
      } catch (Exception e) {
        log.warn("getModificationDate failed: " + e);
      }
      try {

        if (docInfo.getProducer() != null)
          props.put(DCMetaData.PUBLISHER, docInfo.getProducer());
      } catch (Exception e) {
        log.warn("getProducer failed: " + e);
      }
      try {

        if (docInfo.getSubject() != null)
          props.put(DCMetaData.DESCRIPTION, docInfo.getSubject());
      } catch (Exception e) {
        log.warn("getSubject failed: " + e);
      }
      try {

        if (docInfo.getTitle() != null)
          props.put(DCMetaData.TITLE, docInfo.getTitle());
      } catch (Exception e) {
        log.warn("getTitle failed: " + e);
      }

      // docInfo.getTrapped();
    } finally {
      if (pdDocument != null)
        pdDocument.close();
    }

    return props;
  }

}
