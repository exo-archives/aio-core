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
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
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
public class ExoLoginJAASLoginModule implements LoginModule {

  private Log             log              = ExoLogger.getLogger("core.ExoLoginJAASLoginModule");

  private Subject         subject_;
  private CallbackHandler callbackHandler_;
  private Identity        identity_;
  private Map             sharedState_;
  private PortalContainer cachedContainer_ = null;

  public ExoLoginJAASLoginModule() {
  }

  public ExoContainer getContainer() throws Exception {                                                                                                 
    ExoContainer container = ExoContainerContext.getCurrentContainer();                                                                                  
    if (container instanceof RootContainer) {                                                                                                            
      container = RootContainer.getInstance().getPortalContainer("portal");                                                                              
    }                                                                                                                                                    
    return container;                                                                                                                                    
  }

  public void preProcessOperations() throws Exception {
    cachedContainer_ = PortalContainer.getInstance();
    PortalContainer container = cachedContainer_;
    if (container == null) {
      container = (PortalContainer) getContainer();
      PortalContainer.setInstance(container);
    }
    List<ComponentRequestLifecycle> components = container.getComponentInstancesOfType(ComponentRequestLifecycle.class);
    for (ComponentRequestLifecycle component : components) {
      component.startRequest(container);
    }
  }

  public void postProcessOperations() throws Exception {
    PortalContainer container = (PortalContainer) getContainer();
    List<ComponentRequestLifecycle> components = container.getComponentInstancesOfType(ComponentRequestLifecycle.class);
    if (components != null) {
      for (ComponentRequestLifecycle component : components) {
        component.endRequest(container);
      }
    }
    // Previously, the Portal Container was set to null.
    // It is mandatory to restore a previous instance if existing.
    PortalContainer.setInstance(cachedContainer_);
  }

  final public void initialize(Subject subject,
                               CallbackHandler callbackHandler,
                               Map sharedState,
                               Map options) {
    this.subject_ = subject;
    this.callbackHandler_ = callbackHandler;
    this.sharedState_ = sharedState;
  }

  final public boolean login() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In login of ExoLoginJAASLoginModule");

    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("Username");
    callbacks[1] = new PasswordCallback("Password", false);
    ExoContainer container = null;
    try {
      try {
        callbackHandler_.handle(callbacks);
        String username = ((NameCallback) callbacks[0]).getName();
        String password = new String(((PasswordCallback) callbacks[1]).getPassword());
        ((PasswordCallback) callbacks[1]).clearPassword();
        if (username == null || password == null)
          return false;
        sharedState_.put("javax.security.auth.login.name", username);
        subject_.getPrivateCredentials().add(password);
        container = getContainer();
        preProcessOperations();
        AuthenticationService authService = (AuthenticationService) container.getComponentInstanceOfType(AuthenticationService.class);

        if (authService.login(username, password)) {
          return true;
        } else {
          throw new LoginException("Authentication failed");
        }
      } finally {
        postProcessOperations();
      }
    } catch (final Throwable e) {
      throw new LoginException(e.toString()) {

        private static final long serialVersionUID = 5843336633846631565L; // autogen by eclipse

        @Override
        public Throwable getCause() {
          return e;
        }
      };
    }
  }

  final public boolean commit() throws LoginException {
    return true;
  }

  final public boolean abort() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In abort of TomcatLoginModule");
    return true;
  }

  final public boolean logout() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In logout of TomcatLoginModule, It seems this method is never called in tomcat");
    return true;
  }
}
