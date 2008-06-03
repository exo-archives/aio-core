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

import javax.security.auth.Subject;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.jaas.JAASGroup;
import org.exoplatform.services.security.jaas.RolePrincipal;
import org.exoplatform.services.security.jaas.UserPrincipal;
/**
 * Created by The eXo Platform SAS
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * May 17, 2007  
 */
public class JonasAuthenticationListener extends Listener<AuthenticationService, Identity> {

  private String userRoleParentGroup = null ;

  public JonasAuthenticationListener(InitParams params) {
    if(params != null) {
      ValueParam param = params.getValueParam("user.role.parent.group");
      if(param!= null && param.getValue().length()>0) {
        userRoleParentGroup = param.getValue();
      } 
    }         
  }

  public void onEvent(Event<AuthenticationService, Identity> event)  throws Exception {
    OrganizationService service = event.getSource().getOrganizationService() ;
    Identity identity = event.getData() ;
    Subject subject = identity.getSubject() ;
    String username = identity.getUserId() ;
    subject.getPrincipals().add(new UserPrincipal(username));
    Collection groups = service.getGroupHandler().findGroupsOfUser(username);
    Group roleGroup = new JAASGroup(JAASGroup.ROLES);
    for (Iterator iter = groups.iterator(); iter.hasNext();) {
      org.exoplatform.services.organization.Group group = 
        (org.exoplatform.services.organization.Group) iter.next();
      String groupId = group.getId();
      String[] splittedGroupName = StringUtils.split(groupId, "/");
      if(userRoleParentGroup != null &&splittedGroupName[0].equals(userRoleParentGroup) &&
          splittedGroupName.length>1) {
        roleGroup.addMember(new RolePrincipal(splittedGroupName[splittedGroupName.length-1]));
      }else {
        roleGroup.addMember(new RolePrincipal(splittedGroupName[0])); 
      }     
    }
    subject.getPrincipals().add(roleGroup);
  }
}
