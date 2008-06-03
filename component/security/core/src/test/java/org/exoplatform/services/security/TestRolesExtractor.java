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

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.security.impl.DefaultRolesExtractorImpl;

/**
 * Created by The eXo Platform SAS .
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey Zavizionov</a>
 * @version $Id: $
 *
 * Mar 27, 2008  
*/
public class TestRolesExtractor extends TestCase {

  private static Log       log = ExoLogger.getLogger("pc.TestRolesExtractor");

  protected RolesExtractor rolesExtractor;

  public TestRolesExtractor(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    if (rolesExtractor == null) {
      String containerConf = TestRolesExtractor.class.getResource("/conf/standalone/test-configuration.xml").toString();
      StandaloneContainer.addConfigurationURL(containerConf);

      StandaloneContainer manager = StandaloneContainer.getInstance();

      rolesExtractor = (DefaultRolesExtractorImpl) manager.getComponentInstanceOfType(RolesExtractor.class);
      assertNotNull(rolesExtractor);
    }
  }

  public void testExtractRoles1() throws Exception {

    Set<String> groups = getGroups1();

    Set<String> extractRoles = rolesExtractor.extractRoles(groups);
    assertNotNull(extractRoles);
    assertFalse(extractRoles.isEmpty());
    assertEquals(2, extractRoles.size());
    assertTrue(extractRoles.contains("admin"));
    assertTrue(extractRoles.contains("exo"));

    ((DefaultRolesExtractorImpl) rolesExtractor).setUserRoleParentGroup("platform");
  }

  public void testExtractRoles2() throws Exception {

    Set<String> groups = getGroups2();

    ((DefaultRolesExtractorImpl) rolesExtractor).setUserRoleParentGroup("platform");
    Set<String> extractRoles = rolesExtractor.extractRoles(groups);
    assertNotNull(extractRoles);
    assertFalse(extractRoles.isEmpty());
    assertEquals(5, extractRoles.size());
    assertTrue(extractRoles.contains("organization"));
    assertTrue(extractRoles.contains("administrators"));
    assertTrue(extractRoles.contains("users"));

  }

  /**
   * @return set of groups to which this user belongs to
   */
  private Set<String> getGroups1() {
    Set<String> groups = new HashSet<String>();
    groups.add("/admin");
    groups.add("/exo");
    return groups;
  }

  /**
   * @return set of groups to which this user belongs to
   */
  private Set<String> getGroups2() {
    Set<String> groups = new HashSet<String>();
    groups.add("/admin");
    groups.add("/exo");
    groups.add("/organization/management/executive-board");
    groups.add("/platform/administrators");
    groups.add("/platform/users");
    return groups;
  }

}
