/***************************************************************************
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform;

import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: AbstractTransformer.java 5799 2006-05-28 17:55:42Z geaz $
 */

public interface AbstractTransformer {
  /**
   * Initialize a result of transformation
   * 
   * @param result
   *          Result
   * @throws NotSupportedIOTypeException
   *           if try to initialize with not supported implementation of Result
   */
  void initResult(Result result) throws NotSupportedIOTypeException;

  /**
   * Transform source data to result
   * 
   * @param source
   *          Source - input of transformation
   * 
   * @throws NotSupportedIOTypeException
   *           if not supported implementation of Source
   * @throws TransformerException
   *           if error occurred on transformation process
   * @throws IllegalStateException
   *           if result not initialized by initResult
   */
  void transform(Source source) throws NotSupportedIOTypeException,
      TransformerException, IllegalStateException;
}
