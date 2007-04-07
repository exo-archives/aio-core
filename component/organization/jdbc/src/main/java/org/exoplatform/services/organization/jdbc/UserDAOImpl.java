/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.commons.utils.PageList;
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
public class UserDAOImpl  implements  UserHandler {
  
  private List<UserEventListener> listeners_  = new ArrayList<UserEventListener>(3);

  public void addUserEventListener(UserEventListener listener) {
    // TODO Auto-generated method stub
  }

  public boolean authenticate(String username, String password) throws Exception {
    // TODO Auto-generated method stub
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
