/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SAS         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.auth.pam.jaas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Set;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;

import org.exoplatform.services.organization.auth.JAASGroup;
import org.exoplatform.services.organization.auth.pam.jaas.GroupPrincipal;

import static java.lang.System.out;

/**
 * Just for test JpamLoginModule!!!
 * 
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class JPAMTestJAAS {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    LoginContext loginContext = new LoginContext("exo-domain",
        new JPAMTestJAAS().new JpamCallbackHandler());
    loginContext.login();
    out.println(">>> Login seccessful");
    out.println(loginContext.getSubject());
    Set<JAASGroup> gprincipals = loginContext.getSubject().getPrincipals(
        JAASGroup.class);
    if (gprincipals != null && gprincipals.size() != 0) {
      out.println(">>> User is memebr of groups : ");
      for (JAASGroup gp : gprincipals) {
        out.print(gp.getName() + " : ");
        Enumeration<GroupPrincipal> g = gp.members();
        while (g.hasMoreElements()) {
          out.print(g.nextElement().getName() + "; ");
        }
        out.println();
      }
    }
    loginContext.logout();
  }

  class JpamCallbackHandler implements CallbackHandler {

    public void handle(Callback[] callbacks) throws IOException,
        UnsupportedCallbackException {

      BufferedReader reader = new BufferedReader(new InputStreamReader(
          System.in));

      for (int i = 0; i < callbacks.length; i++) {
        if (callbacks[i] instanceof TextOutputCallback) {
          TextOutputCallback toc = (TextOutputCallback) callbacks[i];
          switch (toc.getMessageType()) {
          case TextOutputCallback.INFORMATION:
            System.out.println(toc.getMessage());
            break;
          case TextOutputCallback.ERROR:
            System.out.println("ERROR: " + toc.getMessage());
            break;
          case TextOutputCallback.WARNING:
            System.out.println("WARNING: " + toc.getMessage());
            break;
          default:
            throw new IOException("Unsupported message type: "
                + toc.getMessageType());
          }

        } else if (callbacks[i] instanceof NameCallback) {
          NameCallback nc = (NameCallback) callbacks[i];
          System.out.print(nc.getPrompt());
          nc.setName(reader.readLine().trim());
        } else if (callbacks[i] instanceof PasswordCallback) {
          PasswordCallback pc = (PasswordCallback) callbacks[i];
          System.out.print(pc.getPrompt());
          pc.setPassword(reader.readLine().trim().toCharArray());
        } else {
          throw new UnsupportedCallbackException(callbacks[i],
              "Unrecognized Callback");
        }
      }
    }

  }

}
