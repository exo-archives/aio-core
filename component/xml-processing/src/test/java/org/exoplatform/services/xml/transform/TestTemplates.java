/**
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

package org.exoplatform.services.xml.transform;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.xml.BaseTest;
import org.exoplatform.services.xml.transform.trax.TRAXTemplatesService;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class TestTemplates extends BaseTest {
  
  private TRAXTemplatesService traxTemplatesService;

  public void setUp() throws Exception {
    StandaloneContainer.setConfigurationPath(Thread.currentThread().getContextClassLoader()
        .getResource("conf/standalone/test-configuration.xml").getPath());
    StandaloneContainer container = StandaloneContainer.getInstance();
    traxTemplatesService = (TRAXTemplatesService) container
        .getComponentInstanceOfType(TRAXTemplatesService.class);
    assertNotNull("traxTemplatesService", traxTemplatesService);
  }
  
  public void testTemplates() {
    assertNotNull(traxTemplatesService.getTemplates("xslt1"));
  }

}

