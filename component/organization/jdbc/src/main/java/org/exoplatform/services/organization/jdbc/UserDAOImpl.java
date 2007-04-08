/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBObjectQuery;
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
  
  public User createUserInstance() { return new UserImpl(); }

  public User createUserInstance(String username) { return new UserImpl(username); }
  
  
  public void createUser(User user, boolean broadcast) throws Exception {
    UserImpl userImpl = (UserImpl)user;
    if(broadcast) invokeEvent("pre", "save", userImpl);
    super.save(userImpl);
    if(broadcast) invokeEvent("post", "save", userImpl);
  }
  
  public boolean authenticate(String username, String password) throws Exception {
    return false;
  }

  public User findUserByName(String userName) throws Exception {
    DBObjectQuery<UserImpl> query = new DBObjectQuery<UserImpl>(UserImpl.class);
    query.addLIKE("username", userName);
    return loadUnique(query.toQuery());
  }

  public PageList findUsers(org.exoplatform.services.organization.Query query) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public PageList findUsersByGroup(String groupId) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public PageList getUserPageList(int pageSize) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public User removeUser(String userName, boolean broadcast) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public void saveUser(User user, boolean broadcast) throws Exception {
    // TODO Auto-generated method stub
    
  }

  public UserDAOImpl(ListenerService lService, ExoDatasource datasource, DBObjectMapper<UserImpl> mapper) {
    super(lService, datasource, mapper, UserImpl.class);
  }

  @SuppressWarnings("unused")
  public void addUserEventListener(UserEventListener listener) {}

}
