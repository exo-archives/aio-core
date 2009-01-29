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
package org.exoplatform.services.security;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

//import org.exoplatform.services.organization.Membership;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform exo@exoplatform.com 24
 * févr. 08
 */
public class TestIdentity extends TestCase {

  Identity                    identity    = null;

  Collection<MembershipEntry> memberships = null;

  protected void setUp() {
    // common setup for testIsInGroup and testHasMembership

    memberships = new ArrayList<MembershipEntry>();
    memberships.add(new MembershipEntry("/group1", "*"));
    memberships.add(new MembershipEntry("/group2", "member"));

    identity = new Identity("user", memberships);
  }

  public void testIsInGroup() {
    assertTrue("user in group /group1", identity.isMemberOf("/group1"));
    assertTrue("user in group /group2", identity.isMemberOf("/group2"));
    assertFalse("user in group /non/existing/group", identity.isMemberOf("non/existing/group"));
  }

  public void testHasMembership() {
    assertTrue("membership * in group /group1", identity.isMemberOf("/group1", "*"));
    assertTrue("membership member in group /group2", identity.isMemberOf("/group2", "member"));
    assertTrue("membership * in group /group1", identity.isMemberOf("/group2", "*"));
    // any membership of /group1
    assertFalse("membership null in group group1", identity.isMemberOf("group1", null));
    assertFalse("membership null in group group1", identity.isMemberOf("group1"));
  }

}
