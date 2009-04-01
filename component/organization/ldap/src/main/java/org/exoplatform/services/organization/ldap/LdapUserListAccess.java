/**
 * Copyright (C) 2003-2008 eXo Platform SAS.
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

package org.exoplatform.services.organization.ldap;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.User;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public abstract class LdapUserListAccess implements ListAccess<User> {

  /**
   * Base search DN.
   */
  protected String               searchBase;

  /**
   * Search filter.
   */
  protected String               filter;

  /**
   * LDAP attribute to organization service essences.
   */
  protected LDAPAttributeMapping ldapAttrMapping;

  /**
   * LDAP service.
   */
  protected LDAPService          ldapService;

  /**
   * @param ldapAttrMapping LDAP attribute to organization service essences 
   * @param ldapService LDAP service
   * @param searchBase base search DN
   * @param filter search filter
   */
  public LdapUserListAccess(LDAPAttributeMapping ldapAttrMapping,
                            LDAPService ldapService,
                            String searchBase,
                            String filter) {
    this.ldapService = ldapService;
    this.ldapAttrMapping = ldapAttrMapping;
    this.searchBase = searchBase;
    this.filter = filter;
  }

  /**
   * {@inheritDoc}
   */
  public int getSize() throws Exception {
    LdapContext ctx = ldapService.getLdapContext();
    try {
      for (int err = 0;; err++) {
        try {
          return getSize(ctx);
        } catch (NamingException e) {
          if (BaseDAO.isConnectionError(e) && err < BaseDAO.getMaxConnectionError())
            ctx = ldapService.getLdapContext(true);
          else
            throw e;
        }
      }
    } finally {
      ldapService.release(ctx);
    }
  }

  /**
   * {@inheritDoc}
   */
  public User[] load(int index, int length) throws Exception, IllegalArgumentException {
    LdapContext ctx = ldapService.getLdapContext();
    try {
      for (int err = 0;; err++) {
        try {
          return load(ctx, index, length);
        } catch (NamingException e) {
          if (BaseDAO.isConnectionError(e) && err < BaseDAO.getMaxConnectionError())
            ctx = ldapService.getLdapContext(true);
          else
            throw e;
        }
      }
    } finally {
      ldapService.release(ctx);
    }
  }

  /**
   * @param ctx LDAP context
   * @param index offset 
   * @param length number of users
   * @return result array of users 
   * @throws Exception if any error occurs
   */
  protected abstract User[] load(LdapContext ctx, int index, int length) throws Exception;

  /**
   * @param ctx LDAP context
   * @return list size
   * @throws Exception if any error occurs
   */
  protected abstract int getSize(LdapContext ctx) throws Exception;

}
