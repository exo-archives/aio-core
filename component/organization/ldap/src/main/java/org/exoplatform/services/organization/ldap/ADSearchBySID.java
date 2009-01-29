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
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.exoplatform.services.ldap.LDAPService;

/**
 * Created by The eXo Platform SAS Author : Thuannd nhudinhthuan@yahoo.com Feb
 * 22, 2006
 */
public class ADSearchBySID {

  protected LDAPAttributeMapping ldapAttrMapping_;

  protected LDAPService          ldapService_;

  public ADSearchBySID(LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService) {
    ldapAttrMapping_ = ldapAttrMapping;
    ldapService_ = ldapService;
  }

  public String findMembershipDNBySID(byte[] sid, String baseDN, String scopedRole) throws Exception {
    LdapContext ctx = ldapService_.getLdapContext();
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
    constraints.setReturningAttributes(new String[] { "" });
    constraints.setDerefLinkFlag(true);

    NamingEnumeration answer;
    if (scopedRole == null) {
      answer = ctx.search(baseDN, "objectSid={0}", new Object[] { sid }, constraints);
    } else {
      answer = ctx.search(baseDN,
                          "(& (objectSid={0}) (" + ldapAttrMapping_.membershipTypeRoleNameAttr
                              + "={1}))",
                          new Object[] { sid, scopedRole },
                          constraints);
    }

    while (answer.hasMoreElements()) {
      SearchResult sr = (SearchResult) answer.next();
      NameParser parser = ctx.getNameParser("");
      Name entryName = parser.parse(new CompositeName(sr.getName()).get(0));
      return entryName + "," + baseDN;
    }
    return null;
  }
}
