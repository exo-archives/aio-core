/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.document;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by The eXo Platform SARL
 * Author : Nam
 * @author Gennady Azarenkov
 * @version $Id: DocumentReader.java 11659 2007-01-05 15:35:06Z geaz $
 */
 public interface DocumentReader  {
   
  /**
   * @return all appropriate mime types
   */
  String[] getMimeTypes() ;
  
  /**
   * @param is
   * @return document content
   * @throws Exception
   */
  String getContentAsText(InputStream is) throws Exception ;
  
  /**
   * @param mimeType
   * @return metainfo properties reduced to some supported metadata set (Dublin Core or other)
   */
  Properties getProperties(InputStream is) throws Exception;

}
