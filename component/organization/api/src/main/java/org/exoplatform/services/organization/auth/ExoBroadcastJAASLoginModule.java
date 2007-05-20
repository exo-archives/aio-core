/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.services.organization.auth;

import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
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
public class ExoBroadcastJAASLoginModule implements LoginModule {
  private Subject subject_;
  private Map sharedState_;
  
  public ExoBroadcastJAASLoginModule() { }
  
  final public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
    this.subject_ = subject;
    this.sharedState_ = sharedState;
  }
  
  final public boolean login() throws LoginException {
    return true;
  }
  
  final public boolean commit() throws LoginException {
    List<ComponentRequestLifecycle> components = null ;
    PortalContainer pcontainer = null;
    try {                 
      String username = (String) sharedState_.get("javax.security.auth.login.name"); 	
      
      //TODO use parameter for portal here!
      pcontainer = RootContainer.getInstance().getPortalContainer("portal");
      PortalContainer.setInstance(pcontainer) ;
      components = pcontainer.getComponentInstancesOfType(ComponentRequestLifecycle.class);
      for(ComponentRequestLifecycle component : components) { 
        component.startRequest(pcontainer) ;
      }      
      AuthenticationService authService =
        (AuthenticationService) pcontainer.getComponentInstanceOfType(AuthenticationService.class) ;
      Identity identity = new Identity(username, username, subject_);
      authService.broadcastAuthentication(identity);
      return true;
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