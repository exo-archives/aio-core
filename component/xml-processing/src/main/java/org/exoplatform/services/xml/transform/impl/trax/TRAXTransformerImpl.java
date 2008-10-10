/**
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

package org.exoplatform.services.xml.transform.impl.trax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.exoplatform.services.xml.transform.NotSupportedIOTypeException;
import org.exoplatform.services.xml.transform.impl.TransformerBase;
import org.exoplatform.services.xml.transform.trax.TRAXTransformer;

/**
 * Created by The eXo Platform SAS . Implementation of Trax Transformer
 * interface
 * 
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: TRAXTransformerImpl.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class TRAXTransformerImpl extends TransformerBase implements TRAXTransformer {

  protected TransformerHandler tHandler;

  protected Transformer getTransformer() {
    return tHandler.getTransformer();
  }

  public TRAXTransformerImpl() throws TransformerConfigurationException {
    SAXTransformerFactory saxTFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
    tHandler = saxTFactory.newTransformerHandler();
  }

  public TRAXTransformerImpl(Source source) throws TransformerConfigurationException {
    SAXTransformerFactory saxTFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
    tHandler = saxTFactory.newTransformerHandler(source);
  }

  public TRAXTransformerImpl(Templates templates) throws TransformerConfigurationException {
    SAXTransformerFactory saxTFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
    tHandler = saxTFactory.newTransformerHandler(templates);
  }

  protected void internalTransform(Source source) throws TransformerException,
                                                 NotSupportedIOTypeException,
                                                 IllegalStateException {

    XMLReader xmlReader = null;

    try {
      // xmlReader = XMLReaderFactory.
      // createXMLReader("org.apache.xerces.parsers.SAXParser");
      xmlReader = getXMLReader();
      // set default resolver
      if (resolvingService != null) {
        xmlReader.setEntityResolver(resolvingService.getEntityResolver());
        log.debug("Set entity resolver");
      }
    } catch (SAXException ex) {
      throw new TransformerException(ex);
    }

    xmlReader.setContentHandler(tHandler);
    // tHandler.setResult(getResult());

    InputSource inputSource = null;
    // todo simplify
    // SAXSource.sourceToInputSource(Source source) from JSDK does not supported
    // DOMSource
    if (source instanceof DOMSource) {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      SAXTransformerFactory.newInstance()
                           .newTransformer()
                           .transform(source, new StreamResult(outputStream));
      inputSource = new InputSource(new ByteArrayInputStream(outputStream.toByteArray()));
    } else {
      inputSource = SAXSource.sourceToInputSource(source);
    }
    if (inputSource == null) {
      throw new NotSupportedIOTypeException(source);
    }
    try {
      xmlReader.parse(inputSource);
    } catch (SAXException ex) {
      throw new TransformerException(ex);
    } catch (IOException ex) {
      throw new TransformerException(ex);
    }

  }

  protected void afterInitResult() {
    tHandler.setResult(getResult());
  }

  // delegation to Transformer, see getTransformer()
  public Result getTransformerAsResult() {
    return new SAXResult(tHandler);
  }

  public Object getParameter(String param) {
    return getTransformer().getParameter(param);
  }

  public void setParameter(String name, Object value) {
    getTransformer().setParameter(name, value);
  }

  public void clearParameters() {
    getTransformer().clearParameters();
  }

  public String getOutputProperty(String prop) {
    return getTransformer().getOutputProperty(prop);
  }

  public void setOutputProperty(String name, String value) {
    getTransformer().setOutputProperty(name, value);
  }

  public void setOutputProperties(Properties props) {
    getTransformer().setOutputProperties(props);
  }

  public Properties getOutputProperties() {
    return getTransformer().getOutputProperties();
  }

  public URIResolver getURIResolver() {
    return getTransformer().getURIResolver();
  }

  public void setURIResolver(URIResolver resolver) {
    getTransformer().setURIResolver(resolver);
  }

  public ErrorListener getErrorListener() {
    return getTransformer().getErrorListener();
  }

  public void setErrorListener(ErrorListener listener) {
    getTransformer().setErrorListener(listener);
  }

}
