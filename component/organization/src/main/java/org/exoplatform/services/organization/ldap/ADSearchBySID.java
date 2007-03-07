/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
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
 * Created by The eXo Platform SARL
 * Author : Thuannd
 *         nhudinhthuan@yahoo.com
 * Feb 22, 2006
 */
public class ADSearchBySID {
  
  protected LDAPAttributeMapping ldapAttrMapping_ ; 
  
  protected LDAPService ldapService_ ; 
  
  public ADSearchBySID(LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService) {
    ldapAttrMapping_ = ldapAttrMapping;
    ldapService_ = ldapService;
  }
  
  public String findMembershipDNBySID(byte[] sid, String baseDN, String scopedRole) throws Exception {
    LdapContext ctx = ldapService_.getLdapContext();    
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
    constraints.setReturningAttributes(new String[]{""});
    constraints.setDerefLinkFlag(true); 
    
    NamingEnumeration answer;
    if (scopedRole == null){
      answer = ctx.search(baseDN, "objectSid={0}", new Object[] {sid}, constraints);
    } else {
      answer =ctx.search(baseDN, "(& (objectSid={0}) ("+ 
         ldapAttrMapping_.membershipTypeRoleNameAttr+ "={1}))",new Object[] {sid, scopedRole}, constraints);
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
