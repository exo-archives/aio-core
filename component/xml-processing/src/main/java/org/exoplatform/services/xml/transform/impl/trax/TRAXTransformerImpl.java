/***************************************************************************
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform.impl.trax;

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
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

import org.exoplatform.services.xml.transform.NotSupportedIOTypeException;
import org.exoplatform.services.xml.transform.impl.TransformerBase;
import org.exoplatform.services.xml.transform.trax.TRAXTransformer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

// import org.exoplatform.services.xml.transform.TransformRules;

/**
 * Created by The eXo Platform SARL .
 * 
 * Implementation of Trax Transformer interface
 * 
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: TRAXTransformerImpl.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class TRAXTransformerImpl extends TransformerBase implements
    TRAXTransformer {

  // private SAXTransformerFactory saxTFactory;
  protected TransformerHandler tHandler;

  protected Transformer getTransformer() {
    return tHandler.getTransformer();
  }

  public TRAXTransformerImpl() throws TransformerConfigurationException {
    SAXTransformerFactory saxTFactory = (SAXTransformerFactory) SAXTransformerFactory
        .newInstance();
    tHandler = saxTFactory.newTransformerHandler();
  }

  public TRAXTransformerImpl(Source source)
      throws TransformerConfigurationException {
    SAXTransformerFactory saxTFactory = (SAXTransformerFactory) SAXTransformerFactory
        .newInstance();
    tHandler = saxTFactory.newTransformerHandler(source);
  }

  public TRAXTransformerImpl(Templates templates)
      throws TransformerConfigurationException {
    SAXTransformerFactory saxTFactory = (SAXTransformerFactory) SAXTransformerFactory
        .newInstance();
    tHandler = saxTFactory.newTransformerHandler(templates);
  }

  protected void internalTransform(Source src) throws TransformerException,
      NotSupportedIOTypeException, IllegalStateException {

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

    // todo simplify
    InputSource inputSource = SAXSource.sourceToInputSource(src);
    // InputSource inputSource = null;
    // if (src instanceof StreamSource) {
    // inputSource = new InputSource(((StreamSource) src).getInputStream());
    // }
    // else {
    // inputSource = SAXSource.sourceToInputSource(src);
    // }
    //
    // if (inputSource == null) {
    // throw new NotSupportedIOTypeException(src);
    // }

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
