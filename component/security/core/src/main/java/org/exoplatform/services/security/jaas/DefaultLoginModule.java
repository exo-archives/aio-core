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
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.security.Authenticator;
import org.exoplatform.services.security.Credential;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.PasswordCredential;
import org.exoplatform.services.security.RolesExtractor;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.ConversationRegistry;
import org.exoplatform.services.security.UsernameCredential;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public class DefaultLoginModule implements LoginModule {

  protected Log             log              = ExoLogger.getLogger("core.DefaultLoginModule");

  protected Subject         subject_;
  private CallbackHandler   callbackHandler_;
  protected Identity        identity_;
  protected Map             sharedState_;


  public DefaultLoginModule() {
  }

  protected ExoContainer getContainer() throws Exception {
    return ExoContainerContext.getCurrentContainer(); //RootContainer.getInstance().getPortalContainer("portal");
  }

  public void initialize(Subject subject,
                               CallbackHandler callbackHandler,
                               Map sharedState,
                               Map options) {
    this.subject_ = subject;
    this.callbackHandler_ = callbackHandler;
    this.sharedState_ = sharedState;
  }
 
  public boolean login() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In login of DefaultLoginModule");

    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("Username");
    callbacks[1] = new PasswordCallback("Password", false);

    try {

      callbackHandler_.handle(callbacks);
      String username = ((NameCallback) callbacks[0]).getName();
      String password = new String(((PasswordCallback) callbacks[1]).getPassword());
      ((PasswordCallback) callbacks[1]).clearPassword();
      if (username == null || password == null)
        return false;
      sharedState_.put("javax.security.auth.login.name", username);
      subject_.getPrivateCredentials().add(password);
      subject_.getPublicCredentials().add(new UsernameCredential(username));
     
      Authenticator authenticator = (Authenticator) getContainer().getComponentInstanceOfType(Authenticator.class);
      RolesExtractor rolesExtractor = (RolesExtractor) getContainer().getComponentInstanceOfType(RolesExtractor.class);

      if (authenticator == null)
        throw new LoginException("No Authenticator component found, check your configuration");

      Credential[] credentials = new Credential[] { new UsernameCredential(username), new PasswordCredential(password) };
      
      identity_ = authenticator.authenticate(credentials);
      
      //identity_.setRolesExtractor(rolesExtractor);

      return true;

    } catch (final Throwable e) {
      e.printStackTrace();
      log.warn(e.getLocalizedMessage());
      throw new LoginException(e.getMessage());

    }
  }

  public boolean commit() throws LoginException {
    try {
      
      ConversationRegistry registry = (ConversationRegistry) getContainer().getComponentInstanceOfType(ConversationRegistry.class);
      registry.register(identity_.getUserId(), new ConversationState(identity_));

    } catch (final Throwable e) {
      log.warn(e.getLocalizedMessage());
      throw new LoginException(e.getMessage());
    }
    return true;
  }

  public boolean abort() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In abort of DefaultLoginModule");
    return true;
  }

  public boolean logout() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In logout of DefaultLoginModule, It seems this method is never called in tomcat");
    try {
      ConversationRegistry registry = (ConversationRegistry) getContainer().getComponentInstanceOfType(ConversationRegistry.class);
      registry.unregister(identity_.getUserId());

    } catch (final Throwable e) {
      log.warn(e.getLocalizedMessage());
      throw new LoginException(e.getMessage());

    }
    return true;
  }
}
