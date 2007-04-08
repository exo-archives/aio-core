/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBObjectQuery;
import org.exoplatform.services.database.DBPageList;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.database.StandardSQLDAO;
import org.exoplatform.services.listener.ListenerService;
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
public class MembershipDAOImpl extends StandardSQLDAO<MembershipImpl> implements MembershipHandler {
  
  public MembershipDAOImpl(ListenerService lService, ExoDatasource datasource, DBObjectMapper<MembershipImpl> mapper) {
    super(lService, datasource, mapper, MembershipImpl.class);
  }
  
  public Membership createMembershipInstance() { return new MembershipImpl(); }

  public void createMembership(Membership membership, boolean broadcast) throws Exception {
    MembershipImpl membershipImpl = (MembershipImpl) membership;
    if(broadcast) invokeEvent("pre", "insert", membershipImpl);
    membershipImpl.setId(IdentifierUtil.generateUUID(membership));
    super.save(membershipImpl);
    if(broadcast) invokeEvent("post", "insert", membershipImpl);
  }

  public void linkMembership(User user, Group group, MembershipType mt, boolean broadcast) throws Exception {
    MembershipImpl membership = new MembershipImpl();
    membership.setUserName(user.getUserName());
    membership.setMembershipType(mt.getName()) ;    
    membership.setGroupId( group.getId());    
    if(findMembershipByUserGroupAndType( user.getUserName(), group.getId(), mt.getName()) != null) return;   
    createMembership(membership, broadcast);
  }

  public Membership findMembership(String id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Membership findMembershipByUserGroupAndType(String userName, String groupId, String type) throws Exception {
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


  public Membership removeMembership(String id, boolean broadcast) throws Exception {
    DBObjectQuery<MembershipImpl> query = new DBObjectQuery<MembershipImpl>(MembershipImpl.class);
    query.addLIKE("id", id);
    Connection connection = eXoDS_.getConnection();
    try {
      MembershipImpl membershipImpl = super.loadUnique(connection, query.toQuery());
      if(membershipImpl == null) return null;
      if(broadcast) invokeEvent("pre", "delete", membershipImpl);
      String sql = eXoDS_.getQueryBuilder().createRemoveQuery(type_, membershipImpl.getDBObjectId());
      super.execute(connection, sql, (MembershipImpl)null);
      if(broadcast) invokeEvent("post", "delete", membershipImpl);
      return membershipImpl;
    }catch (Exception e) {
      throw e;
    } finally {
      eXoDS_.closeConnection(connection) ; 
    }
  }

  public Collection removeMembershipByUser(String username, boolean broadcast) throws Exception {
    DBObjectQuery<MembershipImpl> query = new DBObjectQuery<MembershipImpl>(MembershipImpl.class);
    query.addLIKE("userName", username);
    return null;
  }
  
  public Collection removeMemberships(DBObjectQuery<MembershipImpl> query , boolean broadcast) throws Exception {
    DBPageList<MembershipImpl> pageList = new DBPageList<MembershipImpl>(20, this, query);
    List<MembershipImpl> list = pageList.getAll();
    Connection connection = eXoDS_.getConnection();
    try {
      for(MembershipImpl membershipImpl : list) {
        if(broadcast) invokeEvent("pre", "delete", membershipImpl);
        if(membershipImpl == null) return null;
        String sql = eXoDS_.getQueryBuilder().createRemoveQuery(type_, membershipImpl.getDBObjectId());
        super.execute(connection, sql, (MembershipImpl)null);
        if(broadcast) invokeEvent("post", "delete", membershipImpl);
      }
      return list;
    }catch (Exception e) {
      throw e;
    } finally {
      eXoDS_.closeConnection(connection) ; 
    }
  }
  
  
  @SuppressWarnings("unchecked")
  public void addMembershipEventListener(MembershipEventListener listener) { }

}
