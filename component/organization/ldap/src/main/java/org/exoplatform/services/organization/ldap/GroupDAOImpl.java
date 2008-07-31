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

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
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
import org.exoplatform.services.organization.GroupEventListener;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.impl.GroupImpl;

/**
 * Created by The eXo Platform SAS
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 14, 2005
 */
public class GroupDAOImpl extends BaseDAO implements  GroupHandler {
  
  private static Log log = ExoLogger.getLogger("core.GroupDAOImpl");
  
  protected List<GroupEventListener> listeners_ ;
  
  public  GroupDAOImpl(LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService) {
    super(ldapAttrMapping, ldapService) ;    
    listeners_ = new ArrayList<GroupEventListener>(3) ;
  }
  
  public void addGroupEventListener(GroupEventListener listener) {
    listeners_.add(listener) ;  
  }
  
  final public Group createGroupInstance() {  return new GroupImpl(); }
  
  public void createGroup(Group group, boolean broadcast) throws Exception {
    addChild(null, group, broadcast);
  } 
  
  public void addChild(Group parent, Group child , boolean broadcast) throws Exception {    
    setId( parent, child);    
    String searchBase = createSubDN( parent);    
    String groupDN = "ou=" + child.getGroupName() + "," + searchBase;    
    String filter = "ou=" + child.getGroupName();    
    
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
    LdapContext ctx = ldapService_.getLdapContext();    
   
    NamingEnumeration results = ctx.search(searchBase, filter,  constraints);
      
    if (results.hasMore()) return;
    GroupImpl group = (GroupImpl) child;  
      
    if(broadcast) preSave(group, true);      
    ctx.createSubcontext(groupDN, ldapAttrMapping_.groupToAttributes(child));
    postSave(group, true);    
  }
  
  public void saveGroup(Group group, boolean broadcast) throws Exception {
    LdapContext ctx = ldapService_.getLdapContext();    
    Group parent = findGroupById(group.getParentId());    
    setId( parent, group);             
    String groupDN = "ou=" + group.getGroupName() + "," + createSubDN(parent);
    
    ArrayList<ModificationItem> modifications = new ArrayList<ModificationItem>();
    ModificationItem  mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, 
        new BasicAttribute(ldapAttrMapping_.ldapDescriptionAttr, group.getDescription()));
    modifications.add(mod);   
    
    mod = new ModificationItem(
        DirContext.REPLACE_ATTRIBUTE, new BasicAttribute( "l", group.getLabel()));
    modifications.add(mod);    
    
