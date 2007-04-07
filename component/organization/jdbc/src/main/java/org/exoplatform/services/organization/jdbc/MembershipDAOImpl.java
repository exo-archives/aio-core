/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import java.util.Collection;

import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipEventListener;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.User;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Apr 7, 2007  
 */
public class MembershipDAOImpl implements MembershipHandler {

  public void addMembershipEventListener(MembershipEventListener listener) {
    // TODO Auto-generated method stub

  }

  public void createMembership(Membership m, boolean broadcast) throws Exception {
    // TODO Auto-generated method stub

  }

  public Membership createMembershipInstance() {
    // TODO Auto-generated method stub
    return null;
  }

  public Membership findMembership(String id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Membership findMembershipByUserGroupAndType(String userName, String groupId, String type)
      throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection findMembershipsByGroup(Group group) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection findMembershipsByUser(String userName) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection findMembershipsByUserAndGroup(String userName, String groupId) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public void linkMembership(User user, Group group, MembershipType m, boolean broadcast)
      throws Exception {
    // TODO Auto-generated method stub

  }

  public Membership removeMembership(String id, boolean broadcast) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection removeMembershipByUser(String username, boolean broadcast) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

}
