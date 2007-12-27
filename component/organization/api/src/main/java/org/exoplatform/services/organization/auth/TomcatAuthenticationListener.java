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

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;
/**
 * Created by The eXo Platform SAS
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * May 17, 2007  
 */
public class TomcatAuthenticationListener extends Listener<AuthenticationService, Identity> {

  private String userRoleParentGroup = null ;
  public TomcatAuthenticationListener(InitParams params) {
    if(params != null) {
      ValueParam param = params.getValueParam("user.role.parent.group");
      if(param!= null && param.getValue().length()>0) {
        userRoleParentGroup = param.getValue();
      } 
    }         
  }
  public void onEvent(Event<AuthenticationService, Identity> event)  throws Exception {
    Identity identity = event.getData() ;
    Subject subject = identity.getSubject() ;
    OrganizationService service = event.getSource().getOrganizationService() ;
    Set principals = subject.getPrincipals();
    Collection groups = groups = service.getGroupHandler().findGroupsOfUser(identity.getUsername());
    for (Iterator iter = groups.iterator(); iter.hasNext();) {
      Group group = (Group) iter.next();
      String groupId = group.getId();   
      String[] splittedGroupName = StringUtils.split(groupId, "/");
      if(userRoleParentGroup != null && splittedGroupName[0].equals(userRoleParentGroup) 
          && splittedGroupName.length>1) {
        principals.add(new RolePrincipal(splittedGroupName[splittedGroupName.length-1]));
      }else {
        principals.add(new RolePrincipal(splittedGroupName[0]));
      }
    }
  }
}
