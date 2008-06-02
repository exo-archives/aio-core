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
package org.exoplatform.services.organization.ext.websphere;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.security.auth.Subject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.auth.AuthenticationService;
import org.exoplatform.services.organization.auth.Identity;
import org.exoplatform.services.organization.auth.JAASGroup;
import org.exoplatform.services.organization.auth.RolePrincipal;
import org.exoplatform.services.organization.auth.UserPrincipal;

/**
 * Created by The eXo Platform SAS .
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey Zavizionov</a>
 * @version $Id: $
 *
 * Mar 20, 2008  
*/
public class WebsphereAuthenticationListener extends Listener<AuthenticationService, Identity> {

  private static Log log                 = ExoLogger.getLogger("core.WebsphereAuthenticationListener");

  private String     userRoleParentGroup = null;

  private Subject    subject;

  private String     username;

  public WebsphereAuthenticationListener(InitParams params) {
    if (log.isDebugEnabled()) log.debug("WebsphereAuthenticationListener.WebsphereAuthenticationListaner() 1 = " + 1);
    if (params != null) {
      ValueParam param = params.getValueParam("user.role.parent.group");
      if (param != null && param.getValue().length() > 0) {
        userRoleParentGroup = param.getValue();
      }
    }
  }

  public void onEvent(Event<AuthenticationService, Identity> event) throws Exception {
    if (log.isDebugEnabled()) log.debug("WebsphereAuthenticationListener.onEvent() 1 = " + 1);
    OrganizationService service = event.getSource().getOrganizationService();
    Identity identity = event.getData();
    subject = identity.getSubject();
    username = identity.getUsername();
    subject.getPrincipals().add(new UserPrincipal(username));
    Collection groups = service.getGroupHandler().findGroupsOfUser(username);
    Group roleGroup = new JAASGroup(JAASGroup.ROLES);
    ArrayList<String> roleGroupList = new ArrayList<String>();
    for (Iterator iter = groups.iterator(); iter.hasNext();) {
      org.exoplatform.services.organization.Group group = (org.exoplatform.services.organization.Group) iter.next();
      String groupId = group.getId();
      String groupName = null;
      String[] splittedGroupName = StringUtils.split(groupId, "/");
      if (userRoleParentGroup != null && splittedGroupName[0].equals(userRoleParentGroup) && splittedGroupName.length > 1) {
        groupName = splittedGroupName[splittedGroupName.length - 1];
      } else {
        groupName = splittedGroupName[0];
      }
      roleGroup.addMember(new RolePrincipal(groupName));
      roleGroupList.add(groupName);
    }
    subject.getPrincipals().add(roleGroup);
  }

}
