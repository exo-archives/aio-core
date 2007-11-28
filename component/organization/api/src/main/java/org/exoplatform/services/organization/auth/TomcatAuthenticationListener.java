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
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;
/**
 * Created by The eXo Platform SARL
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
