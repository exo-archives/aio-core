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
import org.exoplatform.services.security.ConversationRegistry;
import org.exoplatform.services.security.ConversationState;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady Azarenkov</a>
 * @version $Id: SimpleSessionFactoryInitializedFilter.java 7163 2006-07-19
 *          07:30:39Z peterit $
 */

public class SetCurrentIdentityFilter implements Filter {
  private static Log     log    = ExoLogger.getLogger("core.security.SetCurrentIdentityFilter");

  private ServletContext servletContext;

  public void init(FilterConfig config) throws ServletException {
    servletContext = config.getServletContext();
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpSession httpSession = httpRequest.getSession();

    //String contextName = servletContext.getServletContextName();
    String contextName = "portal";
    ExoContainer container = ExoContainerContext.getContainerByName(contextName);
    if (container == null) {
      log.error("Container not found for the servlet context "+ contextName);
      container = ExoContainerContext.getTopContainer();
    }

    ExoContainerContext.setCurrentContainer(container);
    
    ConversationRegistry conversationRegistry = (ConversationRegistry) container
    .getComponentInstanceOfType(ConversationRegistry.class);
    
    ConversationState state = null;
    String userId = httpRequest.getRemoteUser();
    
    if (userId != null) {
      
      ////
      state = conversationRegistry.getState(userId);
      if(state == null) 
        log.error("Can't find identity by userId "+ userId);

   // TODO ?
//      state = conversationRegistry.getState(httpSession.getId());
//      if(state == null) {
//        state = new ConversationState(conversationRegistry.getIdentity(userId));
//        
//        conversationRegistry.register(httpSession.getId(), state);
//      }
      

    }
    
    log.info("Requested "+httpRequest.getRequestURI()+" by "+userId);

    ConversationState.setCurrent(state);
    chain.doFilter(request, response);
    ConversationState.setCurrent(null);
    

    
  }

  public void destroy() {
  }
}
