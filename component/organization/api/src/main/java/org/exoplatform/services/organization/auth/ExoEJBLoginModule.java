/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.auth;

import java.io.IOException;
import java.security.acl.Group;
import java.util.Map;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.ejb.ExoEJBLogin;
import org.exoplatform.services.organization.ejb.ExoEJBLoginHome;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class ExoEJBLoginModule implements LoginModule {

  private Subject subject_;
  private Map<String, ?> sharedState_;
  private Map<String, ?> options_;
  private CallbackHandler callbackHandler_;
  private static final String AUTH_BEAN_NAME = "ExoEJBLogin"; 
  
  private static final Log LOGGER = ExoLogger.getLogger("JonasEJBLoginModule");

  public boolean abort() throws LoginException {
    return true;
  }

  public boolean commit() throws LoginException {
    return true;
  }

  public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map < String, ? > sharedState, Map < String, ? > options) {
    subject_ = subject;
    sharedState_ = sharedState;
    options_ = options;
    callbackHandler_ = callbackHandler;
  }

  public boolean login() throws LoginException {
    Callback[] callbacks = new Callback[2];
    String username = null;
    NameCallback nameCallback = new NameCallback("Username: ");
    callbacks[0] = nameCallback;
    char[] password = null;
    PasswordCallback passwordCallback = new PasswordCallback("Password: ", false);
    callbacks[1] = passwordCallback;

    try {
      callbackHandler_.handle(callbacks);
    } catch (IOException e) {
      LOGGER.error("IOException handling login: "
          + e.getMessage(), e);
      throw new LoginException(e.getMessage());
    } catch (UnsupportedCallbackException e) {
      LOGGER.error("UnsupportedCallbackException handling login: "
          + e.getMessage(), e);
      throw new LoginException(e.getMessage());
    }
    username = nameCallback.getName();
    password = passwordCallback.getPassword();
    try {
      InitialContext ctx = new InitialContext();
      Object obj = ctx.lookup(AUTH_BEAN_NAME);
      ExoEJBLoginHome authBeanHome =
        (ExoEJBLoginHome)PortableRemoteObject.narrow(obj, ExoEJBLoginHome.class);
      ExoEJBLogin authBean = authBeanHome.create();
      Subject subject = authBean.authenticate(username, password);
      subject_.getPrincipals().addAll(subject.getPrincipals());
      subject_.getPrivateCredentials().addAll(subject.getPrivateCredentials());
    } catch (Exception e) {
      LOGGER.error("Authentication for principal : '"
          + username + "' failed! : " + e.getMessage());
      throw new LoginException(e.getMessage());
    }
    return true;
  }

  public boolean logout() throws LoginException {
    return true;
  }

}
