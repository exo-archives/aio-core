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
package org.exoplatform.services.security.pam;

import org.exoplatform.services.security.Credential;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.PasswordCredential;
import org.exoplatform.services.security.UsernameCredential;

/**
 * Just for test JpamLoginModule!!!
 * 
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class JPamAuthenticatorTest {

  public static void main(String[] args) throws Exception {
    JPamAuthenticator auth = new JPamAuthenticator();
    Credential[] credentials = new Credential[] { new UsernameCredential(args[0]),
        new PasswordCredential(args[1]) };
    String userId = auth.validateUser(credentials);
    System.out.println("authentication ok");
    Identity identity = auth.createIdentity(userId);
    System.out.println(identity.getGroups());
  }

}
