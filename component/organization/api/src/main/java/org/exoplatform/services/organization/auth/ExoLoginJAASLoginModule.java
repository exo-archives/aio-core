/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.services.organization.auth;

import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.component.ComponentRequestLifecycle;
/**
 * Created y the eXo platform team
 * User:  Tuan Nguyen
 * Date: May 6th, 2007
 */
public class ExoLoginJAASLoginModule implements LoginModule {
  private Subject subject_;
  private CallbackHandler callbackHandler_;
  private Identity identity_ ;
  private Map sharedState_;
  
  public ExoLoginJAASLoginModule() { }
  
  
  public ExoContainer getContainer() throws Exception {
    return RootContainer.getInstance().getPortalContainer("portal");      
  }
  
  public void preProcessOperations() throws Exception {
    PortalContainer container = (PortalContainer) getContainer();
    PortalContainer.setInstance(container) ;
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
      PortalContainer.setInstance(null) ;
    }    
  }    
  
  final public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
    this.subject_ = subject;
    this.callbackHandler_ = callbackHandler;
    this.sharedState_ = sharedState;
  }
  
  final public boolean login() throws LoginException {
    System.out.println("In login of TomcatLoginModule") ;
    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("Username");
    callbacks[1] = new PasswordCallback("Password", false);    
    ExoContainer container = null;
    try { 
      try {
        callbackHandler_.handle( callbacks);     
        String username = ((NameCallback) callbacks[0]).getName();      
        String password = new String(((PasswordCallback) callbacks[1]).getPassword());
        ((PasswordCallback) callbacks[1]).clearPassword();
        if (username == null ||  password == null) return false;
        sharedState_.put("javax.security.auth.login.name", username);
        
        container = getContainer();
        preProcessOperations();    
        AuthenticationService authService =
            (AuthenticationService) container.getComponentInstanceOfType(AuthenticationService.class) ;      
      
        if(authService.login(username, password)) {
            return true;
          } else {
            throw new LoginException("Authentication failed");
          }
      } finally {
        postProcessOperations();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new LoginException("Authentication failed. Exception " + e);
    }
  }
  
  final public boolean commit() throws LoginException {
    return true;
  }
  
  final public boolean abort() throws LoginException {
    System.out.println("In abort of TomcatLoginModule") ;
    return true  ;
  }
  
  final public boolean logout() throws LoginException {
    System.out.println("In logout of TomcatLoginModule, It seems this method is never called in tomcat") ;
    return  true ;
  }
}