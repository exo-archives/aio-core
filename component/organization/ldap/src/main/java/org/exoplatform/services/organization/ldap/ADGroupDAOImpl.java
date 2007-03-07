/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.ldap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Group;

/**
 * Created by The eXo Platform SARL
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
 * Feb 22, 2006
 */
public class ADGroupDAOImpl extends GroupDAOImpl {
  
  private ADSearchBySID adSearch;
  
  public  ADGroupDAOImpl(
      LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService, ADSearchBySID ad) {
    super(ldapAttrMapping, ldapService) ;   
    adSearch = ad;
  }  
  
  public Collection findGroupByMembership(String userName, String membershipType) throws Exception{
    return findGroups(userName, membershipType); 
  }
  
  public Collection findGroupsOfUser(String userName) throws Exception {
     return findGroups(userName, null); 
  }
  
  private Collection findGroups(String userName, String type) throws Exception {
    LdapContext ctx = ldapService_.getLdapContext();
    List<Group> list = new ArrayList<Group>();  
    String userDN = getDNFromUsername(userName);
    if(userDN == null) return list;
    
    String filter = ldapAttrMapping_.userObjectClassFilter;
    String retAttrs[] = {"tokenGroups"};
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.OBJECT_SCOPE);
    constraints.setReturningAttributes(retAttrs);
    
    NamingEnumeration<SearchResult> results = ctx.search(userDN, filter, constraints);
    while (results.hasMore()) {
      SearchResult sr =  results.next();
      Attributes attrs = sr.getAttributes();      
      Attribute attr = attrs.get("tokenGroups");
      for (int x = 0; x < attr.size(); x++){
        byte[] SID = (byte[])attr.get(x);
        String membershipDN = adSearch.findMembershipDNBySID(SID, ldapAttrMapping_.groupsURL, type);
        if (membershipDN != null){
          Group group = getGroupFromMembershipDN(membershipDN);
          if (group != null && !checkExist(group, list))  list.add(group);            
        }
      }
    }    
    return list;
  } 
  public boolean checkExist(Group group, List<Group> list){
    for(Group ele : list){
      if(ele.getId().equals(group.getId())) return true;
    }    
    return false;
  }
  
}
