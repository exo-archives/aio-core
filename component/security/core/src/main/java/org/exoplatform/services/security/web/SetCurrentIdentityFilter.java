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
package org.exoplatform.services.security.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.security.ConversationRegistry;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.IdentityRegistry;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady
 *         Azarenkov</a>
 * @version $Id: SimpleSessionFactoryInitializedFilter.java 7163 2006-07-19
 *          07:30:39Z peterit $
 */
public class SetCurrentIdentityFilter implements Filter {

  /**
   * Logger.
   */
  private static Log log = ExoLogger.getLogger("core.security.SetCurrentIdentityFilter");

  /**
   * Portal Container name.
   */
  private String     portalContainerName;

  /**
   * {@inheritDoc}
   */
  public void init(FilterConfig config) throws ServletException {
    portalContainerName = config.getInitParameter("portalContainerName");
    if (portalContainerName == null)
      portalContainerName = config.getServletContext().getServletContextName();
  }

  /**
   * Set current {@link ConversationState}, if it is not registered yet then
   * create new one and register in {@link ConversationRegistry}. {@inheritDoc}
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                           ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    // String contextName = "portal";
    ExoContainer container = ExoContainerContext.getContainerByName(portalContainerName);
    if (container == null) {
      if (log.isDebugEnabled()) {
        log.debug("Container not found for the servlet context " + portalContainerName);
      }

      container = ExoContainerContext.getTopContainer();
    }

    try {
      ExoContainerContext.setCurrentContainer(container);
      ConversationState state = getCurrentState(container, httpRequest);
      // NOTE may be set as null
      ConversationState.setCurrent(state);
      chain.doFilter(request, response);
    } finally {
      try {
        ConversationState.setCurrent(null);
      } catch (Exception e) {
        log.warn("An error occured while cleaning the ThreadLocal", e);
      }
      try {
        ExoContainerContext.setCurrentContainer(null);
      } catch (Exception e) {
        log.warn("An error occured while cleaning the ThreadLocal", e);
      }
    }
  }

  /**
   * Gives the current state 
   */
  private ConversationState getCurrentState(ExoContainer container, HttpServletRequest httpRequest) {
    ConversationRegistry conversationRegistry = (ConversationRegistry) container.getComponentInstanceOfType(ConversationRegistry.class);

    IdentityRegistry identityRegistry = (IdentityRegistry) container.getComponentInstanceOfType(IdentityRegistry.class);

    String sessionId = null;
    ConversationState state = null;

    String userId = httpRequest.getRemoteUser();
    if (userId != null) {
      // only if user authenticated, otherwise there is no reason to do
      // anythings
      HttpSession httpSession = httpRequest.getSession();
      sessionId = httpSession.getId();
    }
    if (sessionId != null) {
      if (log.isDebugEnabled()) {
        log.debug("Looking for Conversation State " + sessionId);
      }

      state = conversationRegistry.getState(sessionId);

      if (state == null) {
        if (log.isDebugEnabled()) {
          log.debug("Conversation State not found, try create new one.");
        }

        Identity identity = identityRegistry.getIdentity(userId);
        if (identity != null) {
          state = new ConversationState(identity);
          // keep subject as attribute in ConversationState
          state.setAttribute(ConversationState.SUBJECT, identity.getSubject());
        } else
          log.error("Not found identity in IdentityRegistry for user " + userId
              + ", check Login Module.");

        if (state != null) {
          conversationRegistry.register(sessionId, state);
          if (log.isDebugEnabled()) {
            log.debug("Register Conversation state " + sessionId);
          }

        }
      }
    }
    return state;
  }

  /**
   * {@inheritDoc}
   */
  public void destroy() {
    // nothing to do.
  }

}
