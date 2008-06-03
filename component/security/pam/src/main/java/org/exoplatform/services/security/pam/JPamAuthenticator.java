/**
 *  Copyright 2003-2007 Greg Luck
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.exoplatform.services.security.pam;

import java.util.Collection;
import java.util.HashSet;

import javax.security.auth.login.LoginException;

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

public class JPamAuthenticator implements Authenticator {

  private static final Log LOGGER = ExoLogger.getLogger("JPamAuthenticator");
  private static final String PROPERTIES_NAME = "jpam-configuration";
  private static final String SERVICE_NAME_PARAM = "service-name";
  private static final String DEFAULT_SERVICE_NAME = "exo-jpam";
  private String serviceName_;

  public JPamAuthenticator(InitParams params) {
    PropertiesParam pparams = params.getPropertiesParam(PROPERTIES_NAME);
    if (pparams == null || pparams.getProperty(SERVICE_NAME_PARAM) == null)
      LOGGER.warn("Properties param were not found in configuration.xml. "
          + "Service name unknown, " + DEFAULT_SERVICE_NAME + " will be used.");
    else
      serviceName_ = pparams.getProperty(SERVICE_NAME_PARAM);
  }

   public JPamAuthenticator() {
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
    Pam pam = new Pam(serviceName_ == null ? DEFAULT_SERVICE_NAME
        : serviceName_);
    PamReturnValue res = null;
    if (!(res = pam.authenticate(user, pass))
        .equals(PamReturnValue.PAM_SUCCESS))
      throw new LoginException("Authentication failed. Status : "
          + res.toString());
    LOGGER.debug("Authentication for user '" + user + "' success.");
    Collection<MembershipEntry> entries = new HashSet<MembershipEntry>();
    for (String g : pam.getGroups())
      entries.add(new MembershipEntry(g));
    return new Identity(user, entries);
    //identity.setMemberships(entries);
  }



}
