/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
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

import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipEventListener;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.impl.MembershipImpl;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 14, 2005
 */
public class MembershipDAOImpl extends BaseDAO implements MembershipHandler {
  
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
  
  public Membership findMembershipByUserGroupAndType(String userName, String groupId, String type) throws Exception {    
    String membershipDN = 
      ldapAttrMapping_.membershipTypeNameAttr + "=" + type + "," + getGroupDNFromGroupId(groupId);     
    try {
      LdapContext ctx = ldapService_.getLdapContext();      
      Attributes attrs = ctx.getAttributes( membershipDN);      
      if (attrs == null) return null; 
      if(!haveUser( attrs, getDNFromUsername( userName).trim())) return null; 
      return createObject( userName, groupId, type);
    } catch (NameNotFoundException e){        
    }    
    return null;   
  }
  
  public Collection findMembershipsByUserAndGroup(String userName, String groupId) throws Exception {
    ArrayList<Membership> memberships = new ArrayList<Membership>();  
    String userDN = getDNFromUsername(userName);    
    if(userDN == null ) return memberships;
    userDN = userDN.trim();
    NamingEnumeration<SearchResult>  results = null;       
    LdapContext ctx = ldapService_.getLdapContext();   
    try{  
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope( SearchControls.ONELEVEL_SCOPE);
      results = ctx.search( getGroupDNFromGroupId( groupId), "objectClass=groupOfNames",constraints);
    }catch( Exception exp){        
    }
    
    if( results == null) return memberships;  
    while( results.hasMore()) {
      SearchResult sr = results.next();
      if(haveUser(sr.getAttributes(), userDN)){        
        memberships.add(createObject(userName, groupId, explodeDN(sr.getNameInNamespace(), true)[0]));
      }
    }
    return memberships;
  }
  
  public Collection findMembershipsByUser( String userName) throws Exception {   
    Collection<Membership> memberships = new ArrayList<Membership>();
    
    String userDN = this.getDNFromUsername(userName);
    if( userDN == null) return memberships;    
    String filter = ldapAttrMapping_.membershipTypeMemberValue + "=" + escapeDN( userDN);  
    
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
    LdapContext ctx = ldapService_.getLdapContext();  
    NamingEnumeration<SearchResult> results = null;
    try{
      results = ctx.search( ldapAttrMapping_.groupsURL, filter, constraints);
    }catch( Exception exp){
      return memberships;
    }
    
    if( results == null) return memberships;
    while(results.hasMore()) {
      SearchResult sr = results.next();
      String membershipDN = sr.getNameInNamespace();
      Group group = getGroupFromMembershipDN(membershipDN);
      String type = explodeDN(membershipDN, true)[0];
      memberships.add( this.createObject( userName, group.getId(), type));
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
    
    try{       
      results = ctx.search(groupDN, ldapAttrMapping_.membershipObjectClassFilter, constraints);
    }catch( Exception exp){ 
      return memberships;    
    }    
    if( results == null || !results.hasMoreElements()) return memberships;    
    while( results.hasMoreElements()) {
      SearchResult sr = results.next();
      NameParser parser = ctx.getNameParser("");
      String type = explodeDN(sr.getNameInNamespace(), true)[0];      
      Attributes attrs =  sr.getAttributes();
//      Attribute attr = attrs.get("member");
      Attribute attr = attrs.get(ldapAttrMapping_.membershipTypeMemberValue);
      String userName;
      for( int i=0; i<attr.size(); i++){
        userName = explodeDN(String.valueOf( attr.get(i)), true)[0];       
        memberships.add(createObject( userName, group.getId(), type));
      }
    }
    return memberships;
  }
  
  private boolean haveUser( Attributes attrs, String userDN) throws Exception {
    if (attrs == null) return false;    
    List<Object> members = this.getAttributes(attrs, ldapAttrMapping_.membershipTypeMemberValue);
    for( int i=0; i<members.size(); i++){        
      if( String.valueOf( members.get( i)).trim().equalsIgnoreCase( userDN)) return true;      
    }      
    return false;
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