    ModificationItem[] mods = new ModificationItem[ modifications.size()];      
    modifications.toArray(mods);
    if(broadcast) preSave( group, true);
    NameParser parser = ctx.getNameParser("");
    Name name = parser.parse( groupDN);
    ctx.modifyAttributes(name, mods);
    if(broadcast) postSave( group, true); 
  }
  
  public Group removeGroup(Group group, boolean broadcast) throws Exception {
    LdapContext ctx = ldapService_.getLdapContext();   
    String filter = "ou=" + group.getGroupName();   
    String searchBase = this.createSubDN( group.getParentId());    
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
    NamingEnumeration<SearchResult> results = ctx.search(searchBase, filter,  constraints);
    
    if ( !results.hasMore())  return group;
    SearchResult sr = results.next();
    NameParser parser = ctx.getNameParser("");
    Name entryName = parser.parse(new CompositeName( sr.getName()).get(0));      
    String groupDN = entryName + "," + searchBase;
    
    group = getGroupByDN(groupDN);
    if (group == null) return group;
    if( broadcast) preDelete(group); 
    removeAllSubtree( ctx, groupDN);
    if( broadcast) postDelete(group);        
    return group;
  }  
  
  public Collection findGroupByMembership(String userName, String membershipType) throws Exception {
    List<Group> groups = new ArrayList<Group>();   
    LdapContext ctx = ldapService_.getLdapContext();
    String filter = "(&("+ldapAttrMapping_.membershipTypeMemberValue + "=" 
                  + getDNFromUsername(userName)+ ")("
                  + ldapAttrMapping_.membershipTypeRoleNameAttr+"="+ membershipType + "))"; 
    
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
    NamingEnumeration<SearchResult> results = ctx.search(ldapAttrMapping_.groupsURL, filter, constraints);     
    while ( results.hasMore()){
      SearchResult sr = results.next();
      NameParser parser = ctx.getNameParser("");    
      Name entryName = parser.parse( new CompositeName( sr.getName()).get(0));
      String entryName_ = String.valueOf( entryName).
      substring( entryName.getSuffix( 1).toString().length()+1);
      String groupDN = entryName_ + "," + ldapAttrMapping_.groupsURL;     
      Group group = getGroupByDN(groupDN);
      if (group != null) addGroup(groups, group); 
    }
    
  if (log.isDebugEnabled()) {
    log.debug("Retrieved " + groups.size() + " groups from ldap for user " + userName + " with membershiptype " + membershipType);
  }

    return groups;
  }
  
  public Group findGroupById(String groupId) throws Exception {   
    if(groupId == null) return null ;    
    String parentId_ = null;     
    LdapContext ctx = ldapService_.getLdapContext();
    StringBuffer buffer = new StringBuffer();
    String groupIdParts[] = groupId.split("/");
    for (int x = 1; x < groupIdParts.length; x++) {
      buffer.append("/" + groupIdParts[x]);
      if (x == (groupIdParts.length - 2)) parentId_ = buffer.toString();     
    }             
    String groupDN = getGroupDNFromGroupId(groupId);   
    
    try {          
      Attributes attrs = ctx.getAttributes( groupDN);     
      Group group = ldapAttrMapping_.attributesToGroup( attrs);
      ((GroupImpl)group).setId(groupId);
      ((GroupImpl)group).setParentId( parentId_);      
      return group;
    } catch ( NameNotFoundException e){          
    }
    return null;
  }
  
  public Collection getAllGroups() throws Exception{
    List<Group> groups = new ArrayList<Group>();    
    LdapContext ctx = ldapService_.getLdapContext();
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope( SearchControls.SUBTREE_SCOPE);
    NamingEnumeration<SearchResult> results = null;
    try{
      results = ctx.search( ldapAttrMapping_.groupsURL, "(ou=*)",  constraints);
    }catch( Exception exp){
      return groups; 
    }
    while (results.hasMore()) {
      SearchResult sr = results.next();
      NameParser parser = ctx.getNameParser("");
      CompositeName name = new CompositeName(sr.getName());
      if (name.size() > 0){
        Name entryName = parser.parse(name.get(0));
        String groupDN = entryName + "," + ldapAttrMapping_.groupsURL;       
        Group group = this.getGroupByDN(groupDN);        
        if (group != null) addGroup(groups, group); 
      }
    } 
    return groups;
  }
  
  public Collection findGroups(Group parent) throws Exception {
    List<Group> groups = new ArrayList<Group>();
    String groupsBaseDN = ldapAttrMapping_.groupsURL;
    StringBuffer buffer = new StringBuffer();
    
    if (parent != null) {
      String dnParts[] = parent.getId().split("/");
      for (int x = (dnParts.length - 1); x > 0; x--) {
        buffer.append("ou=" + dnParts[x] + ", ");
      }
    }
    buffer.append(groupsBaseDN);
    
    LdapContext ctx = ldapService_.getLdapContext();
    String searchBase = buffer.toString();
    String filter = ldapAttrMapping_.groupObjectClassFilter;   
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
    NamingEnumeration<SearchResult> results = null;
    try{
      results = ctx.search(searchBase, filter,  constraints);
    }catch( Exception exp){
      return groups; 
    }
    while (results.hasMore()) {
      SearchResult sr = results.next();
      NameParser parser = ctx.getNameParser("");
      CompositeName name = new CompositeName(sr.getName());
      if (name.size() > 0){
        Name entryName = parser.parse(name.get(0));
        String groupDN = entryName + "," + searchBase;       
        Group group = this.getGroupByDN(groupDN);
        if (group != null) addGroup(groups, group); 
      }
    }
    return groups;
  }
  
  public Collection findGroupsOfUser(String userName) throws Exception {
    List<Group> groups = new ArrayList<Group>();

    // check if user exists
    String userDN = getDNFromUsername(userName);
    if (userDN == null)
      return groups;
    userDN = userDN.trim();

    NamingEnumeration<SearchResult> results = null;
    LdapContext ctx = ldapService_.getLdapContext();
    try {
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
      String mbfilter = membershipClassFilter();
      String userFilter = "(" + ldapAttrMapping_.membershipTypeMemberValue + "=" + userDN + ")";      
      String filter = "(&" + userFilter + mbfilter + ")";          
      results = ctx.search(ldapAttrMapping_.groupsURL, filter, constraints);
    } catch (Exception exp) {
      if (log.isWarnEnabled())
        log.warn("Failed to retrieve memberships for user " + userName + ": " + exp.getMessage());
    }

    // add groups for memberships matching user
    int total = 0;
    while (results.hasMore()) {
      SearchResult sr = results.next();
      total++;
        NameParser parser = ctx.getNameParser("");
        CompositeName name = new CompositeName(sr.getName());
        if (name.size() < 1)
          break;
        Name entryName = parser.parse(name.get(0));
        String membershipDN = entryName + "," + ldapAttrMapping_.groupsURL;
        Group group = this.getGroupFromMembershipDN(membershipDN);
        if (group != null)
          addGroup(groups, group);
    }
    if (log.isDebugEnabled()) {
      log.debug("Retrieved " + groups.size() + " groups from ldap for user " + userName);
    }

    return groups;
  }
  
  protected void addGroup( List<Group> groups, Group g){
    for( int i=0; i<groups.size(); i++)
      if( groups.get( i).getId().equals( g.getId())) return;    
    groups.add( g);
  }
  
  
  protected void preSave(Group group , boolean isNew) throws Exception {
    for( GroupEventListener listener : listeners_ ) listener.preSave(group, isNew) ;    
  }
  
  protected void postSave(Group group , boolean isNew) throws Exception {
    for( GroupEventListener listener : listeners_ ) listener.postSave(group, isNew) ;    
  }
  
  protected void preDelete(Group group) throws Exception {
    for( GroupEventListener listener : listeners_ ) listener.preDelete(group) ;    
  }
  
  protected void postDelete(Group group) throws Exception {
    for( GroupEventListener listener : listeners_ ) listener.postDelete(group) ;   
  }
  
  protected String createSubDN( Group parent){     
    if (parent == null) return createSubDN("");    
    return createSubDN(parent.getId());
  }
  
  protected String createSubDN( String parentId){   
    StringBuffer buffer = new StringBuffer();     
    if ( parentId != null && parentId.length() > 0) {      
      String dnParts[] = parentId.split("/");
      for (int x = (dnParts.length - 1); x > 0; x--) 
        buffer.append("ou=" + dnParts[x] + ", ");       
    }     
    buffer.append(ldapAttrMapping_.groupsURL);     
    return buffer.toString();
  } 
  
  protected void setId( Group parent, Group g){
    GroupImpl group = (GroupImpl) g;    
    if ( parent == null){
      group.setId(  "/" + group.getGroupName());
      return;
    }
    group.setId( parent.getId() + "/" + group.getGroupName());
    group.setParentId( parent.getId());      
  }
}
