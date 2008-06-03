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

import java.util.Collection;
import java.util.HashSet;

import javax.security.auth.login.LoginException;

/**
 * Created by The eXo Platform SAS        .
 * @author Gennady Azarenkov
 * @version $Id:$
 */

public class DummyAuthenticatorImpl implements Authenticator {

  private String[] acceptableUIDs = { "exo" };

  public DummyAuthenticatorImpl() {

  }

  public Identity authenticate(Credential[] credentials) throws LoginException,
      Exception {

      String myID = ((UsernameCredential) credentials[0]).getUsername();
      Collection<MembershipEntry> entries = new HashSet<MembershipEntry>();
      for (String id : this.acceptableUIDs)
        if (id.equals(myID)) {
          entries.add(new MembershipEntry("exo"));
          //identity.setMemberships(entries);
        } else {
          throw new LoginException();
        }
      return new Identity(myID, entries);
  }


}
