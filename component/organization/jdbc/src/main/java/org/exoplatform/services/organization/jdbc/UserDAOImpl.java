/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import java.util.Calendar;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBObjectQuery;
import org.exoplatform.services.database.DBPageList;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.database.StandardSQLDAO;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.organization.UserHandler;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Apr 7, 2007  
 */
public class UserDAOImpl extends StandardSQLDAO<UserImpl> implements  UserHandler {
  
  public UserDAOImpl(ListenerService lService, ExoDatasource datasource, DBObjectMapper<UserImpl> mapper) {
    super(lService, datasource, mapper, UserImpl.class);
  }
  
  public User createUserInstance() { return new UserImpl(); }

  public User createUserInstance(String username) { return new UserImpl(username); }
  
  
  public void createUser(User user, boolean broadcast) throws Exception {
    UserImpl userImpl = (UserImpl)user;
    if(broadcast) invokeEvent("pre", "insert", userImpl);
    super.save(userImpl);
    if(broadcast) invokeEvent("post", "insert", userImpl);
  }
  
  public boolean authenticate(String username, String password) throws Exception {
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

  public User findUserByName(String userName) throws Exception {
    DBObjectQuery<UserImpl> query = new DBObjectQuery<UserImpl>(UserImpl.class);
    query.addLIKE("username", userName);
    return loadUnique(query.toQuery());
  }

  public PageList findUsers(org.exoplatform.services.organization.Query orgQuery) throws Exception {
    DBObjectQuery dbQuery = new DBObjectQuery<UserImpl>(UserImpl.class);
    dbQuery.addLIKE("userName", orgQuery.getUserName()) ;
    dbQuery.addLIKE("firstName", orgQuery.getFirstName() ) ;
    dbQuery.addLIKE("lastName", orgQuery.getLastName()) ;
    dbQuery.addLIKE("email", orgQuery.getEmail()) ;
    dbQuery.addGT("lastLoginTime", orgQuery.getFromLoginDate()) ;
    dbQuery.addLT("lastLoginTime", orgQuery.getToLoginDate()) ;
    return new DBPageList<UserImpl>(20, this, dbQuery.toQuery(), dbQuery.toCountQuery());
  }

  public PageList findUsersByGroup(String groupId) throws Exception {
    DBObjectQuery dbQuery = new DBObjectQuery<UserImpl>(UserImpl.class);
//    dbQuery.addLIKE()
    return null;
  }

  public PageList getUserPageList(int pageSize) throws Exception {
    return new DBPageList<UserImpl>(pageSize, this, new DBObjectQuery<UserImpl>(UserImpl.class));
  }

  public User removeUser(String userName, boolean broadcast) throws Exception {
    UserImpl userImpl = (UserImpl) findUserByName(userName);
    if(userImpl == null) return null;
    if(broadcast) invokeEvent("pre", "delete", userImpl);
    super.remove(userImpl);
    if(broadcast) invokeEvent("post", "delete", userImpl);
    return userImpl;
  }

  public void saveUser(User user, boolean broadcast) throws Exception {
    UserImpl userImpl = (UserImpl)user;
    if(broadcast) invokeEvent("pre", "update", userImpl);
    super.update(userImpl);
    if(broadcast) invokeEvent("post", "update", userImpl);
  }

  @SuppressWarnings("unused")
  public void addUserEventListener(UserEventListener listener) {}

}
