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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.ISO8601;
import org.exoplatform.services.document.DCMetaData;
import org.exoplatform.services.log.ExoLogger;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfReader;

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
   * 
   * @see org.exoplatform.services.document.DocumentReader#getProperties(java.io.InputStream)
   */
  public Properties getProperties(InputStream is) throws Exception {

    Properties props = null;

    PdfReader reader = new PdfReader(is, "".getBytes());

    // Read the file metadata
    byte[] metadata = reader.getMetadata();
    
    if (metadata != null) {
      // there is XMP metadata try exctract it
      props = getPropertiesFromMetadata(metadata);
    } 
    
    if (props == null){
      // it's old pdf document version
      props = getPropertiesFromInfo(reader.getInfo());
    }
    reader.close();
    return props;
  }

  /**
   * Extract properties from XMP xml.
   * 
   * @param metadata XML as byte array
   * @return extracted properties
   * @throws Exception if extracting fails
   */
  protected Properties getPropertiesFromMetadata(byte[] metadata) throws Exception {
    
    Properties props = null;

    // parse xml
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = dbf.newDocumentBuilder();
    Document doc = docBuilder.parse(new ByteArrayInputStream(metadata));

    //Check is there PDF/A-1 XMP
    String  version = "";
    NodeList list = doc.getElementsByTagName("pdfaid:conformance");
    if(list !=null && list.item(0)!=null){
      version += list.item(0).getTextContent() + "-";
    }
    
    list = doc.getElementsByTagName("pdfaid:part");
    if(list !=null && list.item(0)!=null){
      version += list.item(0).getTextContent();
    }
    
    // PDF/A-1a or PDF/A-1b
    if(version.equalsIgnoreCase("A-1")){
      props = getPropsFromPDFAMetadata(doc);
    }
    
    return props;
  }

  /**
   * Extracts properties from PDF Info hash set.
   * 
   * @param Pdf Info hash set
   * @return Extracted properties
   * @throws Exception if extracting fails
   */
  protected Properties getPropertiesFromInfo(HashMap info) throws Exception {
    Properties props = new Properties();

    String title = (String) info.get("Title");
    if (title != null) {
      props.put(DCMetaData.TITLE, title);
    }

    String author = (String) info.get("Author");
    if (author != null) {
      props.put(DCMetaData.CREATOR, author);
    }

    String subject = (String) info.get("Subject");
    if (subject != null) {
      props.put(DCMetaData.SUBJECT, subject);
    }

    /*
     * String publisher = (String) info.get("Producer"); if (publisher != null) {
     * props.put(DCMetaData.PUBLISHER, publisher); } String description =
     * (String) info.get("Desc"); if (description != null) {
     * props.put(DCMetaData.DESCRIPTION, description); }
     */

    String creationDate = (String) info.get("CreationDate");
    if (creationDate != null) {
      props.put(DCMetaData.DATE, PdfDate.decode(creationDate));
    }

    String modDate = (String) info.get("ModDate");
    if (modDate != null) {
      props.put(DCMetaData.DATE, PdfDate.decode(modDate));
    }

    return props;
  }

  private Properties getPropsFromPDFAMetadata(Document doc) throws Exception{
    Properties props = new Properties();
    // get properties
    NodeList list = doc.getElementsByTagName("rdf:li");
    if (list != null && list.getLength() > 0) {
      for (int i = 0; i < list.getLength(); i++) {

        Node n = list.item(i);
        // dc:title - TITLE
        if (n.getParentNode().getParentNode().getNodeName().equals("dc:title")) {
          String title = n.getLastChild().getTextContent();
          props.put(DCMetaData.TITLE, title);
        }

        // dc:creator - CREATOR
        if (n.getParentNode().getParentNode().getNodeName().equals("dc:creator")) {
          String author = n.getLastChild().getTextContent();
          props.put(DCMetaData.CREATOR, author);
        }

        // DC:description - SUBJECT
        if (n.getParentNode().getParentNode().getNodeName().equals("dc:description")) {
          String description = n.getLastChild().getTextContent();
          props.put(DCMetaData.SUBJECT, description);
          // props.put(DCMetaData.DESCRIPTION, description);
        }
      }
    }

    // xmp:CreateDate - DATE
    list = doc.getElementsByTagName("xmp:CreateDate");
    if (list != null && list.item(0) != null) {
      Node creationDateNode = list.item(0).getLastChild();
      if (creationDateNode != null) {
        String creationDate = creationDateNode.getTextContent();
        Calendar c = ISO8601.parseEx(creationDate);
        props.put(DCMetaData.DATE, c);
      }
    }

    // xmp:ModifyDate - DATE
    list = doc.getElementsByTagName("xmp:ModifyDate");
    if (list != null && list.item(0) != null) {
      Node modifyDateNode = list.item(0).getLastChild();
      if (modifyDateNode != null) {
        String modifyDate = modifyDateNode.getTextContent();
        Calendar c = ISO8601.parseEx(modifyDate);
        props.put(DCMetaData.DATE, c);
      }
    }
    return props;
  }
  
  
}
