/**
 * Copyright (C) 2003-2008 eXo Platform SAS.
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

package org.exoplatform.services.security.ntlm;

import java.net.UnknownHostException;
import java.util.HashSet;

import javax.security.auth.login.LoginException;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;

import org.apache.commons.logging.Log;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.PropertiesParam;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.security.Authenticator;
import org.exoplatform.services.security.Credential;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.MembershipEntry;
import org.exoplatform.services.security.PasswordCredential;
import org.exoplatform.services.security.UsernameCredential;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class NTLMAuthenticator implements Authenticator {

  private final static Log LOGGER = ExoLogger
      .getLogger("core.NTLMAuthenticator");

  private String domainControllerName_;

  private static final String PROPERTIES_NAME = "ntlm-configuration";

  private static final String DOMAIN = "domain";

  public NTLMAuthenticator(InitParams params) {
    // super(registry);
    PropertiesParam pparams = params.getPropertiesParam(PROPERTIES_NAME);
    if (pparams == null || pparams.getProperty(DOMAIN) == null)
      LOGGER.warn("Properties param were not found in configuration.xml. "
          + "Domain name is not specified, it should be passed by user.");
  }

  public NTLMAuthenticator() {
  }

  public Identity authenticate(Credential[] credentials) throws LoginException,
      Exception {
    String user = null;
    String pass = null;
    for (Credential cred : credentials) {
      if (cred instanceof UsernameCredential)
        user = ((UsernameCredential) cred).getUsername();
      if (cred instanceof PasswordCredential)
        pass = ((PasswordCredential) cred).getPassword();
    }
    int backSlash = user.indexOf('\\');
    String domainControllerName;
    if (backSlash != -1) {
      domainControllerName = user.substring(0, backSlash);
      user = user.substring(backSlash + 1);
    } else
      domainControllerName = domainControllerName_;
    if (domainControllerName == null) {
      LOGGER.error("Authentication failed, domain controller name is null.");
      throw new LoginException("Domain controller name is null.");
    }
    UniAddress domainController;
    try {
      domainController = UniAddress.getByName(domainControllerName, true);
    } catch (UnknownHostException e) {
      LOGGER.error("Authentication failed, domain controller not found.");
      throw new LoginException("Domain controller not found.");
    }
    try {
      SmbSession.logon(domainController, new NtlmPasswordAuthentication(
          domainControllerName, user, pass));
    } catch (SmbException e) {
      LOGGER.error("Authentication failed: " + e.getMessage());
      throw new LoginException(e.getMessage());
    }
    // TODO: getting group for user and then create set of memberships.
    // identity.setMemberships(new HashSet<MembershipEntry>());

    return new Identity(user, new HashSet<MembershipEntry>());
  }

  // /*
  // * (non-Javadoc)
  // *
  // * @see org.exoplatform.services.security.Authenticator#initIdentity(
  // * org.exoplatform.services.security.Credential[])
  // */
  //
  // @Override
  // protected void initIdentity(Identity identity, Credential[] credentials)
  // throws LoginException, Exception {
  //    
  //
  // }

}
