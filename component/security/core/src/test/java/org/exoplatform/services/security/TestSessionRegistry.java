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

package org.exoplatform.services.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.login.LoginException;

import junit.framework.TestCase;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.security.impl.DefaultRolesExtractorImpl;

/**
 * Created y the eXo platform team User: Benjamin Mestrallet Date: 28 avr. 2004
 */
public class TestSessionRegistry extends TestCase {

  protected ConversationRegistry registry;

  protected Authenticator        authenticator;

  protected ListenerService      listenerService;

  public TestSessionRegistry(String name) {
    super(name);
  }

  protected void setUp() throws Exception {

    if (registry == null) {
      String containerConf = TestLoginModule.class.getResource("/conf/standalone/test-configuration.xml")
                                                  .toString();
      String loginConf = TestLoginModule.class.getResource("/login.conf").toString();
      StandaloneContainer.addConfigurationURL(containerConf);
      if (System.getProperty("java.security.auth.login.config") == null)
        System.setProperty("java.security.auth.login.config", loginConf);

      StandaloneContainer manager = StandaloneContainer.getInstance();

      authenticator = (DummyAuthenticatorImpl) manager.getComponentInstanceOfType(DummyAuthenticatorImpl.class);
      registry = (ConversationRegistry) manager.getComponentInstanceOfType(ConversationRegistry.class);
      assertNotNull(registry);

      listenerService = (ListenerService) manager.getComponentInstanceOfType(ListenerService.class);

    }

    registry.clear();

  }

  public void testRegistry() throws Exception {
    Credential[] cred = new Credential[] { new UsernameCredential("exo") };

    String userId = authenticator.validateUser(cred);
    assertEquals("exo", userId);

    try {
      cred[0] = new UsernameCredential("enemy");
      authenticator.validateUser(cred);
      fail("login exception have been thrown");
    } catch (LoginException e) {
    }

    Identity id = authenticator.createIdentity(userId);
    ConversationState s = new ConversationState(id);
    ConversationState.setCurrent(s);
    assertEquals(s, ConversationState.getCurrent());

    registry.register("key", s);
    assertNotNull(registry.getState("key"));
    assertEquals(id, registry.getState("key").getIdentity());

    registry.unregister("key");

    assertNull(registry.getState("key"));

  }

  public void testMemberships() throws Exception {
    MembershipEntry me = new MembershipEntry("exo");
    assertEquals("*", me.getMembershipType());

    Set<MembershipEntry> memberships = new HashSet<MembershipEntry>();
    memberships.add(new MembershipEntry("exogroup"));
    memberships.add(new MembershipEntry("exogroup1", "member"));

    Identity session = new Identity("exo", memberships);
    assertTrue(session.getGroups().size() > 1);

    assertTrue(session.isMemberOf("exogroup"));
    assertTrue(session.isMemberOf("exogroup1"));
    assertTrue(session.isMemberOf("exogroup", "member"));
    assertTrue(session.isMemberOf("exogroup1", "member"));
    assertFalse(session.isMemberOf("exogroup1", "validator"));
  }

  public void testDefaultRolesExtractor() throws Exception {
    Set<MembershipEntry> memberships = new HashSet<MembershipEntry>();
    memberships.add(new MembershipEntry("exogroup"));
    memberships.add(new MembershipEntry("exogroup/exogroup1/exogroup2", "member"));
    DefaultRolesExtractorImpl extractor = new DefaultRolesExtractorImpl();
    extractor.setUserRoleParentGroup("exogroup");
    Set<String> roles = extractor.extractRoles("exo", memberships);
    assertEquals(2, roles.size());
    assertTrue(roles.contains("exogroup"));
    assertTrue(roles.contains("exogroup2"));
    Identity session = new Identity("exo", memberships, roles);
    Collection<String> roles2 = session.getRoles();
    assertEquals(2, roles2.size());
  }

}
