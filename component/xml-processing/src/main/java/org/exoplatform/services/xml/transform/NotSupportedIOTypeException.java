/***************************************************************************
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NotSupportedIOTypeException.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class NotSupportedIOTypeException extends Exception {

  /** Constructs an Exception without a message. */
  public NotSupportedIOTypeException() {
    super();
  }

  /** Constructs an Exception with a message. */
  public NotSupportedIOTypeException(Result result) {
    super("Result type " + result.getClass().getName()
        + " is not supported by this transformer.");
  }

  /** Constructs an Exception with a message. */
  public NotSupportedIOTypeException(Source source) {
    super("Source type " + source.getClass().getName()
        + " is not supported by this transformer.");
  }

  /**
   * Constructs an Exception with a detailed message.
   * 
   * @param Message
   *          The message associated with the exception.
   */
  public NotSupportedIOTypeException(String message) {
    super(message);
  }
}
