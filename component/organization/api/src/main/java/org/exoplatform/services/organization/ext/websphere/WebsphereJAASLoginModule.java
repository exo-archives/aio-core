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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.security.jaas.DefaultLoginModule;
import org.exoplatform.services.security.jaas.RolePrincipal;
import org.exoplatform.services.security.jaas.UserPrincipal;

/**
 * Created y the eXo platform team User: Tuan Nguyen Date: May 6th, 2007
 * 
 * @version $Id: WebsphereJAASLoginModule.java 8478 2007-12-03 10:45:34Z rainf0x
 *          $
 */
public class WebsphereJAASLoginModule extends DefaultLoginModule {

  private Log log = ExoLogger.getLogger("core.ExoWebsphereJAASLoginModule");

  public WebsphereJAASLoginModule() {
  }

  @Override
  public boolean login() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In login of WebsphereJAASLoginModule");
    if (super.login()) {
      ArrayList<String> roleGroupList = new ArrayList<String>();

      for (String role : identity.getRoles()) {
        roleGroupList.add(role);
      }

      // username principal
      // Principal usernamePrincipal = new UserPrincipal(identity_.getUserId());

      websphereLogin(identity.getUserId(), roleGroupList);

      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean commit() throws LoginException {

    if (super.commit()) {

      Set<Principal> principals = subject.getPrincipals();

      for (String role : identity.getRoles())
        principals.add(new RolePrincipal(role));

      // username principal
      principals.add(new UserPrincipal(identity.getUserId()));

      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean abort() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In abort of WebsphereJAASLoginModule");
    return super.abort();
  }

  @Override
  public boolean logout() throws LoginException {
    if (log.isDebugEnabled())
      log.debug("In logout of WebsphereJAASLoginModule");
    // getSubject().getPrincipals().remove(usernamePrincipal);
    return super.logout();
  }

  final public static String WSCREDENTIAL_PROPERTIES_KEY = "com.ibm.wsspi.security.cred.propertiesObject";

  final public static String WSCREDENTIAL_UNIQUEID       = "com.ibm.wsspi.security.cred.uniqueId";

  final public static String WSCREDENTIAL_SECURITYNAME   = "com.ibm.wsspi.security.cred.securityName";

  final public static String WSCREDENTIAL_GROUPS         = "com.ibm.wsspi.security.cred.groups";

  final public static String WSCREDENTIAL_CACHE_KEY      = "com.ibm.wsspi.security.cred.cacheKey";

  private void websphereLogin(String user, ArrayList<String> roleGroupList) {
    Hashtable hashtable = new Hashtable();
    String uniqueid = user;
    hashtable.put(WSCREDENTIAL_UNIQUEID, uniqueid);
    hashtable.put(WSCREDENTIAL_SECURITYNAME, user);
    hashtable.put(WSCREDENTIAL_GROUPS, roleGroupList);
    hashtable.put(WSCREDENTIAL_CACHE_KEY, uniqueid + "WebsphereJAASLoginModule");
    // sharedState.put(WSCREDENTIAL_PROPERTIES_KEY, hashtable);
    subject.getPublicCredentials().add(hashtable);
  }
}
