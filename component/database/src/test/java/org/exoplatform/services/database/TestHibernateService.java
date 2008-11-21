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
package org.exoplatform.services.database;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.test.BasicTestCase;

/*
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestDatabaseService.java 5332 2006-04-29 18:32:44Z geaz $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestHibernateService extends BasicTestCase {
  HibernateService hservice_;

  public TestHibernateService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance();
    hservice_ = (HibernateService) pcontainer.getComponentInstanceOfType(HibernateService.class);
  }

  public void testDabaseService() throws Exception {
    // assertTrue("Expect hibernate service instance" , hservice_ != null) ;
    assertTrue("Expect database service instance", hservice_ != null);
  }

  protected String getDescription() {
    return "Test Database Service";
  }
}
