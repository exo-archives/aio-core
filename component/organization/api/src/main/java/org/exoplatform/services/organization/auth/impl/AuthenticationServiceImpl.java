/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.organization.auth.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.auth.AuthenticationService;
import org.exoplatform.services.organization.auth.Identity;
import org.exoplatform.services.organization.auth.PasswordEncrypter;

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
  private PasswordEncrypter encrypter ;
  
  public AuthenticationServiceImpl(ListenerService listenerService, OrganizationService orgService, PasswordEncrypter encrypter)  {
    log.info("Start AuthenticationService init ...................... ");
    listenerService_ = listenerService ;
    orgService_ =  orgService ;
    this.encrypter = encrypter;
    log.info("End AuthenticationService init ...................... ");
  }
  
  public AuthenticationServiceImpl(ListenerService listenerService, OrganizationService orgService)  {

    this(listenerService, orgService, null);
  }
  
  public boolean login(String userName, String password) throws Exception {
    String psw = password;
    if(this.encrypter != null)
      psw = new String(encrypter.encrypt(password.getBytes()));
    if(orgService_.getUserHandler().authenticate(userName, psw)) {
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