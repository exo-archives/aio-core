/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
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
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * May 17, 2007  
 */
public class JbossAuthenticationListener extends Listener<AuthenticationService, Identity> {

  private String userRoleParentGroup = null ;

  public JbossAuthenticationListener(InitParams params) {
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
    String username = identity.getUsername() ;
    subject.getPrincipals().add(new UserPrincipal(username));
    Collection groups = service.getGroupHandler().findGroupsOfUser(username);
    Group roleGroup = new JAASGroup(JAASGroup.ROLES);
    for (Iterator iter = groups.iterator(); iter.hasNext();) {
      org.exoplatform.services.organization.Group group = 
        (org.exoplatform.services.organization.Group) iter.next();
      String groupId = group.getId();
      String[] splittedGroupName = StringUtils.split(groupId, "/");     
      if(userRoleParentGroup!=null && splittedGroupName[0].equals(userRoleParentGroup) 
          && splittedGroupName.length>1) {
        roleGroup.addMember(new RolePrincipal(splittedGroupName[splittedGroupName.length-1]));
      }else {
        roleGroup.addMember(new RolePrincipal(splittedGroupName[0])); 
      }     
    }
    subject.getPrincipals().add(roleGroup);
  }
}