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

package org.exoplatform.services.security.web;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.security.ConversationRegistry;
import org.exoplatform.services.security.ConversationState;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class ConversationStateListener implements HttpSessionListener {

  /**
   * Logger.
   */
  protected Log log = ExoLogger.getLogger("core.security.ConversationStateListener");

  /**
   * {@inheritDoc}
   */
  public void sessionCreated(HttpSessionEvent event) {
    // nothing to do here
  }

  /**
   * Remove {@link ConversationState}. {@inheritDoc}
   */
  public void sessionDestroyed(HttpSessionEvent event) {
    String sesionId = event.getSession().getId();
    ConversationRegistry conversationRegistry =
      (ConversationRegistry) getContainer().getComponentInstanceOfType(ConversationRegistry.class);

    ConversationState conversationState = conversationRegistry.unregister(sesionId);

    if (conversationState != null)
      log.info("Remove conversation state " + sesionId);

  }

  /**
   * @return actual ExoContainer instance.
   */
  protected ExoContainer getContainer() {
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    if (container instanceof RootContainer) {
      container = RootContainer.getInstance().getPortalContainer("portal");
    }
    return container;
  }

}
