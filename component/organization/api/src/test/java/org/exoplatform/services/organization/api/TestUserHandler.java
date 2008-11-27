/*
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
package org.exoplatform.services.organization.api;

import java.net.URL;

import junit.framework.TestCase;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.organization.BaseOrganizationService;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.services.organization.auth.TestOrganizationAuthenticator;
import org.exoplatform.services.security.ConversationRegistry;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a>
 * @version $Id: TestUserHandler.java 111 2008-11-11 11:11:11Z $
 */
public class TestUserHandler extends TestCase {

  protected ConversationRegistry  registry;

  private BaseOrganizationService organizationService;

  private UserHandler             uHandler;

  protected void setUp() throws Exception {
    super.setUp();

    if (registry == null) {
      URL containerConfURL = TestOrganizationAuthenticator.class.getResource("/conf/standalone/test-configuration.xml");
      assertNotNull(containerConfURL);

      String containerConf = containerConfURL.toString();
      StandaloneContainer.addConfigurationURL(containerConf);
      StandaloneContainer container = StandaloneContainer.getInstance();

      organizationService = (BaseOrganizationService) container.getComponentInstance(org.exoplatform.services.organization.OrganizationService.class);
      assertNotNull(organizationService);

      uHandler = organizationService.getUserHandler();

      registry = (ConversationRegistry) container.getComponentInstanceOfType(ConversationRegistry.class);
      assertNotNull(registry);
    }
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /**
   * Authenticate users.
   */
  public void testAuthenticate() {
    try {
      assertTrue(uHandler.authenticate("demo", "exo"));
      assertFalse(uHandler.authenticate("demo", "exo_"));
      assertFalse(uHandler.authenticate("_demo_", "exo"));

    } catch (Exception e) {
      e.printStackTrace();
      fail("Exception should not be thrown.");
    }
  }

}
