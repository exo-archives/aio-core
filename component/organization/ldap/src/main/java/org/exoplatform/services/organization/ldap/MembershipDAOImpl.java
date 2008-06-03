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
import java.util.List;

import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.commons.logging.Log;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipEventListener;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.impl.MembershipImpl;
/**
 * Created by The eXo Platform SAS
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 14, 2005
 */
public class MembershipDAOImpl extends BaseDAO implements MembershipHandler {
  
  private static Log log = ExoLogger.getLogger("core.MembershipDAOImpl");
  
  protected List<MembershipEventListener> listeners_;
  
  public MembershipDAOImpl(LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService) {
    super(ldapAttrMapping, ldapService) ;
    listeners_ = new ArrayList<MembershipEventListener>(3);
  }
  
  public void addMembershipEventListener(MembershipEventListener listener) {
    listeners_.add(listener);
  }
  
  final public Membership createMembershipInstance() {  return new MembershipImpl(); }
  
  public void createMembership(Membership m, boolean broadcast) throws Exception {    
    LdapContext ctx = ldapService_.getLdapContext();    
    String userDN = getDNFromUsername(m.getUserName());
    String groupDN = getGroupDNFromGroupId( m.getGroupId());    
    String membershipDN = 
      ldapAttrMapping_.membershipTypeNameAttr + "=" + m.getMembershipType() + "," + groupDN;
    
    Attributes attrs = null;
    try{
      attrs = ctx.getAttributes(membershipDN);
    }catch(Exception exp){      
    }
    if (attrs == null) {  
      if(broadcast) preSave(m, true);      
      ctx.createSubcontext(membershipDN, ldapAttrMapping_.membershipToAttributes(m, userDN));     
      postSave(m, true);
      return;
    }
    List members = getAttributes (attrs, ldapAttrMapping_.membershipTypeMemberValue);            
    if (members.contains(userDN)) return;
    ModificationItem[] mods = new ModificationItem[1];
    mods[0] = new ModificationItem( DirContext.ADD_ATTRIBUTE,
        new BasicAttribute(ldapAttrMapping_.membershipTypeMemberValue, userDN));          
    try{
      preSave(m, true);   
      ctx.modifyAttributes(membershipDN, mods);
      postSave(m, true);     
    }catch(Exception exp){  
      removeMembership(m.getGroupId(), true);
    }    
  }
  
  public void linkMembership(User user, Group group, MembershipType mt, boolean broadcast) throws Exception{
    if( mt == null || group == null) return;
    MembershipImpl membership = new MembershipImpl();
    membership.setMembershipType(mt.getName()) ;    
    membership.setUserName(user.getUserName());
    membership.setGroupId(group.getId());
    createMembership(membership, broadcast);
  }
  
  public Membership removeMembership(String id, boolean broadcast) throws Exception {    
    MembershipImpl m = new MembershipImpl();
    
    String membershipParts[] = id.split(",");
    if( membershipParts.length < 3) return null;
    String username = membershipParts[0];
    String membershipType = membershipParts[1];
    String groupId = membershipParts[2];
    
    m.setGroupId(groupId);
    m.setId(id);
    m.setMembershipType(membershipType);
    m.setUserName(username);
    
    String userDN =  getDNFromUsername( username).trim();     
    String groupDN = getGroupDNFromGroupId(groupId);
    String membershipDN = ldapAttrMapping_.membershipTypeNameAttr + "=" + membershipType + ", " + groupDN;   
    try {
      LdapContext ctx = ldapService_.getLdapContext();    
      NameParser parser = ctx.getNameParser("");
      Name dn = parser.parse(membershipDN);
      Attributes attrs = ctx.getAttributes(dn);
      if (attrs == null) return m;
      // Group does exist, is userDN in it?      
      List<Object> members = 
        this.getAttributes(attrs, ldapAttrMapping_.membershipTypeMemberValue);
      boolean remove = false;
      for( int i=0; i<members.size(); i++){
        if( String.valueOf( members.get( i)).trim().equalsIgnoreCase( userDN)) {
          remove = true;
          break;
        }
      }
      if (!remove) return m;     
      if( members.size() > 1) {
        ModificationItem[] mods = new ModificationItem[1];
        mods[0] = new ModificationItem( DirContext.REMOVE_ATTRIBUTE,
            new BasicAttribute( ldapAttrMapping_.membershipTypeMemberValue, userDN));
        if ( broadcast) preSave( m, true);       
        ctx.modifyAttributes( membershipDN, mods);
        if ( broadcast)  postSave( m, true);    
      } else{
        if ( broadcast) preDelete(m);        
        ctx.destroySubcontext(membershipDN);
        if ( broadcast)  postDelete(m);
      }
    } catch (NameNotFoundException e){
      e.printStackTrace();
    }       
    return m;
  }  
  
