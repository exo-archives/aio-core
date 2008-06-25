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
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.security.Authenticator;
import org.exoplatform.services.security.Credential;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.MembershipEntry;
import org.exoplatform.services.security.PasswordCredential;
import org.exoplatform.services.security.PasswordEncrypter;
import org.exoplatform.services.security.RolesExtractor;
import org.exoplatform.services.security.UsernameCredential;

/**
 * Created by The eXo Platform SAS        .
 * An authentication wrapper over Organization service
 * 
 * TODO move it to Organization Service / Auth
 * @author Gennady Azarenkov
 * @version $Id:$
 */

public class OrganizationAuthenticatorImpl implements Authenticator {

  protected static Log log = ExoLogger.getLogger("org.exoplatform.services.organization.auth.OrganizationUserRegistry");

  private final OrganizationService orgService;
  private final PasswordEncrypter   encrypter;
  private final RolesExtractor rolesExtractor;

  public OrganizationAuthenticatorImpl(OrganizationService orgService,
      RolesExtractor rolesExtractor, PasswordEncrypter encrypter) {
    this.orgService = orgService;
    this.encrypter = encrypter;
    this.rolesExtractor = rolesExtractor;
  }

  public OrganizationAuthenticatorImpl(OrganizationService orgService,
      RolesExtractor rolesExtractor) {
    this(orgService, rolesExtractor, null);
  }
  
  public OrganizationAuthenticatorImpl(OrganizationService orgService) {
    this(orgService, null, null);
  }
  
  public OrganizationService getOrganizationService() {
    return orgService;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.security.Authenticator#createIdentity(java.lang.String)
   */
  public Identity createIdentity(String userId) throws Exception {
    Set<MembershipEntry> entries = new HashSet<MembershipEntry>();
    Collection<Membership> memberships = orgService.getMembershipHandler().findMembershipsByUser(userId);
    if (memberships != null) {
      for (Membership membership : memberships)
        entries.add(new MembershipEntry(membership.getGroupId(), membership.getMembershipType()));
    }
    if (rolesExtractor == null)
      return new Identity(userId, entries);
    return new Identity(userId, entries, rolesExtractor.extractRoles(userId, entries));
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.security.Authenticator#validateUser(org.exoplatform.services.security.Credential[])
   */
  public String validateUser(Credential[] credentials) throws LoginException, Exception {
    String user = null;
    String password = null;
    for (Credential cred : credentials) {
      if (cred instanceof UsernameCredential)
        user = ((UsernameCredential) cred).getUsername();
      if (cred instanceof PasswordCredential)
        password = ((PasswordCredential) cred).getPassword();
    }
    if (user == null || password == null)
      throw new LoginException("Username or Password is not defined");

    if (this.encrypter != null)
      password = new String(encrypter.encrypt(password.getBytes()));

    if (!orgService.getUserHandler().authenticate(user, password))
      throw new LoginException("Login failed for " + user);
    
    return user;
  }
  
}
