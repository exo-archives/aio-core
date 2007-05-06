/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.services.organization.auth;

import java.security.acl.Group;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.organization.OrganizationService;

/**
 * Created by the eXo platform team
 * User: Brice Revenant
 * Date: May 6th, 2007
 */
public class JonasLoginModule extends ExoJAASLoginModule {
  
  protected void populateRolePrincipals(OrganizationService service, String username, Subject subject) throws Exception {
    subject.getPrincipals().add(new UserPrincipal(username));
    Collection groups = service.getGroupHandler().findGroupsOfUser(username);
    Group roleGroup = new JAASGroup(JAASGroup.ROLES);
    for (Iterator iter = groups.iterator(); iter.hasNext();) {
      org.exoplatform.services.organization.Group group = 
        (org.exoplatform.services.organization.Group) iter.next();
      String groupId = group.getId();
      String[] splittedGroupName = StringUtils.split(groupId, "/");
      roleGroup.addMember(new RolePrincipal(splittedGroupName[0]));
    }
    subject.getPrincipals().add(roleGroup);
  }
}