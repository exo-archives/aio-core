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

  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
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
