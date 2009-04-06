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
package org.exoplatform.services.organization.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.commons.utils.ListenerStack;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipEventListener;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.MembershipTypeNullException;
import org.exoplatform.services.organization.NullGroupException;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.impl.MembershipImpl;

/**
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen tuan08@users.sourceforge.net Date: Aug 22, 2003 Time: 4:51:21 PM
 */
public class MembershipDAOImpl implements MembershipHandler {

  private static final String queryFindMembershipByUserGroupAndType = "from m in class org.exoplatform.services.organization.impl.MembershipImpl "
                                                                        + "where m.userName = ? "
                                                                        + "  and m.groupId = ? "
                                                                        + "  and m.membershipType = ? ";

  private static final String queryFindMembershipsByUserAndGroup    = "from m in class org.exoplatform.services.organization.impl.MembershipImpl "
                                                                        + "where m.userName = ? "
                                                                        + "  and m.groupId = ? ";

  private static final String queryFindMembershipsByGroup           = "from m in class org.exoplatform.services.organization.impl.MembershipImpl "
                                                                        + "where m.groupId = ? ";

  private static final String queryFindMembership                   = "from m in class org.exoplatform.services.organization.impl.MembershipImpl "
                                                                        + "where m.id = ? ";

  private static final String queryFindMembershipsByUser            = "from m in class org.exoplatform.services.organization.impl.MembershipImpl "
                                                                        + "where m.userName = ? ";

  private HibernateService    service_;

  private List                listeners_;

  public MembershipDAOImpl(HibernateService service) {
    service_ = service;
    listeners_ = new ListenerStack(5);
  }

  public void addMembershipEventListener(MembershipEventListener listener) {
    listeners_.add(listener);
  }

  final public Membership createMembershipInstance() {
    return new MembershipImpl();
  }

  public void createMembership(Membership m, boolean broadcast) throws Exception {
    Session session = service_.openSession();
    if (broadcast)
      preSave(m, true);
    session.save(IdentifierUtil.generateUUID(m), m);
    if (broadcast)
      postSave(m, true);
    session.flush();
  }

  // static void createMembershipEntries(Collection c, Session session) throws
  // Exception {
  // Iterator i = c.iterator() ;
  // while(i.hasNext()) {
  // Membership impl = (Membership) i.next() ;
  // session.save(impl, impl.getId());
  // }
  // }

  public void linkMembership(User user, Group g, MembershipType mt, boolean broadcast) throws Exception {
    if (g == null) {
      throw new NullGroupException("Can not create membership record for " + user.getUserName()
          + " because group is null");
    }

    if (mt == null) {
      throw new MembershipTypeNullException("Can not create membership record for "
          + user.getUserName() + " because membership type is null");
    }

    Session session = service_.openSession();
    MembershipImpl membership = new MembershipImpl();
    // User user
    // =(User)service_.findExactOne(session,UserHandlerImpl.queryFindUserByName,
    // userName);
    membership.setUserName(user.getUserName());
    membership.setMembershipType(mt.getName());
    membership.setGroupId(g.getId());
    if (membership.getId() != null)
      throw new Exception(" Membership id isn't null!");
    if (findMembershipByUserGroupAndType(user.getUserName(), g.getId(), mt.getName()) != null)
      return;
    String id = IdentifierUtil.generateUUID(membership);
    if (broadcast)
      preSave(membership, true);
    membership.setId(id);
    session.save(membership);
    if (broadcast)
      postSave(membership, true);
    session.flush();
  }

  public void saveMembership(Membership m, boolean broadcast) throws Exception {
    Session session = service_.openSession();
    if (broadcast)
      preSave(m, false);
    session.update(m);
    if (broadcast)
      postSave(m, false);
    session.flush();
  }

  public Membership removeMembership(String id, boolean broadcast) throws Exception {
    Session session = service_.openSession();

    Membership m = (Membership) service_.findOne(session, queryFindMembership, id);
    if (m != null) {
      if (broadcast)
        preDelete(m);
      session.delete(m);
      if (broadcast)
        postDelete(m);
      session.flush();
    }
    return m;
  }

  public Collection removeMembershipByUser(String username, boolean broadcast) throws Exception {
    Session session = service_.openSession();
    Collection collection = findMembershipsByUser(username);
    Iterator iter = collection.iterator();
    while (iter.hasNext()) {
      Membership m = (Membership) iter.next();
      if (m != null) {
        if (broadcast)
          preDelete(m);
        session.delete(m);
        if (broadcast)
          postDelete(m);
        session.flush();
      }
    }
    return collection;
  }

