/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SAS         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.ejb;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.auth.JAASGroup;
import org.exoplatform.services.organization.auth.RolePrincipal;
import org.exoplatform.services.organization.auth.UserPrincipal;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.organization.OrganizationService;

/**
 * Bean for authentication.
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class ExoEJBLoginBean implements SessionBean {

  static final long serialVersionUID = 234765347623L;

  private ExoContainer container;
  private OrganizationService orgService;
  private SessionContext context;

  /**
   * @param user - user name.
   * @param pass - user password.
   * @return subject for authenticated user.
   */
  public Subject authenticate(String user, char[] pass) {
    try {
      container = ExoContainerContext.getContainerByName("portal");
      orgService = (OrganizationService) container
          .getComponentInstanceOfType(OrganizationService.class);
      if (orgService.getUserHandler().authenticate(user, new String(pass))) {
        Collection<Group> groups = orgService.getGroupHandler()
            .findGroupsOfUser(user);
        JAASGroup jaasGroup = new JAASGroup("Roles");
        for (Group g : groups) {
          jaasGroup.addMember(new RolePrincipal(StringUtils.split(g.getId(), "/")[0]));
        }
        Subject subj = new Subject();
        subj.getPrincipals().add(new UserPrincipal(user));
        subj.getPrivateCredentials().add(new String(pass));
        subj.getPrincipals().add(jaasGroup);
        return subj;
      }
      throw new LoginException("Can't login with principal : '" + user + "'");

    } catch (Exception e) {
      throw new EJBException("Exception in Remote Bean: " + e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.ejb.SessionBean#ejbPassivate()
   */
  public void ejbPassivate() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.ejb.SessionBean#ejbActivate()
   */
  public void ejbActivate() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.ejb.SessionBean#ejbRemove()
   */
  public void ejbRemove() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
   */
  public void setSessionContext(SessionContext contx) {
    context = contx;
  }

  public void ejbCreate() {
  }

}
