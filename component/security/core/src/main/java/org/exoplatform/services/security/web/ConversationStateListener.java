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

public class ConversationStateListener implements HttpSessionListener {

  private static final Log log = ExoLogger.getLogger("core.security.ConversationStateListener");
  
  /*
   * (non-Javadoc)
   * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
   */
  public void sessionCreated(HttpSessionEvent event) {
    // nothing to do here
  }

  /*
   * (non-Javadoc)
   * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
   */
  public void sessionDestroyed(HttpSessionEvent event) {
    String sesionId = event.getSession().getId();
    try {
      ConversationRegistry conversationRegistry = (ConversationRegistry) getContainer().getComponentInstanceOfType(
          ConversationRegistry.class);
      
      if (conversationRegistry.getState(sesionId) == null)
        return;
      
      ConversationState conversationState = conversationRegistry.unregister(sesionId);
      
      if (conversationState != null)
        log.info("Remove conversation state " + sesionId);
      else
        log.warn("Not found conversation state " + sesionId);
      
    } catch (Exception e) {
      log.error("Can't remove conversation state " + sesionId);
    }
  }

  protected ExoContainer getContainer() throws Exception {
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    if (container instanceof RootContainer) {
      container = RootContainer.getInstance().getPortalContainer("portal");
    }
    return container;
  }

}
