/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.security.impl.mock;


import javax.security.auth.Subject;
import javax.security.auth.callback.*;

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;

public class MockCallbackHandler implements CallbackHandler {
  private String login;
  private char[] password;

  public MockCallbackHandler(String login, String password) {    
    this.login = login;
    this.password = password.toCharArray();
  }

  public MockCallbackHandler(String login, char[] password) {       
    this.login = login;
    this.password = password;
  }

  public void handle(Callback[] callbacks)throws IOException, UnsupportedCallbackException {   
    for (int i = 0; i < callbacks.length; i++) {
      if (callbacks[i] instanceof NameCallback) {
        ((NameCallback) callbacks[i]).setName(login);
      } else if (callbacks[i] instanceof PasswordCallback) {         
        ((PasswordCallback) callbacks[i]).setPassword(password);
      } else {
        throw new UnsupportedCallbackException(callbacks[i], "Callback class not supported");
      }
    }
  }
}


