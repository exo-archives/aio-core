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

import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.impl.MembershipImpl;

/**
 * Created by The eXo Platform SAS Author : James Chamberlain
 * james.chamberlain@gmail.com Feb 22, 2006
 */
public class ADMembershipDAOImpl extends MembershipDAOImpl {

  private ADSearchBySID adSearch;

  public ADMembershipDAOImpl(LDAPAttributeMapping ldapAttrMapping,
                             LDAPService ldapService,
                             ADSearchBySID ad) throws Exception {
    super(ldapAttrMapping, ldapService);
    adSearch = ad;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Membership findMembershipByUserGroupAndType(String userName, String groupId, String type) throws Exception {
    LdapContext ctx = getLdapContext(true);
    String groupDN = getGroupDNFromGroupId(groupId);
    try {
      for (int err = 0;; err++) {
        try {
          Collection memberships = findMemberships(ctx, userName, groupDN, type);
          if (memberships.size() > 0)
            return (MembershipImpl) memberships.iterator().next();
          return null;
        } catch (NamingException e) {
          if (isConnectionError(e) && err < getMaxConnectionError())
            ctx = getLdapContext(true);
          else
            throw e;
        }
      }
    } finally {
      release(ctx);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Collection findMembershipsByUser(String userName) throws Exception {
    LdapContext ctx = getLdapContext(true);
    try {
      for (int err = 0;; err++) {
        try {
          return findMemberships(ctx, userName, ldapAttrMapping.groupsURL, null);
        } catch (NamingException e) {
          if (isConnectionError(e) && err < getMaxConnectionError())
            ctx = getLdapContext(true);
          else
            throw e;
        }
      }
    } finally {
      release(ctx);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Collection findMembershipsByUserAndGroup(String userName, String groupId) throws Exception {
    String groupDN = getGroupDNFromGroupId(groupId);
    LdapContext ctx = getLdapContext(true);
    try {
      for (int err = 0;; err++) {
        try {
          return findMemberships(ctx, userName, groupDN, null);
        } catch (NamingException e) {
          if (isConnectionError(e) && err < getMaxConnectionError())
            ctx = getLdapContext(true);
          else
            throw e;
        }
      }
    } finally {
      release(ctx);
    }
  }

  @SuppressWarnings("unchecked")
  private Collection findMemberships(LdapContext ctx, String userName, String groupId, String type) throws Exception {
    Collection<Membership> list = new ArrayList<Membership>();
    String userDN = getDNFromUsername(ctx, userName);
    if (userDN == null)
      return list;

    String filter = ldapAttrMapping.userObjectClassFilter;
    String retAttrs[] = { "tokenGroups" };
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.OBJECT_SCOPE);
    constraints.setReturningAttributes(retAttrs);

    NamingEnumeration<SearchResult> results = ctx.search(userDN, filter, constraints);
    while (results.hasMore()) {
      SearchResult sr = (SearchResult) results.next();
      Attributes attrs = sr.getAttributes();
      Attribute attr = attrs.get("tokenGroups");
      for (int x = 0; x < attr.size(); x++) {
        byte[] SID = (byte[]) attr.get(x);
//        String membershipDN = adSearch.findMembershipDNBySID(SID, groupId, type);
        String membershipDN = adSearch.findMembershipDNBySID(ctx, SID, groupId, type);
        if (membershipDN != null)
          list.add(createMembershipObject(ctx, membershipDN, userName, type));
      }
    }
    return list;
  }

  /**
   * Create {@link Membership} instance
   * 
   * @param userName user name
   * @param groupId group ID
   * @param type membership type
   * @return newly created instance of {@link Membership}
   */
  private Membership createMembershipObject(LdapContext ctx, String dn, String user, String type) throws Exception {
    Group group = getGroupFromMembershipDN(ctx, dn);
    if (type == null)
      type = explodeDN(dn, true)[0];
    MembershipImpl membership = new MembershipImpl();
    membership.setId(user + "," + type + "," + group.getId());
    membership.setUserName(user);
    membership.setMembershipType(type);
    membership.setGroupId(group.getId());
    return membership;
  }
}
