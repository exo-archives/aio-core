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

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.component.ComponentRequestLifecycle;
/**
 * Created y the eXo platform team
 * User:  Tuan Nguyen
 * Date: May 6th, 2007
 */
public class ExoJAASLoginModule implements LoginModule {
  private Subject subject_;
  private CallbackHandler callbackHandler_;
  private Identity identity_ ;
  
  public ExoJAASLoginModule() { }
  
  final public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
    this.subject_ = subject;
    this.callbackHandler_ = callbackHandler;
  }
  
  final public boolean login() throws LoginException {
    System.out.println("In login of TomcatLoginModule") ;
    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("Username");
    callbacks[1] = new PasswordCallback("Password", false);    
    try {                 
      callbackHandler_.handle( callbacks);     
      String username = ((NameCallback) callbacks[0]).getName();      
      String password = new String(((PasswordCallback) callbacks[1]).getPassword());
      ((PasswordCallback) callbacks[1]).clearPassword();
      if (username == null ||  password == null) return false;
      identity_ = new Identity(username, username, password, subject_) ;
      return true ;
    } catch (Exception e) {
      throw new LoginException("Authentication failed");
    } 
  }
  
  final public boolean commit() throws LoginException {
    List<ComponentRequestLifecycle> components = null ;
    PortalContainer pcontainer = null;
    try {                 
      pcontainer = RootContainer.getInstance().getPortalContainer("portal");
      PortalContainer.setInstance(pcontainer) ;
      components = pcontainer.getComponentInstancesOfType(ComponentRequestLifecycle.class);
      for(ComponentRequestLifecycle component : components) { 
        component.startRequest(pcontainer) ;
      }      
      AuthenticationService authService =
        (AuthenticationService) pcontainer.getComponentInstanceOfType(AuthenticationService.class) ;
      if(authService.login(identity_)) {
        return true;
      } else {
        throw new LoginException("Authentication failed");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new LoginException("Authentication failed");
    } finally {
      if(components != null) {
        for(ComponentRequestLifecycle component : components) {
          component.endRequest(pcontainer) ;
        }
        PortalContainer.setInstance(null) ;
      }
    }
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