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
package org.exoplatform.services.organization.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.InvalidNameException;

import org.exoplatform.services.log.Log;

import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBObjectQuery;
import org.exoplatform.services.database.DBPageList;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.database.StandardSQLDAO;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipEventListener;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.User;

/**
 * Created by The eXo Platform SAS Apr 7, 2007
 */
public class MembershipDAOImpl extends StandardSQLDAO<MembershipImpl> implements MembershipHandler {

  protected static Log      log = ExoLogger.getLogger("organization:MembershipDAOImpl");

  protected ListenerService listenerService_;

  public MembershipDAOImpl(ListenerService lService,
                           ExoDatasource datasource,
                           DBObjectMapper<MembershipImpl> mapper) {
    super(datasource, mapper, MembershipImpl.class);
    listenerService_ = lService;
  }

  public Membership createMembershipInstance() {
    return new MembershipImpl();
  }

  public void createMembership(Membership membership, boolean broadcast) throws Exception {
    MembershipImpl membershipImpl = (MembershipImpl) membership;
    if (broadcast)
      listenerService_.broadcast("organization.membership.preSave", this, membershipImpl);
    membershipImpl.setId(IdentifierUtil.generateUUID(membership));
    super.save(membershipImpl);
    if (broadcast)
      listenerService_.broadcast("organization.membership.postSave", this, membershipImpl);
  }

  public void linkMembership(User user, Group group, MembershipType mt, boolean broadcast) throws Exception {
    if (group == null) {
      throw new InvalidNameException("Can not create membership record for " + user.getUserName()
          + " because group is null");
    }

    if (mt == null) {
      throw new InvalidNameException("Can not create membership record for " + user.getUserName()
          + " because membership type is null");
    }

    if (log.isDebugEnabled())
      log.debug("LINK MEMBER SHIP (" + user.getUserName() + ", " + group.getId() + " , "
          + mt.getName() + ");");
    MembershipImpl membership = new MembershipImpl();
    membership.setUserName(user.getUserName());
    membership.setMembershipType(mt.getName());
    membership.setGroupId(group.getId());
    if (findMembershipByUserGroupAndType(user.getUserName(), group.getId(), mt.getName()) != null)
      return;
    createMembership(membership, broadcast);
  }

  public Membership findMembership(String id) throws Exception {
    if (id == null)
      return null;
    DBObjectQuery<MembershipImpl> query = new DBObjectQuery<MembershipImpl>(MembershipImpl.class);
    query.addLIKE("MEMBERSHIP_ID", id);
    return loadUnique(query.toQuery());
  }

  public Membership findMembershipByUserGroupAndType(String userName, String groupId, String type) throws Exception {

    if (userName == null || groupId == null || type == null)
      return null;
    DBObjectQuery<MembershipImpl> query = new DBObjectQuery<MembershipImpl>(MembershipImpl.class);
    query.addLIKE("USER_NAME", userName);
    query.addLIKE("GROUP_ID", groupId);
    query.addLIKE("MEMBERSHIP_TYPE", type);
    Membership member = loadUnique(query.toQuery());
    if (log.isDebugEnabled())
      log.debug("FIND MEMBERSHIP BY USER " + userName + ", GROUP " + groupId + ", TYPE " + type
          + " - " + (member != null));
    return member;
  }

  public Collection findMembershipsByGroup(Group group) throws Exception {

    if (group == null)
      return null;
    List<MembershipImpl> list = new ArrayList<MembershipImpl>();
    DBObjectQuery<MembershipImpl> query = new DBObjectQuery<MembershipImpl>(MembershipImpl.class);
    query.addLIKE("GROUP_ID", group.getId());
    loadInstances(query.toQuery(), list);
    return list;
  }

  public Collection findMembershipsByUser(String userName) throws Exception {
    if (userName == null)
      return null;
    List<MembershipImpl> list = new ArrayList<MembershipImpl>();
    DBObjectQuery<MembershipImpl> query = new DBObjectQuery<MembershipImpl>(MembershipImpl.class);
    query.addLIKE("USER_NAME", userName);
    loadInstances(query.toQuery(), list);
    if (log.isDebugEnabled())
      log.debug("FIND MEMBERSHIP BY USER " + userName + " Size = " + list.size());
    return list;
  }

  public Collection findMembershipsByUserAndGroup(String userName, String groupId) throws Exception {
    if (userName == null || groupId == null)
      return null;
    List<MembershipImpl> list = new ArrayList<MembershipImpl>();
    DBObjectQuery<MembershipImpl> query = new DBObjectQuery<MembershipImpl>(MembershipImpl.class);
    query.addLIKE("USER_NAME", userName);
    query.addLIKE("GROUP_ID", groupId);
    loadInstances(query.toQuery(), list);
    if (log.isDebugEnabled())
      log.debug("FIND MEMBERSHIP BY USER " + userName + ", GROUP " + groupId + " Size = "
          + list.size());
    return list;
  }

  public Membership removeMembership(String id, boolean broadcast) throws Exception {
    DBObjectQuery<MembershipImpl> query = new DBObjectQuery<MembershipImpl>(MembershipImpl.class);
    query.addLIKE("MEMBERSHIP_ID", id);
    Connection connection = eXoDS_.getConnection();
    try {
      MembershipImpl membershipImpl = super.loadUnique(connection, query.toQuery());
      if (membershipImpl == null)
        return null;
      if (broadcast)
        listenerService_.broadcast("organization.membership.preDelete", this, membershipImpl);
      String sql = eXoDS_.getQueryBuilder()
                         .createRemoveQuery(type_, membershipImpl.getDBObjectId());
      super.execute(connection, sql, (MembershipImpl) null);
      if (broadcast)
        listenerService_.broadcast("organization.membership.postDelete", this, membershipImpl);
      return membershipImpl;
    } catch (Exception e) {
      throw e;
    } finally {
      eXoDS_.closeConnection(connection);
    }
  }

  @SuppressWarnings("unchecked")
  public Collection removeMembershipByUser(String username, boolean broadcast) throws Exception {
    // DBObjectQuery<MembershipImpl> query = new
    // DBObjectQuery<MembershipImpl>(MembershipImpl.class);
    // query.addLIKE("userName", username);
    List<Membership> members = (List<Membership>) findMembershipsByUser(username);
    for (Membership member : members) {
      removeMembership(member.getId(), true);
    }
    return members;
  }

  public Collection removeMemberships(DBObjectQuery<MembershipImpl> query, boolean broadcast) throws Exception {
    DBPageList<MembershipImpl> pageList = new DBPageList<MembershipImpl>(20, this, query);
    List<MembershipImpl> list = pageList.getAll();
    Connection connection = eXoDS_.getConnection();
    try {
      for (MembershipImpl membershipImpl : list) {
        if (broadcast)
          listenerService_.broadcast("organization.membership.preDelete", this, membershipImpl);
        if (membershipImpl == null)
          return null;
        String sql = eXoDS_.getQueryBuilder().createRemoveQuery(type_,
                                                                membershipImpl.getDBObjectId());
        super.execute(connection, sql, (MembershipImpl) null);
        if (broadcast)
          listenerService_.broadcast("organization.membership.postDelete", this, membershipImpl);
      }
      return list;
    } catch (Exception e) {
      throw e;
    } finally {
      eXoDS_.closeConnection(connection);
    }
  }

  @SuppressWarnings("unchecked")
  public void addMembershipEventListener(MembershipEventListener listener) {
    throw new RuntimeException("This method is not supported anymore, please use the new api");
  }

}
