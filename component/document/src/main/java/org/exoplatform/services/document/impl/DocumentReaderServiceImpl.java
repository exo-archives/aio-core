/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.document.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.document.DocumentReader;
import org.exoplatform.services.document.DocumentReaderService;
import org.exoplatform.services.document.HandlerNotFoundException;
/**
 * Created by The eXo Platform SARL
 * Author : Phung Hai Nam
 * @author Gennady Azarenkov
 * @version $Id: DocumentReaderServiceImpl.java 11659 2007-01-05 15:35:06Z geaz $
 * 
 * Oct 19, 2005
 */
public class DocumentReaderServiceImpl implements DocumentReaderService {  
  private Map <String, BaseDocumentReader> readers_;
  
  public DocumentReaderServiceImpl(InitParams params){
    readers_ = new HashMap<String, BaseDocumentReader>();
  }
  
  public String getContentAsText(String  mimeType, InputStream is) throws Exception {    
    BaseDocumentReader reader = readers_.get(mimeType.toLowerCase());   
    if(reader != null)  return reader.getContentAsText(is);
    throw new Exception("Cannot handle the document type: " + mimeType);
  }
  
  /**
   * @param plugin
   */
  public void addDocumentReader(ComponentPlugin plugin) {
    BaseDocumentReader reader = (BaseDocumentReader) plugin;
    for(String mimeType:reader.getMimeTypes())
      readers_.put(mimeType.toLowerCase(), reader);
  }
  
  /* (non-Javadoc)
   * @see org.exoplatform.services.document.DocumentReaderService#getDocumentReader(java.lang.String)
   */
  public DocumentReader getDocumentReader(String  mimeType) throws HandlerNotFoundException {
    BaseDocumentReader reader = readers_.get(mimeType.toLowerCase());   
    if(reader != null)  
      return reader;
    else
      throw new HandlerNotFoundException("No appropriate properties extractor for " + mimeType);
  }
  
}