/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SAS         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;
import javax.security.auth.Subject;

/**
 * Bean for authentication. Remote interface.
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public interface ExoEJBLogin extends EJBObject {

  public Subject authenticate(String user, char[] pass) throws RemoteException;

}
