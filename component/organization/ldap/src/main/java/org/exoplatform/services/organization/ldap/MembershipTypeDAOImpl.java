/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
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
 * Created by The eXo Platform SARL
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