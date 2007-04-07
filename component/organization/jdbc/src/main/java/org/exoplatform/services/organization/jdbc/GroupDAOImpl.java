/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import java.util.Collection;

import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupEventListener;
import org.exoplatform.services.organization.GroupHandler;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Apr 7, 2007  
 */
public class GroupDAOImpl implements GroupHandler {

  public void addChild(Group parent, Group child, boolean broadcast) throws Exception {
    // TODO Auto-generated method stub

  }

  public void addGroupEventListener(GroupEventListener listener) {
    // TODO Auto-generated method stub

  }

  public void createGroup(Group group, boolean broadcast) throws Exception {
    // TODO Auto-generated method stub

  }

  public Group createGroupInstance() {
    // TODO Auto-generated method stub
    return null;
  }

  public Group findGroupById(String groupId) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection findGroupByMembership(String userName, String membershipType) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection findGroups(Group parent) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection findGroupsOfUser(String user) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection getAllGroups() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Group removeGroup(Group group, boolean broadcast) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public void saveGroup(Group group, boolean broadcast) throws Exception {
    // TODO Auto-generated method stub

  }

}
