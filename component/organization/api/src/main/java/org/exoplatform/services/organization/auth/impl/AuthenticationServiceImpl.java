/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SAS         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.auth.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.auth.AuthenticationService;
import org.exoplatform.services.organization.auth.Identity;

/**
 * Created by The eXo Platform SAS
 * May 17, 2007  
 */
public class AuthenticationServiceImpl implements AuthenticationService {
  
  protected static Log log = ExoLogger.getLogger("authenticationService");
  
  private ThreadLocal <Identity> currentIdentity_ = new ThreadLocal <Identity>();
  private Map<String, Identity>  identities_ = new HashMap<String, Identity>() ;;
  private ListenerService listenerService_ ;
  private OrganizationService orgService_ ;
  
  public AuthenticationServiceImpl(ListenerService listenerService, OrganizationService orgService)  {
    log.info("Start AuthenticationService init ...................... ");
    listenerService_ = listenerService ;
    orgService_ =  orgService ;
    log.info("End AuthenticationService init ...................... ");
  }
  
  public boolean login(String userName, String password) throws Exception {
    if(orgService_.getUserHandler().authenticate(userName, password)) {
      return true ;
    } else {
      return false ;
    }
  }
  
  public void broadcastAuthentication(Identity identity) throws Exception {
    
    identities_.put(identity.getSessionId(), identity) ;
    // [PN] 18.06.07
    currentIdentity_.set(identity);
    
    listenerService_.broadcast("exo.service.authentication.login", this, identity) ;
  }
  
  
  public Identity getIdentityBySessionId(String sessionId) throws Exception {
    return  identities_.get(sessionId) ;
  }
  
  public Identity getCurrentIdentity() { 
    return currentIdentity_.get() ; 
  }
  
  public void setCurrentIdentity(Identity identity) { 
    currentIdentity_.set(identity) ; 
  }
  
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