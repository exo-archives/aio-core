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

package org.exoplatform.services.security.j2ee;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.MalformedObjectNameException;

import org.exoplatform.services.security.jaas.DefaultLoginModule;
import org.exoplatform.services.security.jaas.JAASGroup;
import org.exoplatform.services.security.jaas.RolePrincipal;
import org.exoplatform.services.security.jaas.UserPrincipal;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.container.monitor.jvm.J2EEServerInfo;
import org.apache.commons.logging.Log;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author Gennady Azarenkov
 * @version $Id$
 */

public class JbossLoginModule extends DefaultLoginModule {
  /** . */
  private static Log log = ExoLogger.getLogger(JbossLoginModule.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean commit() throws LoginException {

    if (super.commit()) {

      Set<Principal> principals = subject.getPrincipals();

      Group roleGroup = new JAASGroup(JAASGroup.ROLES);
      for (String role : identity.getRoles())
        roleGroup.addMember(new RolePrincipal(role));

      // group principal
      principals.add(roleGroup);

      // username principal
      principals.add(new UserPrincipal(identity.getUserId()));

      return true;
    } else {
      return false;
    }

  }

  /**
   * Attempts eviction of the subject in the JBoss security manager cache.
   *
   * @return a boolean
   * @throws LoginException any login exception
   */
  @Override
  public boolean logout() throws LoginException {
    org.exoplatform.container.monitor.jvm.J2EEServerInfo info = new J2EEServerInfo();
    MBeanServer jbossServer = info.getMBeanServer();

    //
    if (jbossServer != null) {
      try {

        log.debug("Performing JBoss security manager cache eviction");

        ObjectName securityManagerName = new ObjectName("jboss.security:service=JaasSecurityManager");

        // Obtain user name
        String userName = null;
        Set<UserPrincipal> userPrincipals = subject.getPrincipals(UserPrincipal.class);
        if (!userPrincipals.isEmpty()) {
          // There should be one
          userName = userPrincipals.iterator().next().getName();
        }

        //
        if (userName != null) {
          log.debug("Going to perform JBoss security manager cache eviction for user " + userName);

          //
          List allPrincipals = (List)
            jbossServer.invoke(
              securityManagerName,
              "getAuthenticationCachePrincipals",
              new Object[]{"exo-domain"},
              new String[]{String.class.getName()});

          // Make a copy to avoid some concurrent mods
          allPrincipals = new ArrayList(allPrincipals);

          // Lookup for invalidation key, it must be the same principal!
          Principal key = null;
          for (Iterator i = allPrincipals.iterator();i.hasNext();) {
            Principal principal = (Principal)i.next();
            if (principal.getName().equals(userName)) {
              key = principal;
              break;
            }
          }

          // Perform invalidation
          if (key != null) {
            jbossServer.invoke(
              securityManagerName,
              "flushAuthenticationCache",
              new Object[]{"exo-domain",key},
              new String[]{String.class.getName(),Principal.class.getName()});
            log.debug("Performed JBoss security manager cache eviction for user " + userName + " with principal " + key);
          } else {
            log.warn("No principal found when performing JBoss security manager cache eviction for user " + userName);
          }
        } else {
          log.warn("No user name found when performing JBoss security manager cache eviction");
        }
      }
      catch (Exception e) {
        log.error("Could not perform JBoss security manager cache eviction", e);
      }
    } else {
      log.debug("Could not find mbean server for performing JBoss security manager cache eviction");
    }

    //
    return true;
  }
}
