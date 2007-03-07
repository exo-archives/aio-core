/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.ldap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.exoplatform.commons.utils.ObjectPageList;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.services.organization.impl.UserImpl;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 14, 2005
 */
public class UserDAOImpl extends BaseDAO implements UserHandler {
  
  private List<UserEventListener> listeners_  = new ArrayList<UserEventListener>(5);
  
  public UserDAOImpl(LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService) {
    super(ldapAttrMapping, ldapService) ;
  }
  
  public void addUserEventListener(UserEventListener listener) {
    listeners_.add(listener) ;
  }
  
  public User createUserInstance() {  return new UserImpl() ;  }
  
  public User createUserInstance(String username) {  return new UserImpl(username) ;  }
  
  public void createUser(User user, boolean broadcast) throws Exception {  
//    String userDN = "cn=" + user.getUserName()+ "," + ldapAttrMapping_.userURL;  
    String userDN = ldapAttrMapping_.userDNKey + "=" + user.getUserName()+ "," + ldapAttrMapping_.userURL;  
    Attributes attrs = ldapAttrMapping_.userToAttributes(user);
    if(broadcast) preSave(user, true) ; 
    ldapService_.getLdapContext().createSubcontext(userDN, attrs);
    if(broadcast) postSave(user, true) ;   
  }
  
