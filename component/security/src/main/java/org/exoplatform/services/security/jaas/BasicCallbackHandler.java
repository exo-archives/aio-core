/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.security.jaas;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;


public class BasicCallbackHandler implements CallbackHandler {
  private String login;
  private char[] password;

  public BasicCallbackHandler(String login, char[] password) {       
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


