/**
 * Copyright (C) 2003-2008 eXo Platform SAS.
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
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.security.Authenticator;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.IdentityRegistry;

/**
 * This LoginModule should be used after customer LoginModule, which makes 
 * authentication. This one registers Identity for user in IdentityRegistry.
 * Required name of user MUST be passed to LM via sharedState (see method
 * {@link #initialize(Subject, CallbackHandler, Map, Map)}), with name 
 * javax.security.auth.login.name.
 * 
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class IdentitySetLoginModule implements LoginModule {

  protected Log log = ExoLogger.getLogger("core.IdentitySetLoginModule");

  private Map<String, ?> sharedState;

  /*
   * (non-Javadoc)
   * @see javax.security.auth.spi.LoginModule#abort()
   */
  public boolean abort() throws LoginException {
    if (log.isDebugEnabled()) {
      log.debug("abort of IdentitySetLoginModule");
    }

    return true;
  }

  /*
   * (non-Javadoc)
   * @see javax.security.auth.spi.LoginModule#commit()
   */
  public boolean commit() throws LoginException {
    if (log.isDebugEnabled()) {
      log.debug("in commit");
    }
    
    String userId = (String) sharedState.get("javax.security.auth.login.name");
    try {
      Authenticator authenticator = (Authenticator) getContainer()
          .getComponentInstanceOfType(Authenticator.class);

      if (authenticator == null)
        throw new LoginException("No Authenticator component found, check your configuration");

      IdentityRegistry identityRegistry = (IdentityRegistry) getContainer()
          .getComponentInstanceOfType(IdentityRegistry.class);

      if (identityRegistry.getIdentity(userId) == null) {
        Identity identity = authenticator.createIdentity(userId);
        identityRegistry.register(identity);
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw new LoginException(e.getMessage());
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject,
   *      javax.security.auth.callback.CallbackHandler, java.util.Map,
   *      java.util.Map)
   */
  public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map<String, ?> sharedState, Map<String, ?> options) {
    if (log.isDebugEnabled()) {
      log.debug("in initialize");
    }

    this.sharedState = sharedState;
  }

  /*
   * (non-Javadoc)
   * @see javax.security.auth.spi.LoginModule#login()
   */
  public boolean login() throws LoginException {
    if (log.isDebugEnabled()) {
      log.debug("in login");
    }

    return true;
  }

  /*
   * (non-Javadoc)
   * @see javax.security.auth.spi.LoginModule#logout()
   */
  public boolean logout() throws LoginException {
    if (log.isDebugEnabled()) {
      log.debug("in logout");
    }

    return true;
  }

  protected ExoContainer getContainer() throws Exception {
    // TODO set correct current container
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    if (container instanceof RootContainer) {
      container = RootContainer.getInstance().getPortalContainer("portal");
    }
    return container;
  }

}
