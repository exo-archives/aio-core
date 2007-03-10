/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.test.BasicTestCase;

/*
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestDatabaseService.java 5332 2006-04-29 18:32:44Z geaz $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestHibernateService extends BasicTestCase {
  HibernateService hservice_ ;
  public TestHibernateService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    hservice_ = (HibernateService) pcontainer.getComponentInstanceOfType(HibernateService.class) ;
  }

  public void testDabaseService() throws Exception {
    //assertTrue("Expect hibernate service instance" , hservice_ != null) ;
    assertTrue("Expect database service instance" , hservice_ != null) ;
  }
  
  protected String getDescription() {
    return "Test Database Service" ;
  }
}