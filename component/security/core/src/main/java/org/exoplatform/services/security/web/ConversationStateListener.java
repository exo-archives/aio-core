package org.exoplatform.services.security.web;

import javax.security.auth.login.LoginContext;
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

  /**
   * Logger.
   */
  private static final Log LOG = ExoLogger.getLogger("core.security.ConversationStateListener");
  
  /**
   * {@inheritDoc} 
   */
  public void sessionCreated(HttpSessionEvent event) {
    // nothing to do here
  }

  /**
   * Remove {@link ConversationState}.
   * {@inheritDoc} 
   */
  public void sessionDestroyed(HttpSessionEvent event) {
    String sesionId = event.getSession().getId();
    try {
      ConversationRegistry conversationRegistry = (ConversationRegistry) getContainer().getComponentInstanceOfType(
          ConversationRegistry.class);
      
      ConversationState conversationState = conversationRegistry.unregister(sesionId);
      
      if (conversationState != null) {
        LOG.info("Remove conversation state " + sesionId);
        LoginContext ctx = new LoginContext("exo-domain",
            (javax.security.auth.Subject) conversationState.getAttribute(ConversationState.SUBJECT));
        ctx.logout();
      }
      
    } catch (Exception e) {
      LOG.error("Can't remove conversation state " + sesionId);
    }
  }

  /**
   * @return actual ExoContainer instance.
   */
  protected ExoContainer getContainer() throws Exception {
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    if (container instanceof RootContainer) {
      container = RootContainer.getInstance().getPortalContainer("portal");
    }
    return container;
  }

}
