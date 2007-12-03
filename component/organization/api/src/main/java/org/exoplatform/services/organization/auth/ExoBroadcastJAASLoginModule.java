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
package org.exoplatform.services.organization.auth;

import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created y the eXo platform team
 * User:  Tuan Nguyen
 * Date: May 6th, 2007
 * 
 * @version $Id$
 */
public class ExoBroadcastJAASLoginModule implements LoginModule {
  
  private Log      log = ExoLogger.getLogger("kernel.ExoBroadcastJAASLoginModule");
  
  private Subject subject_;
  private Map sharedState_;
  private PortalContainer cachedContainer_ = null;
  
  public ExoBroadcastJAASLoginModule() { }
  
  public ExoContainer getContainer() throws Exception {
    return RootContainer.getInstance().getPortalContainer("portal");	  
  }
  
  public void preProcessOperations() throws Exception {
    cachedContainer_ = PortalContainer.getInstance();
    PortalContainer container = cachedContainer_;
    if(container == null) {
      container = (PortalContainer) getContainer();
      PortalContainer.setInstance(container) ;
    }    
    List<ComponentRequestLifecycle> components = container.getComponentInstancesOfType(ComponentRequestLifecycle.class);
    for(ComponentRequestLifecycle component : components) { 
      component.startRequest(container) ;
    }        
  }

  public void postProcessOperations() throws Exception {
    PortalContainer container = (PortalContainer) getContainer();
    List<ComponentRequestLifecycle> components = container.getComponentInstancesOfType(ComponentRequestLifecycle.class);
    if(components != null) {
      for(ComponentRequestLifecycle component : components) {
        component.endRequest(container) ;
      }
    }
    // Previously, the Portal Container was set to null.
    // It is mandatory to restore a previous instance if existing.
    PortalContainer.setInstance(cachedContainer_) ;
  }  
  
  final public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
    this.subject_ = subject;
    this.sharedState_ = sharedState;
  }
  
  final public boolean login() throws LoginException {
    return true;
  }
  
  final public boolean commit() throws LoginException {
    try {
      try {
        String username = (String) sharedState_.get("javax.security.auth.login.name"); 	
        ExoContainer container = getContainer();
        preProcessOperations();
        AuthenticationService authService =
          (AuthenticationService) container.getComponentInstanceOfType(AuthenticationService.class) ;
        Identity identity = new Identity(username, username, subject_);
        authService.broadcastAuthentication(identity);
        return true;
      } finally {
        postProcessOperations();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new LoginException("Authentication failed. Exception " + e);
    }
  }
  
  final public boolean abort() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In abort of TomcatLoginModule") ;
    return true  ;
  }
  
  final public boolean logout() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In logout of TomcatLoginModule, It seems this method is never called in tomcat") ;
    return  true ;
  }


}