  public void saveUser(User user, boolean broadcast) throws Exception {    
    String userDN = getDNFromUsername(user.getUserName());
    if (userDN == null) return;
    User existingUser = getUserFromUsername(user.getUserName());          
    ArrayList<ModificationItem> modifications = new ArrayList<ModificationItem>();
    
    // update displayName & description
    if (!user.getFullName().equals(existingUser.getFullName())){
      ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, 
          new BasicAttribute( ldapAttrMapping_.userDisplayNameAttr, user.getFullName()));
      modifications.add(mod);
      mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, 
          new BasicAttribute( ldapAttrMapping_.ldapDescriptionAttr, user.getFullName()));
      modifications.add(mod);
    }      
    // update account name
    if (!user.getUserName().equals(existingUser.getUserName())){
      ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
          new BasicAttribute( ldapAttrMapping_.userUsernameAttr, user.getUserName()));
      modifications.add(mod);
    }      
    // update last name
    if (!user.getLastName().equals(existingUser.getLastName())){
      ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, 
          new BasicAttribute( ldapAttrMapping_.userLastNameAttr, user.getLastName()));
      modifications.add(mod);
    }      
    // update first name
    if (!user.getFirstName().equals(existingUser.getFirstName())){
      ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, 
          new BasicAttribute( ldapAttrMapping_.userFirstNameAttr, user.getFirstName()));
      modifications.add(mod);
    }      
    // update email
    if (!user.getEmail().equals(existingUser.getEmail())){
      ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
          new BasicAttribute(ldapAttrMapping_.userMailAttr, user.getEmail()));
      modifications.add(mod);
    }      
    
    ModificationItem[] mods = new ModificationItem[modifications.size()];      
    modifications.toArray(mods);
    if(broadcast) preSave(user, false);
    ldapService_.getLdapContext().modifyAttributes(userDN, mods);
    if(broadcast) postSave(user, false);    
    if (!user.getPassword().equals("PASSWORD")) saveUserPassword(user, userDN);    
  }
  
  void saveUserPassword(User user, String userDN) throws Exception {
    ModificationItem[] mods = new ModificationItem[]{ 
        new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
            new BasicAttribute(ldapAttrMapping_.userPassword, user.getPassword())) };  
    ldapService_.getLdapContext().modifyAttributes(userDN, mods);
  }
  
  public User removeUser(String userName, boolean broadcast) throws Exception {
    User user = getUserFromUsername(userName) ;
    if(user == null)  return null;
    LdapContext ctx = ldapService_.getLdapContext();
    if(broadcast) preDelete(user)  ;
    ctx.destroySubcontext(getDNFromUsername(userName));      
    if(broadcast) postDelete(user) ;
    return user ;
  }
   
  public User findUserByName(String userName) throws Exception {
    return getUserFromUsername(userName);
  }  
  
  public PageList findUsersByGroup(String groupId) throws Exception {    
    ArrayList<User> users = new ArrayList<User>();
    TreeMap< String, User> map = new TreeMap< String, User>();    
    
    LdapContext ctx = ldapService_.getLdapContext();
    String searchBase = this.getGroupDNFromGroupId(groupId);
    String filter = ldapAttrMapping_.membershipObjectClassFilter;    
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
    NamingEnumeration<SearchResult> results = ctx.search(searchBase, filter, constraints);
    
    while( results.hasMore()){
      SearchResult sr = results.next();
      Attributes attrs = sr.getAttributes();
      List<Object> members = this.getAttributes(attrs, ldapAttrMapping_.membershipTypeMemberValue);
      for (int x = 0; x < members.size(); x++){        
        User user = findUserByDN((String)members.get(x), ctx);       
        if( user != null) map.put(user.getUserName(), user);
      }
    }  
    
    for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) users.add( map.get(i.next()));    
    return new ObjectPageList(users, 20);
  }
  
  protected User findUserByDN (String userDN, LdapContext ctx) throws Exception {   
    if (userDN == null) return null;
    try {      
      Attributes attrs = ctx.getAttributes(userDN);
      if (attrs == null) return null;
      User user = ldapAttrMapping_.attributesToUser(attrs);         
      user.setFullName(user.getFirstName()+" "+user.getLastName());  
      return user;
    } catch (NameNotFoundException e){     
      return null;
    }
  }
  
  public PageList getUserPageList(int pageSize) throws Exception {      
    String searchBase = ldapAttrMapping_.userURL;
    String filter = ldapAttrMapping_.userObjectClassFilter;    
    return new LDAPUserPageList(ldapAttrMapping_, ldapService_, searchBase, filter, pageSize);
  }
  
  public PageList findUsers(Query q) throws Exception { 
    String filter = null;
    ArrayList<String> list = new ArrayList<String>();
    if (q.getUserName() != null && q.getUserName().length() > 0) {
      list.add("(" + ldapAttrMapping_.userUsernameAttr + "=" + q.getUserName() + ")");
    }
    if (q.getFirstName() != null && q.getFirstName().length() > 0) {
      list.add("(" + ldapAttrMapping_.userFirstNameAttr + "=" + q.getFirstName() + ")");
    }
    if (q.getLastName() != null && q.getLastName().length() > 0) {
      list.add("(" + ldapAttrMapping_.userLastNameAttr + "=" + q.getLastName() + ")");
    }
    if (q.getEmail() != null && q.getEmail().length() > 0) {
      list.add("(" + ldapAttrMapping_.userMailAttr + "=" + q.getEmail() + ")");
    }
    
    if (list.size() > 0){
      StringBuilder buffer = new StringBuilder();
      buffer.append("(&");
      if (list.size() > 1){
        for (int x = 0; x < list.size(); x++){
          if (x == (list.size() - 1))  buffer.append(list.get(x));
          else buffer.append(list.get(x)+" || ");          
        }
      } else 
        buffer.append(list.get(0));
      
      buffer.append("(" +ldapAttrMapping_.userObjectClassFilter + ") )");
      filter = buffer.toString();
    } else  
      filter = ldapAttrMapping_.userObjectClassFilter ;    
    String searchBase = ldapAttrMapping_.userURL ;
    return new LDAPUserPageList(ldapAttrMapping_, ldapService_, searchBase, filter, 20);   
  }   
  
  public boolean authenticate(String username, String password) throws Exception {
    String userDN = getDNFromUsername(username);
    if(userDN == null) return false ;
    try{
      return ldapService_.authenticate(userDN, password);
    }catch(Exception exp){
      return false;
    }
  }
  
  protected void preSave(User user , boolean isNew) throws Exception {
    for (UserEventListener listener : listeners_)   listener.preSave(user, isNew) ;
  }
  
  protected void postSave(User user , boolean isNew) throws Exception {
    for (UserEventListener listener : listeners_) listener.postSave(user, isNew) ;
  }
  
  protected void preDelete(User user) throws Exception {
    for (UserEventListener listener : listeners_)   listener.preDelete(user) ;
  }
  
  protected void postDelete(User user) throws Exception {
    for (UserEventListener listener : listeners_)    listener.postDelete(user)  ;
  }
}
