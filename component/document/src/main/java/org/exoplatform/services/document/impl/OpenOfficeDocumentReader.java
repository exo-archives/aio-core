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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.exoplatform.commons.utils.QName;
import org.exoplatform.services.document.DCMetaData;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public class OpenOfficeDocumentReader extends BaseDocumentReader {

  /*
   * (non-Javadoc)
   * 
   * @see org.exoplatform.services.document.DocumentReader#getMimeTypes()
   */
  public String[] getMimeTypes() {
    return new String[] {
        "application/vnd.oasis.opendocument.database",
        "application/vnd.oasis.opendocument.formula",
        "application/vnd.oasis.opendocument.graphics",
        "application/vnd.oasis.opendocument.presentation",
        "application/vnd.oasis.opendocument.spreadsheet",
        "application/vnd.oasis.opendocument.text" };
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.exoplatform.services.document.DocumentReader#getContentAsText(java.io.InputStream)
   */
  public String getContentAsText(InputStream is) throws Exception {
    try {
      SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
      saxParserFactory.setValidating(false);
      SAXParser saxParser = saxParserFactory.newSAXParser();
      XMLReader xmlReader = saxParser.getXMLReader();
      xmlReader.setFeature("http://xml.org/sax/features/validation", false);
      xmlReader.setFeature(
          "http://apache.org/xml/features/nonvalidating/load-external-dtd",
          false);

      ZipInputStream zis = new ZipInputStream(is);
      ZipEntry ze = zis.getNextEntry();
      while (!ze.getName().equals("content.xml")) {
        ze = zis.getNextEntry();
      }

      OpenOfficeContentHandler contentHandler = new OpenOfficeContentHandler();
      xmlReader.setContentHandler(contentHandler);
      try {
        xmlReader.parse(new InputSource(zis));
      } finally {
        zis.close();
      }

      return contentHandler.getContent();
//    } catch (ParserConfigurationException e) {
//      return "";
//    } catch (SAXException e) {
//      return "";
    } finally {
      is.close();
    }
  }

  public String getContentAsText(InputStream is, String encoding) throws Exception {
    //Ignore encoding
    return getContentAsText(is);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.exoplatform.services.document.DocumentReader#getProperties(java.io.InputStream)
   */
  public Properties getProperties(InputStream is) throws Exception {
    try {
      SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
      saxParserFactory.setValidating(false);
      SAXParser saxParser = saxParserFactory.newSAXParser();
      XMLReader xmlReader = saxParser.getXMLReader();
      xmlReader.setFeature("http://xml.org/sax/features/validation", false);
      xmlReader.setFeature(
          "http://apache.org/xml/features/nonvalidating/load-external-dtd",
          false);

      ZipInputStream zis = new ZipInputStream(is);
      ZipEntry ze = zis.getNextEntry();
      while (!ze.getName().equals("meta.xml")) {
        ze = zis.getNextEntry();
      }

      OpenOfficeMetaHandler metaHandler = new OpenOfficeMetaHandler();
      xmlReader.setContentHandler(metaHandler);
      try {
        xmlReader.parse(new InputSource(zis));
      } finally {
        zis.close();
      }

      return metaHandler.getProperties();
      
    } finally {
      is.close();
    }
  }

  
  // --------------------------------------------< OpenOfficeContentHandler >

  private class OpenOfficeContentHandler extends DefaultHandler {

    private StringBuffer content;

    private boolean appendChar;

    public OpenOfficeContentHandler() {
      content = new StringBuffer();
      appendChar = false;
    }

    /**
     * Returns the text content extracted from parsed content.xml
     */
    public String getContent() {
      return content.toString();
    }

    public void startElement(String namespaceURI, String localName,
        String rawName, Attributes atts) throws SAXException {
      if (rawName.startsWith("text:")) {
        appendChar = true;
      }
    }

    public void characters(char[] ch, int start, int length)
        throws SAXException {
      if (appendChar) {
        content.append(ch, start, length).append(" ");
      }
    }

    public void endElement(java.lang.String namespaceURI,
        java.lang.String localName, java.lang.String qName) throws SAXException {
      appendChar = false;
    }
  }
  
  private class OpenOfficeMetaHandler extends DefaultHandler {

    private Properties props;

    private QName curPropertyName;
    
    private StringBuffer curPropertyValue;
    
    public OpenOfficeMetaHandler() {
      props = new Properties();
      curPropertyValue = new StringBuffer();
    }

    public Properties getProperties() {
      return props;
    }

    public void startElement(String namespaceURI, String localName,
        String rawName, Attributes atts) throws SAXException {
      if (rawName.startsWith("dc:")) {
        curPropertyName = new QName(DCMetaData.DC_NAMESPACE, rawName.substring(3));
      }
    }

    public void characters(char[] ch, int start, int length)
        throws SAXException {
      if(curPropertyName != null) {
        curPropertyValue.append(ch, start, length);
      }
    }

    public void endElement(java.lang.String namespaceURI,
        java.lang.String localName, java.lang.String qName) throws SAXException {
      if(curPropertyName != null) {
        props.put(curPropertyName, curPropertyValue.toString());
        curPropertyValue = new StringBuffer();
        curPropertyName = null;
      }
    }
  }

}
