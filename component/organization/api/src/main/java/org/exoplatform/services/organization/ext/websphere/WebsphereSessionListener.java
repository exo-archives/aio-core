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
package org.exoplatform.services.organization.ext.websphere;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.auth.AuthenticationService;
import org.exoplatform.services.security.Identity;

/**
 * Created by The eXo Platform SAS .
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey Zavizionov</a>
 * @version $Id: $
 *
 * Mar 11, 2008  
*/
public class WebsphereSessionListener implements HttpSessionListener {

  private static Log log = ExoLogger.getLogger("core.WebsphereSessionListener");

  public void sessionCreated(HttpSessionEvent event) {
  }

  public void sessionDestroyed(HttpSessionEvent event) {
    try {
      HttpSession session = event.getSession();
      logout(session);
    } catch (Exception ex) {
      log.error("Error while logout a portal", ex);
      ex.printStackTrace();
    } finally {
      PortalContainer.setInstance(null);
    }
  }

  private void logout(HttpSession session) {
    try {
      String portalContainerName = session.getServletContext().getServletContextName();

      RootContainer rootContainer = RootContainer.getInstance();
      PortalContainer portalContainer = rootContainer.getPortalContainer(portalContainerName);
      PortalContainer.setInstance(portalContainer);

      AuthenticationService authenticationService = (AuthenticationService) portalContainer.getComponentInstanceOfType(AuthenticationService.class);
      Identity identity = authenticationService.getCurrentIdentity();
      String username = null;
      Subject subject = null;
      if (identity != null) {
        username = identity.getUserId();
        subject = identity.getSubject();
      }

      if (subject != null) {
        LoginContext lc = null;
        lc = new LoginContext("system.WEB_INBOUND", subject);
        log.warn("LOGOUT with user '" + username + "'");
        lc.logout();
      }

    } catch (Exception ex) {
      log.error("Error while logout a portal", ex);
      ex.printStackTrace();
    } finally {
      PortalContainer.setInstance(null);
    }
  }

}
