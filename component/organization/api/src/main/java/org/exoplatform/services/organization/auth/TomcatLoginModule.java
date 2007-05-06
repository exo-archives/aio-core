/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.services.organization.auth;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.organization.OrganizationService;
/**
 * Created y the eXo platform team
 * User:  Tuan Nguyen
 * Date: May 6th, 2007
 */
public class TomcatLoginModule extends ExoJAASLoginModule {
  
  protected void populateRolePrincipals(OrganizationService service, String username, Subject subject) throws Exception {
    Set principals = subject.getPrincipals();
    Collection groups = groups = service.getGroupHandler().findGroupsOfUser(username);
    for (Iterator iter = groups.iterator(); iter.hasNext();) {
      org.exoplatform.services.organization.Group group = 
        (org.exoplatform.services.organization.Group) iter.next();
      String groupId = group.getId();
      String[] splittedGroupName = StringUtils.split(groupId, "/");
      principals.add(new RolePrincipal(splittedGroupName[0]));
    }
  }
}