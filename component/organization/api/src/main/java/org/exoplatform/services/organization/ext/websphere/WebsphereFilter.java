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

import java.io.IOException;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.auth.AuthenticationService;
import org.exoplatform.services.organization.auth.Identity;

/**
 * Created by The eXo Platform SAS .
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey Zavizionov</a>
 * @version $Id: $
 *
 * Mar 4, 2008  
*/
public class WebsphereFilter implements Filter {

  private static Log log = ExoLogger.getLogger("org.exoplatform.frameworks.jcr.web.WebsphereFilter");
  private static String cookieName = "LtpaToken";
  private static String cookieName2 = "LtpaToken2";

  public void destroy() {
  }

  public void doFilter(ServletRequest request,
                       ServletResponse response,
                       FilterChain chain) throws IOException,
                                         ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    if (httpRequest.getQueryString() == null && httpRequest.getRequestURI() != null && httpRequest.getRequestURI().contains("/public")) {
      removeLtpaTokenCookie(httpRequest, httpResponse);
    }
    chain.doFilter(request, response);
  }

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  private void removeLtpaTokenCookie(HttpServletRequest req,
                                     HttpServletResponse res) {
    Cookie[] cooks = req.getCookies();
    if (cooks != null) {
      for (Cookie cook : cooks) {
//        System.out.println(">>> EXOMAN WebsphereFilter.removeLtpaTokenCookie() cook.getName() = " + cook.getName());
        if (cook != null && (cookieName.equalsIgnoreCase(cook.getName()) || cookieName2.equalsIgnoreCase(cook.getName()))) {
          cook.setMaxAge(0);
          cook.setPath("/");
          res.addCookie(cook);
//          System.out.println(">>> EXOMAN WebsphereFilter.removeLtpaTokenCookie() REMOVED LtpaToken = ");
        }
      }
    }
  }

  @Deprecated
  private void logout(HttpSession session) {
    try {
      String portalContainerName = session.getServletContext().getServletContextName();
      RootContainer rootContainer = RootContainer.getInstance();
      PortalContainer portalContainer = rootContainer.getPortalContainer(portalContainerName);
      PortalContainer.setInstance(portalContainer);
      AuthenticationService authenticationService = (AuthenticationService) portalContainer.getComponentInstanceOfType(AuthenticationService.class);
//      System.out.println(">>> EXOMAN WebsphereFilter.logout() authenticationService = " + authenticationService);
      Identity identity = authenticationService.getCurrentIdentity();
//      System.out.println(">>> EXOMAN WebsphereFilter.logout() identity = " + identity);
      String username = null;
      Subject subject = null;
      if (identity != null) {
        username = identity.getUsername();
//        System.out.println(">>> EXOMAN WebsphereFilter.logout() username = " + username);
        subject = identity.getSubject();
//        System.out.println(">>> EXOMAN WebsphereFilter.logout() subject = " + subject);
      }
      if (subject != null) {
        LoginContext lc = null;
//        System.out.println(">>> EXOMAN WebsphereFilter.logout() BEFORE CREATE LoginContext = ");
        lc = new LoginContext("exo-domain", subject);
//        System.out.println(">>> EXOMAN WebsphereFilter.logout() lc = " + lc);
//        System.out.println(">>> EXOMAN WebsphereFilter.logout() AFTER CREATE LoginContext = ");

//        System.out.println(">>> EXOMAN WebsphereFilter.logout() BEFORE LOGOUT = ");
        log.warn("LOGOUT with user '" + username + "'");
        lc.logout();
//        System.out.println(">>> EXOMAN WebsphereFilter.logout() AFTER LOGOUT = ");
      }
    } catch (Exception ex) {
      log.error("Error while logout a portal", ex);
      ex.printStackTrace();
    } finally {
      PortalContainer.setInstance(null);
    }
  }

}
