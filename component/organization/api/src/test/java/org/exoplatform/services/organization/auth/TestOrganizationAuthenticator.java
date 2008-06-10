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

import java.net.URL;

import junit.framework.TestCase;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.security.Authenticator;
import org.exoplatform.services.security.Credential;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.ConversationRegistry;
import org.exoplatform.services.security.PasswordCredential;
import org.exoplatform.services.security.UsernameCredential;

/**
 * Created y the eXo platform team User: Benjamin Mestrallet Date: 28 avr. 2004
 */
public class TestOrganizationAuthenticator extends TestCase {

  protected ConversationRegistry registry;
  protected Authenticator    authenticator;

  public TestOrganizationAuthenticator(String name) {
    super(name);
  }

  protected void setUp() throws Exception {

    if (registry == null) {
      URL containerConfURL = TestOrganizationAuthenticator.class.getResource("/conf/standalone/test-configuration.xml");
      assertNotNull(containerConfURL);
      String containerConf = containerConfURL.toString();
      URL loginConfURL = TestOrganizationAuthenticator.class.getResource("/login.conf");
      assertNotNull(loginConfURL);
      String loginConf = loginConfURL.toString();
      StandaloneContainer.addConfigurationURL(containerConf);
      if (System.getProperty("java.security.auth.login.config") == null)
        System.setProperty("java.security.auth.login.config", loginConf);

      StandaloneContainer container = StandaloneContainer.getInstance();

      authenticator = (Authenticator) container.getComponentInstanceOfType(OrganizationAuthenticatorImpl.class);
      assertNotNull(authenticator);

      //System.out.println(">>>>>>>>>>>>>>>>>> " + authenticator);
      registry = (ConversationRegistry) container.getComponentInstanceOfType(ConversationRegistry.class);
      assertNotNull(registry);

    }

  }

  public void testAuthenticator() throws Exception {
    assertNotNull(authenticator);
    assertTrue(authenticator instanceof OrganizationAuthenticatorImpl);
    Credential[] cred = new Credential[] { new UsernameCredential("admin"), new PasswordCredential("admin") };
    String userId = authenticator.validateUser(cred);
    assertEquals("admin", userId);
    Identity identity = authenticator.createIdentity(userId);
    assertTrue(identity.getGroups().size() > 0);
  }
}
