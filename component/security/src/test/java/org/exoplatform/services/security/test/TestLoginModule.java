/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.security.test;


import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

import junit.framework.TestCase;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.security.SecurityService;
import org.exoplatform.services.security.UserPrincipal;
import org.exoplatform.services.security.jaas.BasicCallbackHandler;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 28 avr. 2004
 */
public class TestLoginModule extends TestCase {
  protected static SecurityService service_;
  protected static OrganizationService orgService_;

  public TestLoginModule(String name) {
    super(name);
  }

  protected String getDescription() {  return "test JAAS Login module"; }

  protected void setUp() throws Exception {
    if (service_ == null) {
      String containerConf = TestLoginModule.class.getResource("/conf/standalone/test-configuration.xml").toString();
      String loginConf = TestLoginModule.class.getResource("/login.conf").toString();      
      StandaloneContainer.addConfigurationURL(containerConf);
      if (System.getProperty("java.security.auth.login.config") == null)
        System.setProperty("java.security.auth.login.config", loginConf);
      
      StandaloneContainer manager = StandaloneContainer.getInstance() ;

      orgService_ = 
        (OrganizationService) manager.getComponentInstanceOfType(OrganizationService.class);
//      ((LogService) manager.getComponentInstanceOfType(LogService.class)).
//          setLogLevel("org.exoplatform.services.security", LogService.DEBUG, false);
      service_ = (SecurityService) manager.getComponentInstanceOfType(SecurityService.class);
    }
  }

  public void testLogin() throws Exception {
    String LOGIN = "exo" ;
    
    System.out.println(">>>>>>>>>>>>>>>>>> "+orgService_);
    User user = orgService_.getUserHandler().createUserInstance();
    user.setUserName(LOGIN);
    user.setPassword("exo");
    user.setFirstName("Exo") ;
    user.setLastName("Platform") ;
    user.setEmail("exo@exoportal.org") ;
    orgService_.getUserHandler().createUser(user, true);
    
//    BasicCallbackHandler handler = new BasicCallbackHandler(LOGIN, "password@default".toCharArray());
    BasicCallbackHandler handler = new BasicCallbackHandler(LOGIN, "exo".toCharArray());

    LoginContext loginContext = new LoginContext("eXo", handler);
    loginContext.login();
    assertNotNull(loginContext.getSubject());
    Subject subject = service_.getSubject(LOGIN);
    assertNotNull(loginContext.getSubject());
    assertSame(loginContext.getSubject(), subject);
    Set principals = subject.getPrincipals(UserPrincipal.class);
    UserPrincipal userPrincipal = ((UserPrincipal) principals.iterator().next());
    assertEquals(LOGIN, userPrincipal.getName());
    Iterator p = subject.getPrincipals(Group.class).iterator();
    if (p.hasNext()) {
      Group roles = (Group) p.next();
      Enumeration roleEnum = roles.members();
      Principal princ = (Principal) roleEnum.nextElement();
//      assertEquals("user", princ.getName());
      assertTrue(roles.isMember(princ));
    }
    loginContext.logout();
    assertNull(service_.getSubject(LOGIN));
  }
}
