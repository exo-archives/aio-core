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
package org.exoplatform.services.ldap;

import javax.naming.InitialContext;
import javax.naming.ldap.LdapContext;

/**
 * Created by the eXo platform team User: Daniel Summer Date: 25/5/2004
 * interface abstracted from JSDK
 */
public interface LDAPService {

  public static int DEFAULT_SERVER          = 0;

  public static int ACTIVE_DIRECTORY_SERVER = 1;

  // public static int OPEN_LDAP_SERVER = 2;
  // public static int NETSCAPE_SERVER = 3;
  // public static int REDHAT_SERVER = 4;

  // Normal context for all directories
  public LdapContext getLdapContext() throws Exception;

  // LDAP booster pack context for v3 directories (except Active Directory)
  public InitialContext getInitialContext() throws Exception;

  // LDAP bind authentication
  public boolean authenticate(String userDN, String password) throws Exception;

  public int getServerType();
}
