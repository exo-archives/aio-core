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
                             ADSearchBySID ad) {
    super(ldapAttrMapping, ldapService);
    adSearch = ad;
  }

  public Membership findMembershipByUserGroupAndType(String userName, String groupId, String type) throws Exception {
    String groupDN = getGroupDNFromGroupId(groupId);
    ArrayList memberships = (ArrayList) findMemberships(userName, groupDN, type);
    if (memberships.size() > 0)
      return (MembershipImpl) memberships.get(0);
    return null;
  }

  public Collection findMembershipsByUser(String userName) throws Exception {
    ArrayList<Membership> list = (ArrayList<Membership>) findMemberships(userName,
                                                                         ldapAttrMapping_.groupsURL,
                                                                         null);
    return list;
  }

  public Collection findMembershipsByUserAndGroup(String userName, String groupId) throws Exception {
    String groupDN = getGroupDNFromGroupId(groupId);
    return (ArrayList) findMemberships(userName, groupDN, null);
  }

  private Collection findMemberships(String userName, String groupId, String type) throws Exception {
    LdapContext ctx = ldapService_.getLdapContext();
    Collection<Membership> list = new ArrayList<Membership>();
    String userDN = getDNFromUsername(userName);
    if (userDN == null)
      return list;

    String filter = ldapAttrMapping_.userObjectClassFilter;
    String retAttrs[] = { "tokenGroups" };
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.OBJECT_SCOPE);
    constraints.setReturningAttributes(retAttrs);

    NamingEnumeration results = ctx.search(userDN, filter, constraints);
    while (results.hasMore()) {
      SearchResult sr = (SearchResult) results.next();
      Attributes attrs = sr.getAttributes();
      Attribute attr = attrs.get("tokenGroups");
      for (int x = 0; x < attr.size(); x++) {
        byte[] SID = (byte[]) attr.get(x);
        String membershipDN = adSearch.findMembershipDNBySID(SID, groupId, type);
        if (membershipDN != null)
          list.add(createMembershipObject(membershipDN, userName, type));
      }
    }
    return list;
  }

  private Membership createMembershipObject(String dn, String user, String type) throws Exception {
    Group group = getGroupFromMembershipDN(dn);
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
