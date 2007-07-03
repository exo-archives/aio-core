/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.exoplatform.commons.exception.UniqueObjectException;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBObjectQuery;
import org.exoplatform.services.database.DBPageList;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.database.StandardSQLDAO;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupEventListener;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.OrganizationService;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Apr 7, 2007  
 */
public class GroupDAOImpl extends StandardSQLDAO<GroupImpl> implements GroupHandler {
  
  protected ListenerService listenerService_;
  
  public GroupDAOImpl(ListenerService lService, ExoDatasource datasource, DBObjectMapper<GroupImpl> mapper) {
    super(datasource, mapper, GroupImpl.class);
    listenerService_ = lService;
  }
  
  public Group createGroupInstance() { return new GroupImpl(); }
  
  public void createGroup(Group group, boolean broadcast) throws Exception {
    addChild(null, group, broadcast);
  }

  public void addChild(Group parent, Group child, boolean broadcast) throws Exception {
    GroupImpl childImpl = (GroupImpl) child ; 
    String groupId =  "/" + child.getGroupName() ;
    Connection connection = eXoDS_.getConnection();
    childImpl.setParentId("/");
    DBObjectQuery<GroupImpl> query = new DBObjectQuery<GroupImpl>(GroupImpl.class);
    if(parent != null){
      query.addLIKE("groupId", parent.getId());
      Group parentGroup = super.loadUnique(connection, query.toQuery());
      groupId = parentGroup.getId() + "/" + child.getGroupName() ;
      childImpl.setParentId(parentGroup.getId()) ;
    }else if(child.getId() != null){
      groupId = child.getId();    
      childImpl.setParentId("/");
    }

    query.getParameters().clear();
    query.addLIKE("groupId", groupId);
    Group o = super.loadUnique(connection, query.toQuery());   
    if(o != null) {     
      Object[] args = {child.getGroupName()} ;
      throw new UniqueObjectException("OrganizationService.unique-group-exception",  args) ;
    }

    if(broadcast) listenerService_.broadcast("organization.group.preSave", this, childImpl);
    childImpl.setId(groupId);
    try {
      if(childImpl.getDBObjectId() == -1) {
        childImpl.setDBObjectId(eXoDS_.getIDGenerator().generateLongId(childImpl));
      }      
      long id = childImpl.getDBObjectId();
      execute(connection, eXoDS_.getQueryBuilder().createInsertQuery(type_, id), childImpl);
      if(broadcast) listenerService_.broadcast("organization.group.postSave", this, childImpl);
    } catch (Exception e) {
      throw e;
    } finally {
      eXoDS_.closeConnection(connection) ;
    }
  }

  public Group findGroupById(String groupId) throws Exception {
    DBObjectQuery<GroupImpl> query = new DBObjectQuery<GroupImpl>(GroupImpl.class);
    query.addLIKE("groupId", groupId);
    return super.loadUnique(query.toQuery());
  }

  @SuppressWarnings("unchecked")
  public Collection findGroupByMembership(String userName, String membershipType) throws Exception {
    MembershipHandler membershipHandler = getMembershipHandler();
    List<Membership> members = (List<Membership>) membershipHandler.findMembershipsByUser(userName);
    List<Group> groups = new ArrayList<Group>();
    for(Membership member: members){
      if(!member.getMembershipType().equals(membershipType)) continue;
      Group g = findGroupById(member.getGroupId());
      if(g!=null) groups.add(g);
    }
    return groups;  
  }

  public Collection findGroups(Group parent) throws Exception {
    if(parent == null ) return null;
    DBObjectQuery<GroupImpl> query = new DBObjectQuery<GroupImpl>(GroupImpl.class);
    query.addLIKE("parentId", parent.getId());
    DBPageList<GroupImpl> pageList = new DBPageList<GroupImpl>(20, this, query);
    return pageList.getAll();
  }

  @SuppressWarnings("unchecked")
  public Collection findGroupsOfUser(String user) throws Exception {
    MembershipHandler membershipHandler = getMembershipHandler();
    List<Membership> members = (List<Membership>) membershipHandler.findMembershipsByUser(user);
    List<Group> groups = new ArrayList<Group>();
    for(Membership member: members){
      Group g = findGroupById(member.getGroupId());
      if(g!=null && !hasGroup(groups, g)) groups.add(g);
    }
    return groups;
  }
  
  private boolean hasGroup(List <Group>list, Group g) {
    for(Group ele: list) {
      if(ele.getId().endsWith(g.getId())) return true;
    }
    return false;
  }

  public Collection getAllGroups() throws Exception {
    DBObjectQuery<GroupImpl> query = new DBObjectQuery<GroupImpl>(GroupImpl.class);
    DBPageList<GroupImpl> pageList = new DBPageList<GroupImpl>(20, this, query);
    return pageList.getAll();
  }
  
  public void saveGroup(Group group, boolean broadcast) throws Exception {
    GroupImpl groupImpl = (GroupImpl)group;
    if(broadcast) listenerService_.broadcast(GroupHandler.PRE_UPDATE_GROUP_EVENT, this, groupImpl);
    super.update(groupImpl);
    if(broadcast) listenerService_.broadcast(GroupHandler.POST_UPDATE_GROUP_EVENT, this, groupImpl);
  }
  
  public Group removeGroup(Group group, boolean broadcast) throws Exception {
    GroupImpl groupImpl = (GroupImpl)group;
    if(broadcast) listenerService_.broadcast(GroupHandler.PRE_DELETE_GROUP_EVENT, this, groupImpl);
    super.remove(groupImpl);
    if(broadcast) listenerService_.broadcast(GroupHandler.POST_DELETE_GROUP_EVENT, this, groupImpl);
    return group;
  }

  @SuppressWarnings("unused")
  public void addGroupEventListener(GroupEventListener listener) { }
  
  private MembershipHandler getMembershipHandler(){
    PortalContainer manager  = PortalContainer.getInstance();    
    OrganizationService service = (OrganizationService) manager.getComponentInstanceOfType(OrganizationService.class);
    return service.getMembershipHandler();
  }

}