  public Collection removeMembershipByUser(String username, boolean broadcast) throws Exception {
    String userDN = getDNFromUsername(username);    
    String filter =  ldapAttrMapping_.membershipTypeMemberValue + "=" +escapeDN(userDN);    
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
    LdapContext ctx = ldapService_.getLdapContext();
    NamingEnumeration<SearchResult> results = ctx.search( ldapAttrMapping_.groupsURL, filter, constraints);    
    while(results.hasMore()) {
      SearchResult sr = results.next();       
      try{
        Attributes attrs = sr.getAttributes();   
        if( attrs.get(ldapAttrMapping_.membershipTypeMemberValue).size() > 1){          
          ModificationItem[] mods = new ModificationItem[1];
          mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
              new BasicAttribute(ldapAttrMapping_.membershipTypeMemberValue, userDN));
          ctx.modifyAttributes(sr.getNameInNamespace(), mods); 
        }else ctx.destroySubcontext(sr.getNameInNamespace());        
      }catch( Exception exp){
        exp.printStackTrace();
      }
    }
    return new ArrayList();
  }
  
  public Membership findMembership(String id) throws Exception {
    String membershipParts[] = id.split(",");
    Membership membership = 
      findMembershipByUserGroupAndType(membershipParts[0], membershipParts[2], membershipParts[1]);
    return membership;
  }
  
  public Membership findMembershipByUserGroupAndType(String userName, String groupId, String type)
      throws Exception {
    Membership membership = null;

    // check if user exists
    String userDN = getDNFromUsername(userName);
    if (userDN == null)
      return null;
    userDN = userDN.trim();

    String filter = "(&" + ldapAttrMapping_.membershipObjectClassFilter + "("
        + ldapAttrMapping_.membershipTypeNameAttr + "=" + type + "))";

    // retrieve memberships
    NamingEnumeration<SearchResult> results = findMembershipsInGroup(groupId, filter);
    if (results == null)
      return null;

    // add memberships matching user
    if (results.hasMore()) {
      SearchResult sr = results.next();
      if (haveUser(sr.getAttributes(), userDN)) {
        //String type = explodeDN(sr.getNameInNamespace(), true)[0];
        membership = createObject(userName, groupId, type);
      }
    }

    return membership;
  }
  
  public Collection findMembershipsByUserAndGroup(String userName, String groupId) throws Exception {
    ArrayList<Membership> memberships = new ArrayList<Membership>();

    // check if user exists
    String userDN = getDNFromUsername(userName);
    if (userDN == null)
      return memberships;
    userDN = userDN.trim();

    // retrieve memberships
    NamingEnumeration<SearchResult> results = findMembershipsInGroup(groupId,
        ldapAttrMapping_.membershipObjectClassFilter);
    if (results == null)
      return memberships;

    // add memberships matching user
    while (results.hasMore()) {
      SearchResult sr = results.next();
      if (haveUser(sr.getAttributes(), userDN)) {
        String type = explodeDN(sr.getNameInNamespace(), true)[0];
        Membership membership = createObject(userName, groupId, type);
        memberships.add(membership);
      }
    }
    return memberships;
  }

  /**
   * List memberships of a group by applying the membershipObjectFilter
   * 
   * @param groupId id of the group to retrieve
   * @param filter filter to apply to search
   * @return search results or null if failed
   * @throws Exception
   */
  private NamingEnumeration<SearchResult> findMembershipsInGroup(String groupId, String filter) throws Exception {
    NamingEnumeration<SearchResult> results = null;
    LdapContext ctx = ldapService_.getLdapContext();
    try {
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
      String groupDN = getGroupDNFromGroupId(groupId);
      results = ctx.search(groupDN, filter, constraints);
    } catch (Exception exp) {
      if (log.isWarnEnabled())
        log.warn("Failed to retrieve memberships for " + groupId + ": " + exp.getMessage());
    }
    return results;
  }
  
  public Collection findMembershipsByUser( String userName) throws Exception {
    ArrayList<Membership> memberships = new ArrayList<Membership>();

    // check if user exists
    String userDN = getDNFromUsername(userName);
    if (userDN == null)
      return memberships;
    userDN = userDN.trim();

    NamingEnumeration<SearchResult> results = null;
    LdapContext ctx = ldapService_.getLdapContext();
    try {
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
      // TODO : Need to optimize! Retrieving ALL memberships!
      String filter = ldapAttrMapping_.membershipObjectClassFilter;
      results = ctx.search(ldapAttrMapping_.groupsURL, filter, constraints);
    } catch (Exception exp) {
      if (log.isWarnEnabled())
        log.warn("Failed to retrieve memberships for user " + userName + ": " + exp.getMessage());
    }

    // add memberships matching user
    while (results.hasMore()) {
      SearchResult sr = results.next();
      if (haveUser(sr.getAttributes(), userDN)) {
        String membershipDN = sr.getNameInNamespace();
        Group group = getGroupFromMembershipDN(membershipDN);
        String type = explodeDN(membershipDN, true)[0];        
        Membership membership = createObject(userName, group.getId(), type);
        memberships.add(membership);
      }
    }
    return memberships;
    
  }
  
  public Collection findMembershipsByGroup(Group group) throws Exception {
    ArrayList<Membership> memberships = new ArrayList<Membership>();
    LdapContext ctx = ldapService_.getLdapContext();   
    String groupDN = this.getGroupDNFromGroupId(group.getId());  
    
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope( SearchControls.ONELEVEL_SCOPE);
    NamingEnumeration<SearchResult>  results = null;  
    if (log.isDebugEnabled()) log.debug("Searching memberships of group " + group.getId() + ": ");
    try{       
      results = ctx.search(groupDN, ldapAttrMapping_.membershipObjectClassFilter, constraints);
    }catch( Exception exp){ 
      return memberships;    
    }    
    
    if( results == null || !results.hasMoreElements()) return memberships;
    
    while( results.hasMoreElements()) {
      SearchResult sr = results.next();
      String membershipType = explodeDN(sr.getNameInNamespace(), true)[0];      
      Attributes attrs =  sr.getAttributes();
      Attribute attr = attrs.get(ldapAttrMapping_.membershipTypeMemberValue);
      String userName;
      for( int i=0; i<attr.size(); i++){
    	String userDN = String.valueOf( attr.get(i));
        
        if (ldapAttrMapping_.userDNKey.equals(ldapAttrMapping_.userUsernameAttr)) {
        	userName = explodeDN(userDN, true)[0];
        }         
        else {
        	userName = findUserByDN(userDN, ctx).getUserName();        	
        }
        Membership membership = createObject( userName, group.getId(), membershipType);
        if (log.isDebugEnabled()) log.debug("  found " + membership.toString());
        memberships.add(membership);
      }
    }
    return memberships;
  }
  
  
  private MembershipImpl createObject(String userName, String groupId, String type) throws Exception{
    MembershipImpl membership = new MembershipImpl();
    membership.setGroupId( groupId);
    membership.setUserName( userName);
    membership.setMembershipType( type);
    membership.setId( userName + "," + type + "," + groupId);
    return membership;
  }
  
  private void postDelete(Membership membership) throws Exception {   
    for( MembershipEventListener listener : listeners_ ) listener.postDelete(membership) ;  
  }
  
  private void preDelete(Membership membership) throws Exception {   
    for( MembershipEventListener listener : listeners_ ) listener.preDelete(membership) ;  
  }
  
  private void postSave(Membership membership, boolean isNew) throws Exception { 
    for( MembershipEventListener listener : listeners_ ) listener.postSave(membership, isNew) ;  
  }
  
  private void preSave(Membership membership, boolean isNew) throws Exception { 
    for( MembershipEventListener listener : listeners_ ) listener.preSave(membership, isNew) ;  
  }
}
