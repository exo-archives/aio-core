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

import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.jaas.JAASGroup;
import org.exoplatform.services.security.jaas.RolePrincipal;
import org.exoplatform.services.security.jaas.UserPrincipal;

/**
 * Created y the eXo platform team
 * User:  Tuan Nguyen
 * Date: May 6th, 2007
 * 
 * @version $Id: ExoLoginJAASLoginModule.java 8478 2007-12-03 10:45:34Z rainf0x $
 */
public class WebsphereJAASLoginModule implements LoginModule {

  private Log             log              = ExoLogger.getLogger("core.ExoWebsphereJAASLoginModule");

  private Subject         subject_;
  private CallbackHandler callbackHandler_;
  private Identity        identity_;
  private Map             sharedState_;
  private PortalContainer cachedContainer_ = null;

  public WebsphereJAASLoginModule() {
  }

  public ExoContainer getContainer() throws Exception {                                                                                                 
    ExoContainer container = ExoContainerContext.getCurrentContainer();                                                                                  
    if (container instanceof RootContainer) {                                                                                                            
      container = RootContainer.getInstance().getPortalContainer("portal");                                                                              
    }                                                                                                                                                    
    return container;                                                                                                                                    
  }

  public void preProcessOperations() throws Exception {
    cachedContainer_ = PortalContainer.getInstance();
    PortalContainer container = cachedContainer_;
    if (container == null) {
      container = (PortalContainer) getContainer();
      PortalContainer.setInstance(container);
    }
    List<ComponentRequestLifecycle> components = container.getComponentInstancesOfType(ComponentRequestLifecycle.class);
    for (ComponentRequestLifecycle component : components) {
      component.startRequest(container);
    }
  }

  public void postProcessOperations() throws Exception {
    PortalContainer container = (PortalContainer) getContainer();
    List<ComponentRequestLifecycle> components = container.getComponentInstancesOfType(ComponentRequestLifecycle.class);
    if (components != null) {
      for (ComponentRequestLifecycle component : components) {
        component.endRequest(container);
      }
    }
    // Previously, the Portal Container was set to null.
    // It is mandatory to restore a previous instance if existing.
    PortalContainer.setInstance(cachedContainer_);
  }

  final public void initialize(Subject subject,
                               CallbackHandler callbackHandler,
                               Map sharedState,
                               Map options) {
    this.subject_ = subject;
    this.callbackHandler_ = callbackHandler;
    this.sharedState_ = sharedState;
  }

  final public boolean login() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In login of WebsphereJAASLoginModule");
    try {
      try {
        username_ = (String) sharedState_.get("javax.security.auth.login.name");
        ExoContainer container = getContainer();
        preProcessOperations();
        OrganizationService orgService = (OrganizationService) container.getComponentInstanceOfType(OrganizationService.class);
        onEvent(orgService);
        return true;
      } finally {
        postProcessOperations();
      }
    } catch (Exception e) {
      throw new LoginException("Authentication failed. Exception " + e);
    }
  }

  final public boolean commit() throws LoginException {
    return true;
  }

  final public boolean abort() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In abort of WebsphereJAASLoginModule");
    return true;
  }

  final public boolean logout() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In logout of WebsphereJAASLoginModule");
    getSubject().getPrincipals().remove(usernamePrincipal);
    return true;
  }

  protected Subject getSubject() {
    return subject_;
  }

  private String             username_;
  private Principal          usernamePrincipal;
  private String             userRoleParentGroup         = "platform";

  final public static String WSCREDENTIAL_PROPERTIES_KEY = "com.ibm.wsspi.security.cred.propertiesObject";
  final public static String WSCREDENTIAL_UNIQUEID       = "com.ibm.wsspi.security.cred.uniqueId";
  final public static String WSCREDENTIAL_SECURITYNAME   = "com.ibm.wsspi.security.cred.securityName";
  final public static String WSCREDENTIAL_GROUPS         = "com.ibm.wsspi.security.cred.groups";
  final public static String WSCREDENTIAL_CACHE_KEY      = "com.ibm.wsspi.security.cred.cacheKey";

  public void onEvent(OrganizationService service) throws Exception {
    usernamePrincipal = new UserPrincipal(username_);
    subject_.getPrincipals().add(usernamePrincipal);
    Collection groups = service.getGroupHandler().findGroupsOfUser(username_);
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
      roleGroupList.add(groupName);
    }
    websphereLogin(roleGroupList);
  }

  private void websphereLogin(ArrayList<String> roleGroupList) {
    Hashtable hashtable = new Hashtable();
    String uniqueid = username_;
    hashtable.put(WSCREDENTIAL_UNIQUEID, uniqueid);
    hashtable.put(WSCREDENTIAL_SECURITYNAME, username_);
    hashtable.put(WSCREDENTIAL_GROUPS, roleGroupList);
    hashtable.put(WSCREDENTIAL_CACHE_KEY, uniqueid + "WebsphereJAASLoginModule");
    //sharedState.put(WSCREDENTIAL_PROPERTIES_KEY, hashtable);
    subject_.getPublicCredentials().add(hashtable);
  }
}
