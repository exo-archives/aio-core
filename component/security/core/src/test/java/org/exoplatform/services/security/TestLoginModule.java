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

import java.net.URL;

import javax.security.auth.login.LoginContext;

import junit.framework.TestCase;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.security.jaas.BasicCallbackHandler;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 28 avr. 2004
 */
public class TestLoginModule extends TestCase {

  protected ConversationRegistry conversationRegistry;
  protected IdentityRegistry identityRegistry;
  protected Authenticator    authenticator;

  public TestLoginModule(String name) {
    super(name);
  }

  protected void setUp() throws Exception {

    if (conversationRegistry == null) {
      URL containerConfURL = TestLoginModule.class.getResource("/conf/standalone/test-configuration.xml");
      assertNotNull(containerConfURL);
      String containerConf = containerConfURL.toString();
      URL loginConfURL = TestLoginModule.class.getResource("/login.conf");
      assertNotNull(loginConfURL);
      String loginConf = loginConfURL.toString();
      StandaloneContainer.addConfigurationURL(containerConf);
      if (System.getProperty("java.security.auth.login.config") == null)
        System.setProperty("java.security.auth.login.config", loginConf);

      StandaloneContainer manager = StandaloneContainer.getInstance();

      authenticator = (DummyAuthenticatorImpl) manager.getComponentInstanceOfType(DummyAuthenticatorImpl.class);
      assertNotNull(authenticator);
      conversationRegistry = (ConversationRegistry) manager.getComponentInstanceOfType(ConversationRegistry.class);
      assertNotNull(conversationRegistry);
      identityRegistry = (IdentityRegistry) manager.getComponentInstanceOfType(IdentityRegistry.class);
      assertNotNull(identityRegistry);

    }
    identityRegistry.clear();
    conversationRegistry.clear();
  }

  public void testLogin() throws Exception {
    //System.out.println(">>>>>>>>>>>>>>>>>> " + registry.getSession().getUserId());
    //registry.unregister();
    //assertNull(registry.getSession());
    BasicCallbackHandler handler = new BasicCallbackHandler("exo", "exo".toCharArray());
    LoginContext loginContext = new LoginContext("exo", handler);
    loginContext.login();
    
//    assertNotNull(conversationRegistry.getState("exo").getIdentity());
    assertNotNull(identityRegistry.getIdentity("exo"));
//    assertEquals("exo", conversationRegistry.getState("exo").getIdentity().getUserId());
    assertEquals("exo", identityRegistry.getIdentity("exo").getUserId());

//    assertEquals(1, conversationRegistry.getState("exo").getIdentity().getGroups().size());
    assertEquals(1, identityRegistry.getIdentity("exo").getGroups().size());
    
    conversationRegistry.register("exo", new ConversationState(identityRegistry.getIdentity("exo")));
    assertNotNull(conversationRegistry.getState("exo"));
    

  }

  public void testRepeatLoginSameCredentials() throws Exception {

    BasicCallbackHandler handler = new BasicCallbackHandler("exo", "exo".toCharArray());
    LoginContext loginContext = new LoginContext("exo", handler);
    loginContext.login();
//    Identity id = conversationRegistry.getState("exo").getIdentity();
    Identity id = identityRegistry.getIdentity("exo");
    
    handler = new BasicCallbackHandler("exo", "exo".toCharArray());
    loginContext = new LoginContext("exo", handler);
    loginContext.login();
//    assertNotSame(id, conversationRegistry.getState("exo").getIdentity());
    assertSame(id, identityRegistry.getIdentity("exo"));
    
//    assertNotNull(conversationRegistry.getState("exo"));
//    assertEquals(1, conversationRegistry.getStateKeys("exo").size());
    
  }

  public void testRepeatLoginLogoutSameCredentials() throws Exception {
    BasicCallbackHandler handler = new BasicCallbackHandler("exo", "exo".toCharArray());
    LoginContext loginContext = new LoginContext("exo", handler);
    loginContext.login();
//    Identity id = conversationRegistry.getState("exo").getIdentity();
    Identity id = identityRegistry.getIdentity("exo");
    ConversationState s1 = new ConversationState(id);//conversationRegistry.getState("exo");
    conversationRegistry.register("exo1", s1);
    ConversationState.setCurrent(s1);
    //assertNotNull(s1.getSubject());
    assertNotNull(conversationRegistry.getState("exo1"));
    loginContext.logout();
    
//    handler = new BasicCallbackHandler("exo", "exo".toCharArray());
//    loginContext = new LoginContext("exo", handler);
//    loginContext.login();
//    assertNotSame(s1, conversationRegistry.getState("exo"));
//    //assertNotSame(s1.getSubject(), registry.getState("exo").getSubject());
//    assertNotSame(id, conversationRegistry.getState("exo").getIdentity());
//    assertEquals(id.getUserId(), conversationRegistry.getState("exo").getIdentity().getUserId());
//    //assertEquals(id, registry.getIdentity());
  }
  
/*
  public void testRepeatLoginLogoutSameCredentialsAndPrincipals() throws Exception {
    BasicCallbackHandler handler = new BasicCallbackHandler("exo", "exo".toCharArray());
    LoginContext loginContext = new LoginContext("exo", handler);
    loginContext.login();
    
    Subject subject = null;
    Set<Principal> setPs = null;
    int sizeOfPrincipal;
    // 1 principal
    subject = loginContext.getSubject();
    assertNotNull(subject);
    setPs = subject.getPrincipals();
    assertNotNull(setPs);
    sizeOfPrincipal = setPs.size();
    
    Identity id = registry.getIdentity();
    loginContext.logout();
    assertNull(registry.getIdentity());
    handler = new BasicCallbackHandler("exo", "exo".toCharArray());
    loginContext = new LoginContext("exo", handler);
    loginContext.login();
    // 2 principal
    subject = loginContext.getSubject();
    assertNotNull(subject);
    setPs = subject.getPrincipals();
    assertNotNull(setPs);
    
    assertSame(sizeOfPrincipal, setPs.size());
    
    assertNotSame(id, registry.getIdentity());
    assertEquals(id.getUserId(), registry.getIdentity().getUserId());
    //assertEquals(id, registry.getIdentity());
  }
*/
  
  
//  public void testUri() throws Exception {
//    //URI uri = new URI("jcr://#uuid");  //id only (repo == null, ws == "")
//    //URI uri = new URI("jcr://repo#uuid"); //id and repository=repo (ws == "")
//    //URI uri = new URI("jcr://repo/ws/#uuid"); //id=uuid and repository=repo and ws=ws (cut leading and trailing / on ws!)
//    URI uri = new URI("//repo/ws/#uuid"); //scheme=null
//    
//    System.out.println(" "+uri.getScheme()+" "+uri.getHost()+" "+uri.getPath()+" "+uri.getFragment());
//  }

}
