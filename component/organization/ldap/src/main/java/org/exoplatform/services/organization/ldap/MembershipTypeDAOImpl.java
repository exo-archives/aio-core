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
import java.util.Date;

import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.MembershipTypeHandler;
import org.exoplatform.services.organization.impl.MembershipTypeImpl;
/**
 * Created by The eXo Platform SAS
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 14, 2005
 */
public class MembershipTypeDAOImpl extends BaseDAO implements MembershipTypeHandler  {
    
  public MembershipTypeDAOImpl(LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService) {
    super(ldapAttrMapping, ldapService) ;
  }
  
  final public MembershipType createMembershipTypeInstance() {
    return new MembershipTypeImpl() ;
  } 

  public MembershipType createMembershipType(MembershipType mt, boolean broadcast) throws Exception {    
    LdapContext ctx = ldapService_.getLdapContext();    
    String membershipTypeDN = ldapAttrMapping_.membershipTypeNameAttr
                              + "=" + mt.getName() + "," + ldapAttrMapping_.membershipTypeURL;    
    try {
      ctx.getAttributes(membershipTypeDN);
    } catch (NameNotFoundException e){     
      Date now = new Date();
      mt.setCreatedDate(now);
      mt.setModifiedDate(now);
      ctx.createSubcontext(membershipTypeDN, ldapAttrMapping_.membershipTypeToAttributes(mt));    
    }
    return mt;
  }

  public MembershipType saveMembershipType(MembershipType mt, boolean broadcast) throws Exception {
    LdapContext ctx  = ldapService_.getLdapContext();
    String membershipTypeDN = ldapAttrMapping_.membershipTypeNameAttr
                + "=" + mt.getName() + "," + ldapAttrMapping_.membershipTypeURL;
    Attributes attrs = ctx.getAttributes(membershipTypeDN);
    if (attrs == null) return mt;
    ModificationItem[] mods = new ModificationItem[1];
    String desc = mt.getDescription();
    // TODO: http://jira.exoplatform.org/browse/COR-49
    if (desc != null && desc.length() > 0){
      mods[0] = new ModificationItem(
          DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("description" , mt.getDescription()));
    } else {
      mods[0] = new ModificationItem(
          DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("description" , mt.getDescription()));
    }
    ctx.modifyAttributes(membershipTypeDN, mods);    
    return mt;
  }

  public MembershipType removeMembershipType(String name, boolean broadcast) throws Exception {    
    MembershipType m = null;
    LdapContext ctx = ldapService_.getLdapContext();
    String membershipTypeDN = ldapAttrMapping_.membershipTypeNameAttr
                              + "=" + name + "," + ldapAttrMapping_.membershipTypeURL;  
    try {
      Attributes attrs = ctx.getAttributes(membershipTypeDN);
      if (attrs == null) return m;
      m = ldapAttrMapping_.attributesToMembershipType( attrs);
      removeMembership( name);
      ctx.destroySubcontext(membershipTypeDN);      
    } catch (NameNotFoundException e){      
    }
    return m;
  }
  
  private void removeMembership(String name) throws Exception{
    LdapContext ctx = ldapService_.getLdapContext();
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
    String filter = ldapAttrMapping_.membershipTypeNameAttr + "=" + name; 
    NamingEnumeration<SearchResult> results 
             = ctx.search( ldapAttrMapping_.groupsURL, filter, constraints); 
    
    while (results.hasMore()) {
      SearchResult sr = results.next();     
      ctx.destroySubcontext(sr.getNameInNamespace());
    }
  }

  public MembershipType findMembershipType(String name) throws Exception {
    MembershipType membershipType = null;    
    LdapContext ctx = ldapService_.getLdapContext();
    String membershipTypeDN = ldapAttrMapping_.membershipTypeNameAttr
                                + "=" + name + "," + ldapAttrMapping_.membershipTypeURL;
    try {      
      Attributes attrs = ctx.getAttributes(membershipTypeDN);
      if (attrs == null) return membershipType;
      membershipType = ldapAttrMapping_.attributesToMembershipType(attrs);      
    } catch(NameNotFoundException e){        
    }
    return membershipType;
  }

  public Collection findMembershipTypes() throws Exception {
    LdapContext ctx = ldapService_.getLdapContext();
    Collection<MembershipType> memberships = new ArrayList<MembershipType>();    
    String filter = ldapAttrMapping_.membershipTypeNameAttr + "=*";
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
    NamingEnumeration<SearchResult> results = ctx.search( ldapAttrMapping_.membershipTypeURL, filter, constraints);   
    while (results.hasMore()) {
      SearchResult sr = results.next();
      Attributes attrs = sr.getAttributes();     
      memberships.add( ldapAttrMapping_.attributesToMembershipType( attrs));
    }
    return memberships;
  }
}