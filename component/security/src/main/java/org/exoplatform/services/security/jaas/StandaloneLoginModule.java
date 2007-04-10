/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.services.security.jaas;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.logging.Log;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.security.SecurityService;
import org.exoplatform.services.security.impl.CredentialsImpl;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 28 avr. 2004
 */
public class StandaloneLoginModule implements LoginModule {

  private static Log log_ = ExoLogger.getLogger("core.StandaloneLoginModule");

  private SecurityService securityService_;
  private boolean success_;
  private String username_;
  private Subject subject_;
  private CallbackHandler callbackHandler_;
  private Map sharedState_;
  
  public StandaloneLoginModule() {
    this.success_ = false;
    this.username_ = null;
  }
  
  public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
    this.subject_ = subject;
    this.callbackHandler_ = callbackHandler;
    this.sharedState_ = sharedState;
  }
  
  public boolean login() throws LoginException {    
    if (callbackHandler_ == null)  throw new LoginException("CallbackHandler null");
    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("Username: ");
    callbacks[1] = new PasswordCallback("Password: ", false);
    try {
      StandaloneContainer container = StandaloneContainer.getInstance();
      // prompt for username and password
      callbackHandler_.handle(callbacks);
      username_ = ((NameCallback) callbacks[0]).getName();
      String password = new String(((PasswordCallback) callbacks[1]).getPassword());
      //System.out.println("StandaloneLoginModule.login: user: " + username_);
      //System.out.println("StandaloneLoginModule.login: pwd: " + password);
      
      if("".equals(password)) {       
        password = null;
      } 
      securityService_ = 
        (SecurityService) container.getComponentInstanceOfType(SecurityService.class);
      //log_ = securityService_.getLog() ;
      
      if (username_ == null) {
        if(log_.isDebugEnabled())
          log_.debug("No user name entered");
        success_ = false;
        return false;
      }
      if (password == null) {
        if(log_.isDebugEnabled())
          log_.debug("No password entered");
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
        throw new LoginException("Authentication failed User: "+this.username_+" "+password);
      }
      subject_.getPrivateCredentials().add(password);
      subject_.getPublicCredentials().add(new CredentialsImpl(this.username_, password.toCharArray()));
      
      return true;
    } catch (Exception e) {
      log_.error("error while trying to login", e);
      throw new LoginException("Authentication failed");
    }
  }
  
  public boolean commit() throws LoginException {
//    S ystem.out.println("StandaloneLoginModule.commit");
    if (success_) {
      try {        
        securityService_.setUpAndCacheSubject(username_, subject_);
      } catch (Exception e) {
        throw new LoginException("error while filling subject with Principal in commit() of BasicLoginModule");
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