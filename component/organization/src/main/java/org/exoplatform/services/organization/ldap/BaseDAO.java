/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.ldap;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.impl.GroupImpl;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 14, 2005
 */
public class BaseDAO {
  
  protected LDAPAttributeMapping ldapAttrMapping_ ; 
  
  protected LDAPService ldapService_ ; 
  
  private NameParser parser;
  
  public  BaseDAO(LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService){
    ldapAttrMapping_ = ldapAttrMapping ;
    ldapService_ =  ldapService ;
  }
  
  protected String getGroupDNFromGroupId(String groupId) {   
    StringBuilder buffer = new StringBuilder();
    String groupParts[] = groupId.split("/");
    for (int x = (groupParts.length - 1); x > 0; x--) {
      buffer.append("ou=" + groupParts[x] + ", ");
    }   
    buffer.append(ldapAttrMapping_.groupsURL);    
    return buffer.toString();
  }
  
  protected List<Object> getAttributes(Attributes attributes, String attribute) {
    List<Object> results = new ArrayList<Object>();
    try {
      if (attributes == null)  return results;
      Attribute attr = attributes.get(attribute);
      for (int x = 0; x < attr.size(); x++)  results.add(attr.get(x));  
    } catch (Exception e) {
    }
    return results;
  }
  
  protected Group getGroupFromMembershipDN(String membershipDN) throws Exception {
    String membershipParts[] = explodeDN(membershipDN, false);    
    StringBuffer buffer = new StringBuffer();
    for (int x = 1; x < membershipParts.length; x++) {
      if (x == membershipParts.length - 1) {
        buffer.append(membershipParts[x]);
      } else {
        buffer.append(membershipParts[x] + ",");
      }
    }
    Group group = getGroupByDN(buffer.toString());    
    return group;
  }
  
  protected Group getGroupByDN(String groupDN) throws Exception {
    LdapContext ctx = ldapService_.getLdapContext();  
    StringBuffer idBuffer = new StringBuffer();
    String parentId = null;
    String baseParts[] = explodeDN(ldapAttrMapping_.groupsURL, true);    
    String membershipParts[] = explodeDN(groupDN, true);    
    for (int x = (membershipParts.length - baseParts.length - 1); x > -1; x--) {      
      idBuffer.append("/" + membershipParts[x]);
      if (x == 1) parentId = idBuffer.toString();      
    }    
    if (idBuffer == null) return null;
    Attributes attrs = ctx.getAttributes(groupDN);
    GroupImpl group = new GroupImpl();
    group.setGroupName( membershipParts[0]);
    group.setId(idBuffer.toString());
    group.setDescription( ldapAttrMapping_.getAttributeValueAsString(attrs, "description"));
    group.setLabel(ldapAttrMapping_.getAttributeValueAsString(attrs, "l"));  
    group.setParentId(parentId);
    return group;    
  }  
  
  protected String[] explodeDN(String nameDN, boolean removeTypes) throws Exception {
    if(parser == null)
      parser = ldapService_.getLdapContext().getNameParser("");  
    Name dn = parser.parse(nameDN);
    Enumeration<String> enumeration = dn.getAll();
    List<String> list = new ArrayList<String>();
    while(enumeration.hasMoreElements()){
      String ldap = enumeration.nextElement();
      if (removeTypes){
        int position = ldap.indexOf("=");
        String value = ldap.substring(position + 1);
        list.add(0, value);
      } else 
        list.add(0, ldap);
    }
    String explodedDN[] = new String[list.size()];
    list.toArray(explodedDN);
    return explodedDN;
  } 
  
  protected User getUserFromUsername(String username) throws Exception {   
    NamingEnumeration<SearchResult> answer =  findUser(username, true);
    while (answer.hasMoreElements()){
      String userDN = answer.next().getNameInNamespace();  
      return ldapAttrMapping_.attributesToUser(ldapService_.getLdapContext().getAttributes(userDN));
    }
    return null;
  }
  
  protected String getDNFromUsername(String username) throws Exception { 
    try{
//        String userDN = "CN="+username+","+ldapAttrMapping_.userURL;
        String userDN = ldapAttrMapping_.userDNKey + "="+username+","+ldapAttrMapping_.userURL;
      Object obj =ldapService_.getLdapContext().lookup(userDN);
      if(obj != null) return userDN;      
    }catch(Exception exp){}
    NamingEnumeration<SearchResult> answer =  findUser(username, false);    
    while (answer.hasMoreElements()) return answer.next().getNameInNamespace();   
    return null;
  }
  
  private NamingEnumeration<SearchResult>findUser(String username, boolean hasAttribute)throws Exception{
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);   
    if(!hasAttribute) {
      constraints.setReturningAttributes(new String[]{""});
      constraints.setDerefLinkFlag(true);
    }
    String filter = "(&("+ldapAttrMapping_.userUsernameAttr + "=" + username+")" ;
    filter += "("+ldapAttrMapping_.userObjectClassFilter+"))";        
    return ldapService_.getLdapContext().search(ldapAttrMapping_.baseURL, filter, constraints); 
  }
  
  
  protected void removeAllSubtree(LdapContext context, String dn) throws Exception{           
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope( SearchControls.ONELEVEL_SCOPE);      
    NamingEnumeration<SearchResult> results = context.search(dn, "(objectclass=*)",  constraints);
    while( results.hasMore()){
      SearchResult sr =  results.next();        
      removeAllSubtree( context, sr.getNameInNamespace());
    }      
    context.destroySubcontext( dn);    
  }
  
  public String escapeDN(String dn) {
    if(dn == null) return dn;   
    StringBuilder buf = new StringBuilder(dn.length());
    for (int i = 0; i < dn.length(); i++) {
      char c = dn.charAt(i);
      switch (c) {
      case '\\':
        buf.append("\\5c");
        break;
      case '*':
        buf.append("\\2a");
        break;
      case '(':
        buf.append("\\28");
        break;
      case ')':
        buf.append("\\29");
        break;
      case '\0':
        buf.append("\\00");
        break;
      default:
        buf.append(c);
      break;
      }
    }
    return buf.toString();
  }
  
}
