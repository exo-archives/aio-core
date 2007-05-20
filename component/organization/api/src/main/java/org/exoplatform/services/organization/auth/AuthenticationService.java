/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.auth;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.organization.OrganizationService;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * May 17, 2007  
 */
public class AuthenticationService {
  private ThreadLocal <Identity> currentIdentity_ = new ThreadLocal <Identity>();
  private Map<String, Identity>  identities_ = new HashMap<String, Identity>() ;;
  private ListenerService listenerService_ ;
  private OrganizationService orgService_ ;
  
  public AuthenticationService(ListenerService listenerService, OrganizationService orgService)  {
    listenerService_ = listenerService ;
    orgService_ =  orgService ;
  }
  
  public boolean login(Identity identity) throws Exception {
    if(orgService_.getUserHandler().authenticate(identity.getUsername(), identity.getPassword())) {
      identities_.put(identity.getSessionId(), identity) ;
      listenerService_.broadcast("exo.service.authentication.login", this, identity) ;
      return true ;
    } else {
      return false ;
    }
  }
  
  public Identity getIdentityBySessionId(String sessionId) throws Exception {
    return  identities_.get(sessionId) ;
  }
  
  public Identity getCurrentIdentity() { return currentIdentity_.get() ; }
  public void setCurrentIdentity(Identity identity) { currentIdentity_.set(identity) ; }
  
  public void logout(String sessionId) throws Exception {
    Identity identity =  identities_.remove(sessionId) ;
    if(identity == null) {
      throw new Exception("Cannot find the subject for the " + sessionId) ;
    } else {
      listenerService_.broadcast("exo.service.authentication.logout", this, identity) ;
    }
  }
  
  public OrganizationService getOrganizationService() { return orgService_ ; }
}