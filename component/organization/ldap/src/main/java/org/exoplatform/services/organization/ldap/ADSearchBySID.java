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
package org.exoplatform.services.organization.ldap;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.commons.logging.Log;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS Author : Thuannd nhudinhthuan@yahoo.com Feb
 * 22, 2006
 */
public class ADSearchBySID {

  /**
   * Logger.
   */
  private static final Log       LOG = ExoLogger.getLogger(ADSearchBySID.class.getName());

  protected LDAPAttributeMapping ldapAttrMapping;

  /**
   * Instead {@link #findMembershipDNBySID(byte[], String, String)} use method
   * {@link #findMembershipDNBySID(LdapContext, byte[], String, String)}. In
   * this case {@link LDAPService} useless in here.
   */
  @Deprecated
  protected LDAPService          ldapService;

  /**
   * @param ldapAttrMapping attribute mapping
   * @param ldapService see {@link #ldapService}
   */
  @Deprecated
  public ADSearchBySID(LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService) {
    this.ldapAttrMapping = ldapAttrMapping;
    this.ldapService = ldapService;
  }

  public ADSearchBySID(LDAPAttributeMapping ldapAttrMapping) {
    this.ldapAttrMapping = ldapAttrMapping;
  }

  @Deprecated
  public String findMembershipDNBySID(byte[] sid, String baseDN, String scopedRole) throws Exception {
    LdapContext ctx = ldapService.getLdapContext();
    try {
      for (int err = 0;; err++) {
        try {
          return findMembershipDNBySID(ctx, sid, baseDN, scopedRole);
        } catch (NamingException e) {
          if (BaseDAO.isConnectionError(e) && err < BaseDAO.getMaxConnectionError())
            ctx = ldapService.newLdapContext();
          else
            throw e;
        }
      }
    } finally {
      try {
        ctx.close();
      } catch (NamingException e) {
        LOG.warn("Exception occur when try close LDAP context. ", e);
      }
      ctx = null;
    }
  }

  public String findMembershipDNBySID(LdapContext ctx, byte[] sid, String baseDN, String scopedRole) throws NamingException {
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
    constraints.setReturningAttributes(new String[] { "" });
    constraints.setDerefLinkFlag(true);

    NamingEnumeration<SearchResult> answer = null;
    if (scopedRole == null) {
      answer = ctx.search(baseDN, "objectSid={0}", new Object[] { sid }, constraints);
    } else {
      answer = ctx.search(baseDN,
                          "(& (objectSid={0}) (" + ldapAttrMapping.membershipTypeRoleNameAttr
                              + "={1}))",
                          new Object[] { sid, scopedRole },
                          constraints);
    }
    while (answer.hasMoreElements()) {
      SearchResult sr = answer.next();
      NameParser parser = ctx.getNameParser("");
      Name entryName = parser.parse(new CompositeName(sr.getName()).get(0));
      return entryName + "," + baseDN;
    }
    return null;
  }

}
