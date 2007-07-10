/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.database.DBObjectPageList;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.ObjectQuery;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.services.organization.impl.UserImpl;
import org.hibernate.Session;
import org.hibernate.Transaction;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:51:21 PM
 */
public class UserDAOImpl implements  UserHandler {
  public static final String queryFindUserByName =
    "from u in class org.exoplatform.services.organization.impl.UserImpl " +
    "where u.userName = ?" ;

  private HibernateService service_ ;
  private ExoCache cache_ ;
  private List<UserEventListener> listeners_  = new ArrayList<UserEventListener>(3);

  public UserDAOImpl(HibernateService service, CacheService cservice) throws Exception {
    service_ = service ; 
    cache_ = cservice.getCacheInstance(UserImpl.class.getName()) ;
  }

  final public List getUserEventListeners() {  return listeners_ ;  }

  public void addUserEventListener(UserEventListener listener) {
    listeners_.add(listener) ;
  }
  
  public User createUserInstance() {  return new UserImpl() ;  }
  
  public User createUserInstance(String username) { return new UserImpl(username) ; }
  
  public void createUser(User user, boolean broadcast) throws Exception {    
    Session session = service_.openSession();   
    Transaction transaction = session.beginTransaction() ;
    if(broadcast) preSave(user, true)  ;
    UserImpl userImpl = (UserImpl) user;
    userImpl.setId(user.getUserName());
    session.save(user);
    if(broadcast) postSave(user, true)  ;
    transaction.commit() ;    
  }

  public void saveUser(User user, boolean broadcast) throws Exception {
    Session session = service_.openSession();
    if(broadcast)preSave(user, false)  ;
    session.merge(user) ;
    //session.update(user);
    if(broadcast)postSave(user, false)  ;
    session.flush() ;
    cache_.put(user.getUserName(), user) ;
  }
  
  void createUserEntry(User user, Session session) throws Exception {
     session.save(user);
  }

  public User removeUser(String userName, boolean broadcast) throws Exception {
    Session session = service_.openSession();
    User foundUser = findUserByName(userName, session);
    if(foundUser == null)  return null;
    
    if(broadcast) preDelete(foundUser)  ;
    session.delete(foundUser);
    if(broadcast) postDelete(foundUser)  ;
    session.flush();
    cache_.remove(userName) ;
    return foundUser ;
  }

  public User findUserByName(String userName) throws Exception {
    User user = (User) cache_.get(userName) ;
    if(user != null) return user ;
    Session session = service_.openSession();
    user = findUserByName(userName, session) ;
    if(user != null)cache_.put(userName, user) ;
    return user;
  }

  public User findUserByName(String userName, Session session) throws Exception {
    User user = (User) service_.findOne(session, queryFindUserByName, userName);
    return user;
  }

  public PageList getUserPageList(int pageSize)  throws Exception {
    return new DBObjectPageList(service_ ,UserImpl.class) ;
  }
  
  public boolean authenticate(String username, String password) throws Exception  {    
    User user = findUserByName(username);   
    if(user == null) return false ;    
    boolean authenticated = user.getPassword().equals(password) ;
    if(authenticated){
      UserImpl userImpl = (UserImpl)user;
      userImpl.setLastLoginTime(Calendar.getInstance().getTime());      
      saveUser(userImpl, false);
    }
    return authenticated;
  }
  
  public PageList findUsers(Query q) throws Exception {    
    ObjectQuery oq = new ObjectQuery(UserImpl.class);
    oq.addLIKE("userName", q.getUserName()) ;
    oq.addLIKE("firstName", q.getFirstName() ) ;
    oq.addLIKE("lastName", q.getLastName()) ;
    oq.addLIKE("email", q.getEmail()) ;
    oq.addGT("lastLoginTime", q.getFromLoginDate()) ;
    oq.addLT("lastLoginTime", q.getToLoginDate()) ;
    return new DBObjectPageList(service_, oq);
  }

  public PageList findUsersByGroup(String groupId) throws Exception {
    String queryFindUsersInGroup =
      "select u " +
      "from u in class org.exoplatform.services.organization.impl.UserImpl, " +
      "     m in class org.exoplatform.services.organization.impl.MembershipImpl " +
      "where m.userName = u.userName " +
      "     and m.groupId =  '" + groupId + "'" ; 
    String countUsersInGroup =
      "select count(u) " +
      "from u in class org.exoplatform.services.organization.impl.UserImpl, " +
      "     m in class org.exoplatform.services.organization.impl.MembershipImpl " +
      "where m.userName = u.userName " +
      "  and m.groupId =  '" + groupId + "'" ; 
    return new DBObjectPageList(service_, 20,queryFindUsersInGroup, countUsersInGroup ) ;
  }

  public Collection findUsersByGroupAndRole(String groupName, String role) throws Exception {
    String queryFindUsersByGroupAndRole =
      "select u " +
      "from u in class org.exoplatform.services.organization.impl.UserImpl, " +
      "     m in class org.exoplatform.services.organization.impl.MembershipImpl, " +
      "     g in class org.exoplatform.services.organization.impl.GroupImpl " +
      "where m.user = u " +
      "  and m.group = g " +
      "  and g.groupName = ? " +
      "  and m.role = ? ";
    Session session = service_.openSession();
    org.hibernate.Query q = session.createQuery(queryFindUsersByGroupAndRole).
                            setString(0, groupName).setString(1, role) ;
    List users = q.list();
    return users;
  }
  
  private void preSave(User user , boolean isNew) throws Exception {
    for (UserEventListener listener : listeners_)   listener.preSave(user, isNew) ;
  }
  
  private void postSave(User user , boolean isNew) throws Exception {
    for (UserEventListener listener : listeners_) listener.postSave(user, isNew) ;
  }
  
  private void preDelete(User user) throws Exception {
    for (UserEventListener listener : listeners_)   listener.preDelete(user) ;
  }
  
  private void postDelete(User user) throws Exception {
    for (UserEventListener listener : listeners_)    listener.postDelete(user)  ;
  }
}
