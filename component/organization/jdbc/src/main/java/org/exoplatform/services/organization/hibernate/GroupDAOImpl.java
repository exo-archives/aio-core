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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import org.exoplatform.commons.exception.UniqueObjectException;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupEventListener;
import org.exoplatform.services.organization.GroupEventListenerHandler;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.impl.GroupImpl;

/**
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Date: Aug 22, 2003 Time: 4:51:21 PM
 */
public class GroupDAOImpl implements GroupHandler, GroupEventListenerHandler {
  public static final String       queryFindGroupByName       = "from g in class org.exoplatform.services.organization.impl.GroupImpl "
                                                                  + "where g.groupName = ? ";

  public static final String       queryFindGroupById         = "from g in class org.exoplatform.services.organization.impl.GroupImpl "
                                                                  + "where g.id = ? ";

  public static final String       queryFindGroupByParent     = "from g in class org.exoplatform.services.organization.impl.GroupImpl "
                                                                  + "where g.parentId = ? ";

  private static final String      queryFindRootGroups        = "from g in class org.exoplatform.services.organization.impl.GroupImpl "
                                                                  + "where g.parentId is null";

  private static final String      queryFindGroupsOfUser      = "select distinct g "
                                                                  + "from g in class org.exoplatform.services.organization.impl.GroupImpl, "
                                                                  + "     m in class org.exoplatform.services.organization.impl.MembershipImpl "
                                                                  + "where m.groupId = g.id "
                                                                  + "  and m.userName = ?";

  private static final String      queryFindGroupByMembership = "select g "
                                                                  + "from m in class org.exoplatform.services.organization.impl.MembershipImpl, "
                                                                  + "     g in class org.exoplatform.services.organization.impl.GroupImpl "
                                                                  + "where m.groupId = g.id "
                                                                  + "  and m.userName = ? "
                                                                  + "  and m.membershipType = ? ";

  private static final String      queryGetAllGroups          = "from g in class org.exoplatform.services.organization.impl.GroupImpl";

  private HibernateService         service_;

  private List<GroupEventListener> listeners_;

  public GroupDAOImpl(HibernateService service) {
    service_ = service;
    listeners_ = new ArrayList<GroupEventListener>();
  }

  public void addGroupEventListener(GroupEventListener listener) {
    listeners_.add(listener);
  }

  final public Group createGroupInstance() {
    return new GroupImpl();
  }

  public void createGroup(Group group, boolean broadcast) throws Exception {
    addChild(null, group, broadcast);
  }

  public void addChild(Group parent, Group child, boolean broadcast) throws Exception {
    Session session = service_.openSession();
    String groupId = "/" + child.getGroupName();
    GroupImpl childImpl = (GroupImpl) child;
    if (parent != null) {
      Group parentGroup = (Group) session.get(GroupImpl.class, parent.getId());
      groupId = parentGroup.getId() + "/" + child.getGroupName();
      childImpl.setParentId(parentGroup.getId());
    } else if (child.getId() != null) {
      groupId = child.getId();
    }
    Object o = session.get(GroupImpl.class, groupId);
    if (o != null) {
      Object[] args = { child.getGroupName() };
      throw new UniqueObjectException("OrganizationService.unique-group-exception", args);
    }
    if (broadcast)
      preSave(child, true);
    childImpl.setId(groupId);
    session.save(childImpl);
    if (broadcast)
      postSave(child, true);
    session.flush();
  }

  public void saveGroup(Group group, boolean broadcast) throws Exception {
    Session session = service_.openSession();
    if (broadcast)
      preSave(group, false);
    session.update(group);
    if (broadcast)
      postSave(group, false);
    session.flush();
  }

  public Group removeGroup(Group group, boolean broadcast) throws Exception {
    Session session = service_.openSession();
    if (broadcast)
      preDelete(group);
    session.delete(group);
    List entries = session.createQuery(queryFindGroupByParent).setString(0, group.getId()).list();
    for (int i = 0; i < entries.size(); i++)
      removeGroup((Group) entries.get(i), broadcast);
    MembershipDAOImpl.removeMembershipEntriesOfGroup(group, session);
    if (broadcast)
      postDelete(group);
    session.flush();
    return group;
  }

  static void removeGroupEntry(String groupName, Session session) throws Exception {
    List entries = session.createQuery(queryFindGroupByName).setString(0, groupName).list();
    for (int i = 0; i < entries.size(); i++)
      session.delete(entries.get(i));
  }

  public Collection findGroupByMembership(String userName, String membershipType) throws Exception {
    Session session = service_.openSession();
    Query q = session.createQuery(queryFindGroupByMembership)
                     .setString(0, userName)
                     .setString(1, membershipType);
    List groups = q.list();
    return groups;
  }

  public Group findGroupByName(String groupName) throws Exception {
    Session session = service_.openSession();
    Group group = (Group) service_.findOne(session, queryFindGroupByName, groupName);
    return group;
  }

  public Group findGroupById(String groupId) throws Exception {
    Session session = service_.openSession();
    Group group = (Group) service_.findOne(session, queryFindGroupById, groupId);
    return group;
  }

  public Collection findGroups(Group parent) throws Exception {
    Session session = service_.openSession();
    if (parent == null)
      return session.createQuery(queryFindRootGroups).list();
    // return session.find( queryFindRootGroups );
    // }
    // return session.find( queryFindGroupByParent, parent.getId(),
    // Hibernate.STRING );
    return session.createQuery(queryFindGroupByParent).setString(0, parent.getId()).list();

  }

  public Collection findGroupsOfUser(String user) throws Exception {
    Session session = service_.openSession();
    // return session.find( queryFindGroupsOfUser, user, Hibernate.STRING );
    return session.createQuery(queryFindGroupsOfUser).setString(0, user).list();
  }

  public Collection getAllGroups() throws Exception {
    Session session = service_.openSession();
    Query q = session.createQuery(queryGetAllGroups);
    List groups = q.list();
    return groups;
  }

  private void preSave(Group group, boolean isNew) throws Exception {
    for (GroupEventListener listener : listeners_)
      listener.preSave(group, isNew);
  }

  private void postSave(Group group, boolean isNew) throws Exception {
    for (GroupEventListener listener : listeners_)
      listener.postSave(group, isNew);
  }

  private void preDelete(Group group) throws Exception {
    for (GroupEventListener listener : listeners_)
      listener.preDelete(group);
  }

  private void postDelete(Group group) throws Exception {
    for (GroupEventListener listener : listeners_)
      listener.postDelete(group);
  }

  /**
   * {@inheritDoc}
   */
  public List<GroupEventListener> getGroupListeners() {
    return Collections.unmodifiableList(listeners_);
  }
}
