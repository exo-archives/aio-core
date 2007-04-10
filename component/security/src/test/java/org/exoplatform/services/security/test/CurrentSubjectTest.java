/**
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.security.test;

import javax.jcr.Credentials;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import junit.framework.TestCase;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.security.SecurityService;
import org.exoplatform.services.security.jaas.BasicCallbackHandler;

/**
 * Created by The eXo Platform SARL        .
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public class CurrentSubjectTest extends TestCase {
  
  protected StandaloneContainer container;
  
  public CurrentSubjectTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    System.setProperty("java.security.auth.login.config", "src/main/resource/login.conf" );

    if(container == null) {
      StandaloneContainer.setConfigurationPath("src/test/java/conf/standalone/test-configuration.xml");
      container = StandaloneContainer.getInstance();
    }
  }
  
  public void testCurrentSubject() throws Exception {
    BasicCallbackHandler handler = new BasicCallbackHandler("exo", "exo".toCharArray());
    
    LoginContext loginContext = new LoginContext("eXo", handler);
    loginContext.login();
    assertNotNull(loginContext.getSubject());
    
    SecurityService service = (SecurityService) container.getComponentInstanceOfType(SecurityService.class);
    assertNotNull(service.getSubject("exo"));
    assertNotNull(service.getCurrentSubject());
    assertEquals(service.getSubject("exo"), service.getCurrentSubject());
    
    Subject subj = service.getCurrentSubject();
    assertFalse(subj.getPublicCredentials().isEmpty());
    assertTrue(subj.getPublicCredentials().iterator().next() instanceof Credentials);
  }
  
  public void testMultithread() throws Exception {
    LoginThread thread1 = new LoginThread();
    LoginThread thread2 = new LoginThread();
    thread1.start();
    thread2.start();
    // timeout to make sure run() completed
    Thread.sleep(2000);
    assertNotNull(thread1.subj);
    assertNotNull(thread2.subj);
//    System.out.println(">>> "+thread1.subj+" "+thread2.subj);
    System.out.println(">>> "+thread1.subj.hashCode()+" "+thread2.subj.hashCode());
    
    assertFalse(thread1.subj.hashCode() == thread2.subj.hashCode());
  }
  
  private class LoginThread extends Thread {
    
    Subject subj;
    
    public void run() {
      try {
        
        BasicCallbackHandler handler = new BasicCallbackHandler("exo", "exo".toCharArray());
        
        LoginContext loginContext = new LoginContext("eXo", handler);
        loginContext.login();
        assertNotNull(loginContext.getSubject());
        
        SecurityService service = (SecurityService) container.getComponentInstanceOfType(SecurityService.class);
        
        this.subj = service.getCurrentSubject();
        
      } catch (LoginException e) {
        e.printStackTrace();
      }
    }
  }
}
