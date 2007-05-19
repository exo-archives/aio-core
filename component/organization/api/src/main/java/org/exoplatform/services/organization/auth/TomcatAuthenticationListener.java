/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.auth;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.organization.OrganizationService;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * May 17, 2007  
 */
public class TomcatAuthenticationListener extends Listener<AuthenticationService, Identity> {
  public void onEvent(Event<AuthenticationService, Identity> event)  throws Exception {
    Identity identity = event.getData() ;
    Subject subject = identity.getSubject() ;
    OrganizationService service = event.getSource().getOrganizationService() ;
    Set principals = subject.getPrincipals();
    Collection groups = groups = service.getGroupHandler().findGroupsOfUser(identity.getUsername());
    for (Iterator iter = groups.iterator(); iter.hasNext();) {
      org.exoplatform.services.organization.Group group = 
        (org.exoplatform.services.organization.Group) iter.next();
      String groupId = group.getId();
      String[] splittedGroupName = StringUtils.split(groupId, "/");
      principals.add(new RolePrincipal(splittedGroupName[0]));
    }
  }
}
