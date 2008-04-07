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
package org.exoplatform.services.organization.auth.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.auth.AuthenticationService;
import org.exoplatform.services.organization.auth.Identity;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady Azarenkov</a>
 * @version $Id: SimpleSessionFactoryInitializedFilter.java 7163 2006-07-19
 *          07:30:39Z peterit $
 */

public class SetCurrentIdentityFilter implements Filter {

  private static Log     log    = ExoLogger.getLogger("auth.web.RestFilter");

  private ServletContext servletContext;

  private String         userId = null;

  public void init(FilterConfig config) throws ServletException {
    servletContext = config.getServletContext();
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpSession httpSession = httpRequest.getSession();

    String portalName = servletContext.getServletContextName();
    ExoContainer container = ExoContainerContext.getContainerByName(portalName);
    if (container == null)
      container = ExoContainerContext.getTopContainer();

    ExoContainerContext.setCurrentContainer(container);

    AuthenticationService authenticationService = (AuthenticationService) container
        .getComponentInstanceOfType(AuthenticationService.class);
    Identity identity = null;
    if (httpRequest.getRemoteUser() != null) {
      try {
        identity = authenticationService.getIdentityBySessionId(httpRequest.getRemoteUser());
      }catch (Exception e) {
        log.error("Can't find identity by sessionID ", e);
      }
    }
    authenticationService.setCurrentIdentity(identity);
    chain.doFilter(request, response);
    authenticationService.setCurrentIdentity(null) ;
  }

  public void destroy() {
  }
}
