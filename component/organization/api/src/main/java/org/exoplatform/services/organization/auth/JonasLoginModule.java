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
public class JonasLoginModule extends ExoLoginJAASLoginModule {
  
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