/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.services.organization.auth;

import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.services.organization.OrganizationService;
/**
 * Created y the eXo platform team
 * User:  Tuan Nguyen
 * Date: May 6th, 2007
 */
public class ExoJAASLoginModule implements LoginModule {
  private Subject subject_;
  private CallbackHandler callbackHandler_;
  
  public ExoJAASLoginModule() {
    System.out.println("In constructor of TomcatLoginModule : " + hashCode()) ;
  }
  
  final public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
    System.out.println("In initialize of TomcatLoginModule") ;
    info(subject) ;
    this.subject_ = subject;
    this.callbackHandler_ = callbackHandler;
  }
  
  final public boolean login() throws LoginException {
    System.out.println("In login of TomcatLoginModule") ;
    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("Username");
    callbacks[1] = new PasswordCallback("Password", false);    
    List<ComponentRequestLifecycle> components = null ;
    PortalContainer pcontainer = null;
    try {                 
      callbackHandler_.handle( callbacks);     
      String username = ((NameCallback) callbacks[0]).getName();      
      String password = new String(((PasswordCallback) callbacks[1]).getPassword());
      pcontainer = RootContainer.getInstance().getPortalContainer("portal");
      PortalContainer.setInstance(pcontainer) ;
      components = pcontainer.getComponentInstancesOfType(ComponentRequestLifecycle.class);
      for(ComponentRequestLifecycle component : components) { 
        component.startRequest(pcontainer) ;
      }
      
      if (username == null ||  password == null) return false;
      else {
        ((PasswordCallback) callbacks[1]).clearPassword();
        subject_.getPrivateCredentials().add(password);
        subject_.getPublicCredentials().add(new UserPrincipal(username));
        OrganizationService orgService =
          (OrganizationService) pcontainer.getComponentInstanceOfType(OrganizationService.class) ;
        boolean success =  orgService.getUserHandler().authenticate(username, password) ;
        if(success) populateRolePrincipals(orgService, username, subject_) ;
        return success ;
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
  
  final public boolean commit() throws LoginException {
    System.out.println("In commit of TomcatLoginModule") ;
    return true ;
  }
  
  final public boolean abort() throws LoginException {
    System.out.println("In abort of TomcatLoginModule") ;
    return true  ;
  }
  
  final public boolean logout() throws LoginException {
    System.out.println("In logout of TomcatLoginModule, It seems this method is never called in tomcat") ;
    return  true ;
  }
  
  private void info(Subject subject) {
    StringBuilder b = new StringBuilder() ;
    Iterator<Principal> i = subject.getPrincipals().iterator() ;
    b.append("Subject: ") ;
    b.append("\n  Principal: ") ;
    while(i.hasNext())  b.append(i.next().getName()).append(", ")  ;
    b.append("\n  Public Credential : ") ;
    Iterator ci = subject.getPublicCredentials().iterator() ;
    while(ci.hasNext())  b.append(i.next()).append(", ") ;
    b.append("\n  Private Credential : ") ;
    Iterator pi = subject.getPublicCredentials().iterator() ;
    while(pi.hasNext())  b.append(i.next()).append(", ") ;
    System.out.println(b) ;
  }
  
  protected void populateRolePrincipals(OrganizationService service, String username, Subject subject) throws Exception {
    Set principals = subject.getPrincipals();
    Collection groups = groups = service.getGroupHandler().findGroupsOfUser(username);
    for (Iterator iter = groups.iterator(); iter.hasNext();) {
      org.exoplatform.services.organization.Group group = 
        (org.exoplatform.services.organization.Group) iter.next();
      String groupId = group.getId();
      String[] splittedGroupName = StringUtils.split(groupId, "/");
      principals.add(new RolePrincipal(splittedGroupName[0]));
    }
  }
}