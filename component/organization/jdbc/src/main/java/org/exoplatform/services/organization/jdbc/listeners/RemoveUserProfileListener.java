/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc.listeners;

import org.apache.commons.logging.Log;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.jdbc.UserDAOImpl;
/**
 * Created by The eXo Platform SAS
 * Author : Le Bien Thuy
 *          lebienthuy@gmail.com
 * Jun 28, 2007  
 */
public class RemoveUserProfileListener extends Listener<UserDAOImpl, User> {
  private OrganizationService service_ ;
  
  protected static Log log = ExoLogger.getLogger("organization:RemoveUserProfileListener");
  
  public RemoveUserProfileListener(OrganizationService service) {
    service_ = service ;
  }
  
  public void onEvent(Event<UserDAOImpl, User> event) throws Exception {
    log.info("Delete User Profile: " + event.getData().getUserName());
    service_.getUserProfileHandler().removeUserProfile(event.getData().getUserName(), true) ;
  }
}
