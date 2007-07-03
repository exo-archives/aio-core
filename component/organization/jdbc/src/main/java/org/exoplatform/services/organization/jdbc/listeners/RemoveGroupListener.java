/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc.listeners;

import java.util.List;

import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.jdbc.GroupDAOImpl;
/**
 * Created by The eXo Platform SARL
 * Author : Le Bien Thuy
 *          lebienthuy@gmail.com
 * Jun 28, 2007  
 */
public class RemoveGroupListener extends Listener<GroupDAOImpl, Group> {
  private OrganizationService service_ ;
  
  public RemoveGroupListener(OrganizationService service) {
    service_ = service ;
  }
  
  @SuppressWarnings("unchecked")
  public void onEvent(Event<GroupDAOImpl, Group> event) throws Exception {
//    System.out.println("\n\nRemove all Child of Group: " + event.getData().getId() + "\n\n");
    GroupHandler membershipHanler = service_.getGroupHandler();
    List<Group> children = (List<Group>) membershipHanler.findGroups(event.getData());
    for(Group child: children) {
      membershipHanler.removeGroup(child, true);
    }
  }
}
