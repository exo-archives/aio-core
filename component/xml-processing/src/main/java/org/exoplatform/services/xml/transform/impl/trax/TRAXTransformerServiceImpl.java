/***************************************************************************
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform.impl.trax;

import java.io.IOException;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;

import org.exoplatform.services.xml.transform.impl.trax.TRAXTransformerImpl;
import org.exoplatform.services.xml.transform.NotSupportedIOTypeException;
import org.exoplatform.services.xml.transform.trax.TRAXTemplates;
import org.exoplatform.services.xml.transform.trax.TRAXTransformerService;
import org.exoplatform.services.xml.transform.trax.TRAXTransformer;
import org.exoplatform.services.xml.resolving.XMLResolvingService;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: TRAXTransformerServiceImpl.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class TRAXTransformerServiceImpl implements TRAXTransformerService {

  private XMLResolvingService resolvingService;

  public TRAXTransformerServiceImpl(XMLResolvingService resolvingService) {
    this.resolvingService = resolvingService;
  }

  public TRAXTransformer getTransformer()
      throws TransformerConfigurationException {
    TRAXTransformerImpl transf = new TRAXTransformerImpl();
    transf.resolvingService = resolvingService;
    return transf;
  }

  public TRAXTransformer getTransformer(Source source)
      throws TransformerConfigurationException {
    TRAXTransformerImpl transf = new TRAXTransformerImpl(source);
    transf.resolvingService = resolvingService;
    return transf;
  }

  public TRAXTemplates getTemplates(Source source) throws TransformerException,
      NotSupportedIOTypeException {
    TRAXTemplatesImpl templates = new TRAXTemplatesImpl(getXSLTemplates(source));
    templates.resolvingService = resolvingService;
    return templates;
  }

  private Templates getXSLTemplates(Source source) throws TransformerException,
      NotSupportedIOTypeException {
    SAXTransformerFactory saxTFactory = (SAXTransformerFactory) SAXTransformerFactory
        .newInstance();

    TemplatesHandler templateHandler = saxTFactory.newTemplatesHandler();
    XMLReader xmlReader;
    try {
      // xmlReader = XMLReaderFactory.
      // createXMLReader("org.apache.xerces.parsers.SAXParser");
      xmlReader = TRAXTransformerImpl.getXMLReader();
      // set default resolver
      if (resolvingService != null) {
        xmlReader.setEntityResolver(resolvingService.getEntityResolver());
      }

    } catch (SAXException ex) {
      throw new TransformerException(ex);
    }

    xmlReader.setContentHandler(templateHandler);
    InputSource inputSource = SAXSource.sourceToInputSource(source);
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

    return templateHandler.getTemplates();

  }

}
