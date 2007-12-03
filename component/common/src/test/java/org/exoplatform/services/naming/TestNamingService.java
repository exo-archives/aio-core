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
package org.exoplatform.services.naming;

import javax.naming.Context;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.naming.NamingService;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SAS
 * Author : HoaPham
 *          phamvuxuanhoa@yahoo.com
 * Jan 10, 2006
 */
public class TestNamingService extends BasicTestCase {
  private NamingService service_ ;
  
  public TestNamingService(String name) {
    super(name) ;
  }
  
  public void setUp() throws Exception  {
    if(service_== null) {
      PortalContainer manager = PortalContainer.getInstance() ;
      service_ = (NamingService)manager.getComponentInstanceOfType(NamingService.class) ;
    }        
  }
  
  public void tearDown() throws Exception {
    
  }
  
  public void testNamingService() throws Exception { 
    Context context =  service_.getContext() ;
    context.createSubcontext("test") ;
    context.bind("test/hello", "hello") ;
  }
}
