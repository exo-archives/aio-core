/**
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.document;
/**
 * Created by The eXo Platform SARL        .
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public class HandlerNotFoundException extends Exception {

  public HandlerNotFoundException() {
    super();
  }

  public HandlerNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public HandlerNotFoundException(String message) {
    super(message);
  }

  public HandlerNotFoundException(Throwable cause) {
    super(cause);
  }

}
