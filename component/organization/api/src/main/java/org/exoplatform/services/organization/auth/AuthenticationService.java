/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.auth;

import org.exoplatform.services.organization.OrganizationService;

/**
 * Created by The eXo Platform SAS
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * May 17, 2007
 */
public interface AuthenticationService {
    
  public boolean login(String userName, String password) throws Exception;
  
  public void broadcastAuthentication(Identity identity) throws Exception;

  public Identity getIdentityBySessionId(String sessionId) throws Exception;
  
  public Identity getCurrentIdentity();
  
  public void setCurrentIdentity(Identity identity);
  
  public void logout(String sessionId) throws Exception;
  
  public OrganizationService getOrganizationService();
}