/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import java.util.List;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.database.DAO;
import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.organization.UserHandler;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Apr 7, 2007  
 */
public class UserDAOImpl  extends DAO<UserImpl> implements  UserHandler {
  
  private Class<UserImpl> type_ = UserImpl.class;
  
  protected ListenerService listenerService_;
  
  public UserDAOImpl(ListenerService lService, ExoDatasource datasource, DBObjectMapper<UserImpl> mapper) {
    super(datasource, mapper);
    listenerService_ = lService;
  }

  @Override
  public UserImpl createInstance() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UserImpl load(long id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PageList loadAll() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UserImpl remove(long id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void remove(UserImpl bean) throws Exception {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void save(List<UserImpl> beans) throws Exception {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void save(UserImpl bean) throws Exception {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void update(List<UserImpl> beans) throws Exception {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void update(UserImpl bean) throws Exception {
    // TODO Auto-generated method stub
    
  }

  @SuppressWarnings("unused")
  public void addUserEventListener(UserEventListener listener) {
  }

  public boolean authenticate(String username, String password) throws Exception {
    return false;
  }

  public void createUser(User user, boolean broadcast) throws Exception {
    // TODO Auto-generated method stub
    
  }

  public User createUserInstance() {
    // TODO Auto-generated method stub
    return null;
  }

  public User createUserInstance(String username) {
    // TODO Auto-generated method stub
    return null;
  }

  public User findUserByName(String userName) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public PageList findUsers(Query query) throws Exception {
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

}
