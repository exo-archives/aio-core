/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.document;

import java.io.InputStream;

/**
 * Created by The eXo Platform SARL
 * @author Gennady Azarenkov
 * @version $Id: DocumentReaderService.java 11659 2007-01-05 15:35:06Z geaz $
 */
public interface DocumentReaderService  {
  
  /**
   * @deprecated 
   */
  String getContentAsText(String  mimeType, InputStream is) throws Exception ;
  
  /**
   * @param mimeType
   * @return appropriate document reader
   */
  DocumentReader getDocumentReader(String  mimeType) throws HandlerNotFoundException;


}