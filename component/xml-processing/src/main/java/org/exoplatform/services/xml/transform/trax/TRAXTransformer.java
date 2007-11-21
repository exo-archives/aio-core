/***************************************************************************
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform.trax;

import javax.xml.transform.ErrorListener;
import java.util.Properties;
import javax.xml.transform.URIResolver;
import org.exoplatform.services.xml.transform.PipeTransformer;

/**
 * Created by The eXo Platform SARL.
 * 
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: TRAXTransformer.java 5799 2006-05-28 17:55:42Z geaz $
 */
public interface TRAXTransformer extends PipeTransformer {
  /**
   * @see javax.xml.transform.Transformer#getParameter(java.lang.String)
   */
  Object getParameter(String param);

  /**
   * @see javax.xml.transform.Transformer#setParameter(java.lang.String,
   *      java.lang.Object)
   */
  void setParameter(String name, Object value);

  /**
   * @see javax.xml.transform.Transformer#clearParameters()
   */
  void clearParameters();

  /**
   * @see javax.xml.transform.Transformer#getOutputProperty(java.lang.String)
   */
  String getOutputProperty(String prop);

  /**
   * @see javax.xml.transform.Transformer#setOutputProperty(java.lang.String,
   *      java.lang.String)
   */
  void setOutputProperty(String name, String value);

  /**
   * @see javax.xml.transform.Transformer#setOutputProperties(java.util.Properties)
   */
  void setOutputProperties(Properties props);

  /**
   * @see javax.xml.transform.Transformer#getOutputProperties()
   */
  Properties getOutputProperties();

  /**
   * @see javax.xml.transform.Transformer#getURIResolver()
   */
  URIResolver getURIResolver();

  /**
   * 
   * @see javax.xml.transform.Transformer#setURIResolver(javax.xml.transform.URIResolver)
   */
  void setURIResolver(URIResolver resolver);

  /**
   * @see javax.xml.transform.Transformer#getErrorListener()
   */
  ErrorListener getErrorListener();

  /**
   * @see javax.xml.transform.Transformer#setErrorListener(javax.xml.transform.ErrorListener)
   */
  void setErrorListener(ErrorListener listener);

}
