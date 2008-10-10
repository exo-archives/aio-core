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
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.security.Authenticator;
import org.exoplatform.services.security.Credential;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.IdentityRegistry;
import org.exoplatform.services.security.PasswordCredential;
import org.exoplatform.services.security.UsernameCredential;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public class DefaultLoginModule implements LoginModule {

  /**
   * Logger.
   */
  protected Log           log         = ExoLogger.getLogger("core.DefaultLoginModule");

  /**
   * @see {@link Subject} .
   */
  protected Subject       subject;

  /**
   * @see {@link CallbackHandler}
   */
  private CallbackHandler callbackHandler;

  /**
   * encapsulates user's principals such as name, groups, etc .
   */
  protected Identity      identity;

  /**
   * Shared state.
   */
  protected Map           sharedState;

  /**
   * Is allowed for one user login again if he already login. If must set in LM
   * options.
   */
  protected boolean       singleLogin = false;

  public DefaultLoginModule() {
  }

  /**
   * {@inheritDoc}
   */
  public void initialize(Subject subject,
                         CallbackHandler callbackHandler,
                         Map sharedState,
                         Map options) {
    this.subject = subject;
    this.callbackHandler = callbackHandler;
    this.sharedState = sharedState;

    String sl = (String) options.get("singleLogin");
    if (sl != null && (sl.equalsIgnoreCase("yes") || sl.equalsIgnoreCase("true"))) {
      this.singleLogin = true;
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean login() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In login of DefaultLoginModule.");

    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("Username");
    callbacks[1] = new PasswordCallback("Password", false);

    try {

      callbackHandler.handle(callbacks);
      String username = ((NameCallback) callbacks[0]).getName();
      String password = new String(((PasswordCallback) callbacks[1]).getPassword());
      ((PasswordCallback) callbacks[1]).clearPassword();
      if (username == null || password == null)
        return false;

      Authenticator authenticator = (Authenticator) getContainer().getComponentInstanceOfType(Authenticator.class);

      if (authenticator == null)
        throw new LoginException("No Authenticator component found, check your configuration");

      Credential[] credentials = new Credential[] { new UsernameCredential(username),
          new PasswordCredential(password) };

      String userId = authenticator.validateUser(credentials);
      identity = authenticator.createIdentity(userId);

      sharedState.put("javax.security.auth.login.name", userId);
      subject.getPrivateCredentials().add(password);
      subject.getPublicCredentials().add(new UsernameCredential(username));
      return true;

    } catch (final Throwable e) {
      log.error(e.getLocalizedMessage());
      throw new LoginException(e.getMessage());

    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean commit() throws LoginException {
    try {

      IdentityRegistry identityRegistry = (IdentityRegistry) getContainer().getComponentInstanceOfType(IdentityRegistry.class);

      if (singleLogin && identityRegistry.getIdentity(identity.getUserId()) != null)
        throw new LoginException("User " + identity.getUserId() + " already logined.");

      identity.setSubject(subject);
      identityRegistry.register(identity);

    } catch (final Throwable e) {
      log.error(e.getLocalizedMessage());
      throw new LoginException(e.getMessage());
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean abort() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In abort of DefaultLoginModule.");
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean logout() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In logout of DefaultLoginModule.");

    return true;
  }

  /**
   * @return actual ExoContainer instance.
   */
  protected ExoContainer getContainer() {
    // TODO set correct current container
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    if (container instanceof RootContainer) {
      container = RootContainer.getInstance().getPortalContainer("portal");
    }
    return container;
  }
}
