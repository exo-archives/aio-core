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

package org.exoplatform.services.security.impl;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.security.SecurityService;
import org.exoplatform.services.security.SubjectEventListener;
import org.exoplatform.services.security.jaas.JAASGroup;
import org.exoplatform.services.security.sso.SSOAuthenticationConfig;
import org.exoplatform.services.security.sso.impl.BaseSSOAuthentication;

/**
 * Created y the eXo platform team User: Benjamin Mestrallet Date: 28 avr. 2004
 */
public class SecurityServiceImpl implements SecurityService {

  protected static Log log_ = ExoLogger.getLogger("core.SecurityServiceImpl");

  private Map subjects;
  private OrganizationService orgService_;
  private String authentication_;
  private BaseSSOAuthentication SSOAuthentication_;
  protected static ThreadLocal <String> currentUserHolder = new ThreadLocal <String>();

  public SecurityServiceImpl(
      OrganizationService organizationService, InitParams params) {
    orgService_ = organizationService;
    subjects = new HashMap();
    ValueParam param = params.getValueParam("security.authentication");
    authentication_ = null;
    SSOAuthentication_ = null;

    if (param != null)
      authentication_ = param.getValue();
    if (authentication_ == null || authentication_.equals(""))
      authentication_ = SecurityService.STANDALONE_AUTHENTICATION;

  }

  public boolean authenticate(String login, String password) throws Exception {   
    if (password == null || "".equals(password)) {
      log_.debug("password must not be null or empty");
      throw new Exception("password must not be null or empty");
    }
    boolean result = orgService_.getUserHandler().authenticate(login, password);   
    return result;
  }

  public void setUpAndCacheSubject(String userName, Subject value) throws Exception {
    Set principals = value.getPrincipals();
    principals.add(new UserPrincipalImpl(userName));
    Collection groups = null;
    try {
      groups = orgService_.getGroupHandler().findGroupsOfUser(userName);
    } catch (Exception e) {
      throw new Exception(e);
    }
    Group roleGroup = new JAASGroup(JAASGroup.ROLES);
    for (Iterator iter = groups.iterator(); iter.hasNext();) {
      org.exoplatform.services.organization.Group group = 
        (org.exoplatform.services.organization.Group) iter.next();
      String groupId = group.getId();
      String[] splittedGroupName = StringUtils.split(groupId, "/");
      roleGroup.addMember(new RolePrincipalImpl(splittedGroupName[0]));
    }
    value.getPrincipals().add(roleGroup);
    subjects.put(userName, value);

    currentUserHolder.set(userName);
  }

  //Use this for  tomcat 5.5.x
  public void setUpAndCacheSubjectTomcat55(String userName, Subject value) throws Exception {
    Set principals = value.getPrincipals();
    principals.add(new UserPrincipalImpl(userName));
    Collection groups = null;
    try {
      groups = orgService_.getGroupHandler().findGroupsOfUser(userName);
    } catch (Exception e) {
      throw new Exception(e);
    }
    for (Iterator iter = groups.iterator(); iter.hasNext();) {
      org.exoplatform.services.organization.Group group = 
        (org.exoplatform.services.organization.Group) iter.next();
      String groupId = group.getId();
      String[] splittedGroupName = StringUtils.split(groupId, "/");
      value.getPrincipals().add(new RolePrincipalImpl(splittedGroupName[0]));
    }
    //value.getPrincipals().add(roleGroup);
    subjects.put(userName, value);
  }

  public boolean isUserInRole(String userName, String role) {
    Subject subject = (Subject) subjects.get(userName);
    if (subject == null) return false;
    Set roleGroups = subject.getPrincipals(Group.class);
    for (Iterator iter = roleGroups.iterator(); iter.hasNext();) {
      Group roleGroup = (Group) iter.next();
      Enumeration e = roleGroup.members();
      while (e.hasMoreElements()) {
        Principal rolePrincipal = (Principal) e.nextElement();
        if (rolePrincipal.getName().equals(role))
          return true;
      }
    }
    return false;
  }

  public boolean hasMembershipInGroup(String userId, String membershipName,
      String groupName) {
    
    try {
      if ("*".equals(membershipName)) {
        // Determine if there exists at least one membership
        return
          ! orgService_.getMembershipHandler().findMembershipsByUserAndGroup(
            userId,
            groupName).isEmpty();
      } else {
        // Determine if there exists the membership of specified type
        return 
          orgService_.getMembershipHandler().findMembershipByUserGroupAndType(
            userId,
            groupName,
            membershipName) != null;
      }
    }
    catch(Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean hasMembershipInGroup(String user, String roleExpression) {
    if("*".equals(roleExpression))
      return true;
    String membershipName = roleExpression.substring(0, roleExpression
        .indexOf(":"));
    String groupName = roleExpression
    .substring(roleExpression.indexOf(":") + 1);
    return hasMembershipInGroup(user, membershipName, groupName);
  }

  public Subject getSubject(String userName) {
    log_.debug("get subject for user " + userName);
    return (Subject) subjects.get(userName);
  }

  public void removeSubject(String userName) {
    log_.debug("remove subject for user " + userName);
    subjects.remove(userName);
  }


  /* (non-Javadoc)
   * @see org.exoplatform.services.security.SecurityService#getCurrentSubject()
   */
  public Subject getCurrentSubject() {
    String userName = currentUserHolder.get();
    if(userName == null)
      return null;
    else
      return (Subject)subjects.get(userName);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.security.SecurityService#setCurrentUser(java.lang.String)
   */
  public void setCurrentUser(String userName) {
    this.currentUserHolder.set(userName);
  }

  public void addSubjectEventListener(SubjectEventListener subjectEventListener) {
    // To change body of implemented methods use File | Settings | File
    // Templates.
  }

  public String getSSOAuthentication() {
    if (SSOAuthentication_ != null) 
      return SSOAuthentication_.getSSOAuthenticationConfig().getAuthenticationName() ;
    else return null ;
  }

  public SSOAuthenticationConfig getSSOAuthenticationConfig() {
    if (SSOAuthentication_ != null) 
      return SSOAuthentication_.getSSOAuthenticationConfig() ;
    else return null ;
  }

  public boolean isSSOAuthentication() {
    return SecurityService.SSO_AUTHENTICATION.equals(authentication_);
  }

  public boolean isStandaloneAuthentication() {
    return SecurityService.STANDALONE_AUTHENTICATION.equals(authentication_);
  }

  public String getProxyTicket(String userName, String urlOfTargetService) throws Exception {
    if (!this.isSSOAuthentication())
      throw new Exception("Portal is configured for standalone authentication. "
          + "No proxy authentication feature available !") ;

    if (SSOAuthentication_ == null)
      throw new Exception("No SSO authentication configured !") ;

    Iterator iter = this.getSubject(userName).getPrivateCredentials().iterator();
    if (!iter.hasNext()) return null;

    return SSOAuthentication_.getProxyTicket((String) iter.next(), urlOfTargetService) ;
  }

  public void setSSOAuthenticationPlugin(ComponentPlugin plugin) {
    SSOAuthentication_ = (BaseSSOAuthentication) plugin ;
  }

  public Log getLog() {
    return log_;
  }

  /**
   * Intended to be used by subclasses that implement different policies
   * on setting up and caching the subject.
   * 
   * @return the list of subjects for the specific user
   */
  public Map getSubjects() {
    return subjects;
  }

  /**
   * Intended to be used by subclasses that implement different policies
   * on setting up and caching the subject.
   * 
   * @return the OrganizationService to be used when processing the user
   */
  public OrganizationService getOrgService() {
    return orgService_;
  }
}
