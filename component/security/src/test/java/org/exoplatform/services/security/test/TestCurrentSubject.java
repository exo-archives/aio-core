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
 * Created by The eXo Platform SAS        .
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public class TestCurrentSubject extends TestCase {
  
  protected StandaloneContainer container;
  
  public TestCurrentSubject(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    String containerConf = TestCurrentSubject.class.getResource("/conf/standalone/test-configuration.xml").toString();
    String loginConf = TestCurrentSubject.class.getResource("/login.conf").toString();
    
    if(container == null) {
      StandaloneContainer.addConfigurationURL(containerConf);
      if (System.getProperty("java.security.auth.login.config") == null)
        System.setProperty("java.security.auth.login.config", loginConf);
      
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
    System.out.println(">>> "+thread1.subj.toString()+" "+thread2.subj.toString());
    
    // TODO [PN] 28.11.2007 Seems objects should be same
    //assertFalse(thread1.subj.hashCode() == thread2.subj.hashCode());
    assertEquals(thread1.subj.hashCode(), thread2.subj.hashCode());
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