  public Membership findMembershipByUserGroupAndType(String userName, String groupId, String type) throws Exception {
    Session session = service_.openSession();
    // Object[] args = new Object[] { userName, groupId , type};
    // Type[] types = new Type[] { Hibernate.STRING, Hibernate.STRING,
    // Hibernate.STRING };
    List memberships = session.createQuery(queryFindMembershipByUserGroupAndType)
                              .setString(0, userName)
                              .setString(1, groupId)
                              .setString(2, type)
                              .list();
    if (memberships.size() == 0) {
      return null;
    } else if (memberships.size() == 1) {
      return (Membership) memberships.get(0);
    } else {
      throw new Exception("Expect 0 or 1 membership but found" + memberships.size());
    }
  }

  public Collection findMembershipsByUserAndGroup(String userName, String groupId) throws Exception {
    Session session = service_.openSession();
    // Object[] args = new Object[] { userName, groupId };
    // Type[] types = new Type[] { Hibernate.STRING, Hibernate.STRING };
    List memberships = session.createQuery(queryFindMembershipsByUserAndGroup)
                              .setString(0, userName)
                              .setString(1, groupId)
                              .list();
    return memberships;
  }

  public Collection findMembershipsByUser(String userName) throws Exception {
    Session session = service_.openSession();
    List memberships = session.createQuery(queryFindMembershipsByUser)
                              .setString(0, userName)
                              .list();
    return memberships;
  }

  static void removeMembershipEntriesOfUser(String userName, Session session) throws Exception {
    List entries = session.createQuery(queryFindMembershipsByUser).setString(0, userName).list();
    for (int i = 0; i < entries.size(); i++)
      session.delete(entries.get(i));
  }

  static void removeMembershipEntriesOfGroup(Group group, Session session) throws Exception {
    List entries = session.createQuery(queryFindMembershipsByGroup)
                          .setString(0, group.getId())
                          .list();
    for (int i = 0; i < entries.size(); i++)
      session.delete(entries.get(i));
  }

  Collection findMembershipsByUser(String userName, Session session) throws Exception {
    return session.createQuery(queryFindMembershipsByUser).setString(0, userName).list();
  }

  public Collection findMembershipsByGroup(Group group) throws Exception {
    Session session = service_.openSession();
    List memberships = session.createQuery(queryFindMembershipsByGroup)
                              .setString(0, group.getId())
                              .list();
    return memberships;
  }

  public Collection findMembershipsByGroupId(String groupId) throws Exception {
    Session session = service_.openSession();
    // List memberships = session.find( queryFindMembershipsByGroup, groupId,
    // Hibernate.STRING );
    List memberships = session.createQuery(queryFindMembershipsByGroup)
                              .setString(0, groupId)
                              .list();
    return memberships;
  }

  public Membership findMembership(String id) throws Exception {
    Session session = service_.openSession();
    List memberships = session.createQuery(queryFindMembership).setString(0, id).list();
    if (memberships.size() == 0) {
      return null;
    } else if (memberships.size() == 1) {
      return (Membership) memberships.get(0);
    } else {
      throw new Exception("Expect 0 or 1 membership but found" + memberships.size());
    }
    // Membership membership =
    // (Membership) session.createQuery(queryFindMembership).setString(0,
    // id).list() ;
    // return membership;
  }

  private void preSave(Membership membership, boolean isNew) throws Exception {
    for (int i = 0; i < listeners_.size(); i++) {
      MembershipEventListener listener = (MembershipEventListener) listeners_.get(i);
      listener.preSave(membership, isNew);
    }
  }

  private void postSave(Membership membership, boolean isNew) throws Exception {
    for (int i = 0; i < listeners_.size(); i++) {
      MembershipEventListener listener = (MembershipEventListener) listeners_.get(i);
      listener.postSave(membership, isNew);
    }
  }

  private void preDelete(Membership membership) throws Exception {
    for (int i = 0; i < listeners_.size(); i++) {
      MembershipEventListener listener = (MembershipEventListener) listeners_.get(i);
      listener.preDelete(membership);
    }
  }

  private void postDelete(Membership membership) throws Exception {
    for (int i = 0; i < listeners_.size(); i++) {
      MembershipEventListener listener = (MembershipEventListener) listeners_.get(i);
      listener.postDelete(membership);
    }
  }
}
