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

import org.apache.commons.logging.Log;
import org.exoplatform.services.document.DCMetaData;
import org.exoplatform.services.log.ExoLogger;
import org.jempbox.xmp.XMPMetadata;
import org.jempbox.xmp.XMPSchemaBasic;
import org.jempbox.xmp.XMPSchemaDublinCore;
import org.pdfbox.exceptions.InvalidPasswordException;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentCatalog;
import org.pdfbox.pdmodel.PDDocumentInformation;
import org.pdfbox.pdmodel.common.PDMetadata;
import org.pdfbox.util.PDFTextStripper;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

/**
 * Created by The eXo Platform SAS A parser of Adobe PDF files.
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
   * Returns only a text from pdf file content.
   * 
   * @param is an input stream with .pdf file content.
   * @return The string only with text from file content.
   * @throws Exception
   */
  public String getContentAsText(InputStream is) throws Exception {
    if (!isInputStreamValid(is)) {
      return "";
    }

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

    PDDocument pdDocument = PDDocument.load(is);
    Properties props = new Properties();
    try {
      if (pdDocument.isEncrypted()) {
        try {
          pdDocument.decrypt("");
        } catch (InvalidPasswordException e) {
          throw new Exception("The pdf document is encrypted.");
        } catch (org.pdfbox.exceptions.CryptographyException e) {
          throw e;
        }
      }

      PDDocumentCatalog catalog = pdDocument.getDocumentCatalog();
      PDMetadata meta = catalog.getMetadata();
      if (meta != null) {
        XMPMetadata metadata = XMPMetadata.load(meta.createInputStream());

        XMPSchemaDublinCore dc = metadata.getDublinCoreSchema();
        if (dc != null) {
          try {
            if (dc.getTitle() != null)
              props.put(DCMetaData.TITLE, dc.getTitle());
          } catch (Exception e) {
            log.warn("getTitle failed: " + e);
          }
          try {
            if (dc.getDescription() != null)
              props.put(DCMetaData.SUBJECT, dc.getDescription());
          } catch (Exception e) {
            log.warn("getSubject failed: " + e);
          }

          try {
            if (dc.getCreators() != null) {
              List<String> list = dc.getCreators();
              for (String creator : list) {
                props.put(DCMetaData.CREATOR, creator);
              }
            }
          } catch (Exception e) {
            log.warn("getCreator failed: " + e);
          }

          try {
            if (dc.getDates() != null) {
              List<Calendar> list = dc.getDates();

              for (Calendar date : list) {
                props.put(DCMetaData.DATE, date);
              }
            }
          } catch (Exception e) {
            log.warn("getDate failed: " + e);
          }
        }

        XMPSchemaBasic basic = metadata.getBasicSchema();
        if (basic != null) {
          try {
            if (basic.getCreateDate() != null)
              props.put(DCMetaData.DATE, basic.getCreateDate());
          } catch (Exception e) {
            log.warn("getCreationDate failed: " + e);
          }
          try {
            if (basic.getModifyDate() != null)
              props.put(DCMetaData.DATE, basic.getModifyDate());
          } catch (Exception e) {
            log.warn("getModificationDate failed: " + e);
          }
        }
      }

      if (props.isEmpty()) {
        // The pdf doesn't contain any metadata or metadata do not contains any
        // usefull data, try to use the document
        // information instead
        PDDocumentInformation docInfo = pdDocument.getDocumentInformation();

        if (docInfo != null) {
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
        }
      }

    } finally {
      if (pdDocument != null) {
        pdDocument.close();
      }
    }

    return props;
  }
}
