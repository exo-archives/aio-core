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
package org.exoplatform.services.security.jaas;

import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.security.SecurityService;
import org.exoplatform.services.security.impl.CredentialsImpl;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 28 avr. 2004
 */
public class BasicLoginModule implements LoginModule {
  
  private static final String DEFAULT_DOMAIN = "portal";

  private static Log log_ = ExoLogger.getLogger("core.BasicLoginModule");

  private SecurityService securityService_;
  private boolean success_;
  private String username_;
  private Subject subject_;
  private String portalDomain_ ;
  private CallbackHandler callbackHandler_;
  private Map sharedState_;
  private Map options_;
  
  public BasicLoginModule() {
    this.success_ = false;
    this.username_ = null;
  }
  
  public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
    this.subject_ = subject;
    this.callbackHandler_ = callbackHandler;
    this.sharedState_ = sharedState;
    this.options_ = options;
  }
  
  public boolean login() throws LoginException {    
    if (callbackHandler_ == null) {
      throw new LoginException("CallbackHandler null");
    }    
    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("Username: ");
    callbacks[1] = new PasswordCallback("Password: ", false);    
    List<ComponentRequestLifecycle> components = null ;
    PortalContainer savedContainer = null;
    PortalContainer authContainer = null;
    try {                 
      callbackHandler_.handle( callbacks);     
      username_ = ((NameCallback) callbacks[0]).getName();      
      String identifier = new String(((PasswordCallback) callbacks[1]).getPassword());     
      int split = identifier.lastIndexOf("@");
      String password;
      if(split == 0) {         // no password in password callback - login will be unsuccessful
        password = null;
        portalDomain_ = identifier.substring(split +1);
      } else if(split == -1) { // no Domain in password callback - check in specified options
        password = identifier;
        portalDomain_ = (String) options_.get("domain");
        if(portalDomain_ == null) {
          throw new LoginException("Cannot identify  the  portal container");
        }
      } else {
        password = identifier.substring(0, split);
        portalDomain_ = identifier.substring(split +1);
      }
      /*
       * Cache the current container. This is to handle among others the case
       * when a programmatic authentication is performed while the application
       * server is starting. In that case the current Portal Container is
       * already set and needs to be restored.
       */
      savedContainer = PortalContainer.getInstance();
      authContainer = RootContainer.getInstance().getPortalContainer(portalDomain_);
      PortalContainer.setInstance(authContainer) ;
      components = authContainer.getComponentInstancesOfType(ComponentRequestLifecycle.class);
      for(ComponentRequestLifecycle component : components) { 
        component.startRequest(authContainer) ;
      }
      securityService_ = 
        (SecurityService) authContainer.getComponentInstanceOfType(SecurityService.class);
      //log_ = securityService_.getLog() ;     
      
      if (username_ == null) {
        success_ = false;
        return false;
      }
      if (password == null) {
        success_ = false;
        return false;
      }
      // share username and password with other LoginModules
      sharedState_.put("javax.security.auth.login.name", username_);
      sharedState_.put("javax.security.auth.login.password", password);
      
      ((PasswordCallback) callbacks[1]).clearPassword();
      success_ = securityService_.authenticate(this.username_, password);
      if (!success_) {
        if(log_.isDebugEnabled())
          log_.debug("Authentication failed");
        throw new LoginException("Authentication failed. User: "+this.username_+" "+password);
      }
      subject_.getPrivateCredentials().add(password);
      subject_.getPublicCredentials().add(new CredentialsImpl(this.username_, password.toCharArray()));
      
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      if(log_.isDebugEnabled())
        log_.error("error while trying to login", e);
      throw new LoginException("Authentication failed");
    } finally {
      if(authContainer != null) {
        for(ComponentRequestLifecycle component : components) {
          component.endRequest(authContainer) ;
        }
        PortalContainer.setInstance(savedContainer) ;
      }
    }
  }
  
  public boolean commit() throws LoginException {
    if (success_) {
      List<ComponentRequestLifecycle> components = null ;
      PortalContainer savedContainer = null;
      PortalContainer authContainer = null;
      try {
        /*
         * Cache the current container. This is to handle among others the case
         * when a programmatic authentication is performed while the application
         * server is starting. In that case the current Portal Container is
         * already set and needs to be restored.
         */
        savedContainer = PortalContainer.getInstance();
        authContainer =  RootContainer.getInstance().getPortalContainer(portalDomain_);
        PortalContainer.setInstance(authContainer) ;
        components = authContainer.getComponentInstancesOfType(ComponentRequestLifecycle.class);
        for(ComponentRequestLifecycle component : components) component.startRequest(authContainer) ;
        securityService_.setUpAndCacheSubject(username_, subject_);
      } catch (Exception e) {
        throw new LoginException("error while filling subject with Principal in commit() of BasicLoginModule "+e);
      } finally {
        if(authContainer != null) {
          for(ComponentRequestLifecycle component : components) component.startRequest(authContainer) ;
          PortalContainer.setInstance(savedContainer) ;
        }
      }
    }
    return success_;
  }
  
  public boolean abort() throws LoginException {
    if(log_.isDebugEnabled())
      log_.debug("call abort()") ;
    clear();
    if(success_)
      return true;
    return false;
  }
  
  public boolean logout() throws LoginException {
    if(log_.isDebugEnabled())
      log_.debug("logout user: " + username_ ) ;
    securityService_.removeSubject(username_);
    clear();
    return true;
  }
  
  private void clear() {
    subject_.getPrincipals().clear();
    subject_.getPrivateCredentials().clear();
    subject_.getPublicCredentials().clear();
    username_ = null;
  }
}
