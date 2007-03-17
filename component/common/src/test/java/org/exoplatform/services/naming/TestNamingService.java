/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.naming;

import javax.naming.Context;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.naming.NamingService;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SARL
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
