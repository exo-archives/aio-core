/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.exception.UniqueObjectException;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBObjectQuery;
import org.exoplatform.services.database.DBPageList;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.database.StandardSQLDAO;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupEventListener;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.OrganizationService;

/**
 * Created by The eXo Platform SAS
 * Apr 7, 2007  
 */
public class GroupDAOImpl extends StandardSQLDAO<GroupImpl> implements GroupHandler {
  
  protected static Log log = ExoLogger.getLogger("organization:GroupDAOImpl");
  
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
      query.addLIKE("GROUP_ID", parent.getId());
      Group parentGroup = super.loadUnique(connection, query.toQuery());
      groupId = parentGroup.getId() + "/" + child.getGroupName() ;
      childImpl.setParentId(parentGroup.getId()) ;
    }else if(child.getId() != null){
      groupId = child.getId();    
      childImpl.setParentId("/");
    }

    query.getParameters().clear();
    query.addLIKE("GROUP_ID", groupId);
    Group o = super.loadUnique(connection, query.toQuery());   
    if(o != null) {     
      Object[] args = {child.getGroupName()} ;
      throw new UniqueObjectException("OrganizationService.unique-group-exception",  args) ;
    }

    if(broadcast) listenerService_.broadcast("organization.group.preSave", this, childImpl);
    childImpl.setId(groupId);
    if(log.isDebugEnabled())
      log.debug("----------ADD GROUP " + child.getId() + " into Group" + child.getParentId());
    
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
    query.addLIKE("GROUP_ID", groupId);
    Group g = super.loadUnique(query.toQuery());
    if(log.isDebugEnabled())
      log.debug("----------FIND GROUP BY ID: " + groupId + " _ " + (g!=null));
    return g;
  }

  @SuppressWarnings("unchecked")
  public Collection findGroupByMembership(String userName, String membershipType) throws Exception {
    
    if(userName == null || membershipType == null) return null;
    MembershipHandler membershipHandler = getMembershipHandler();
    List<Membership> members = (List<Membership>) membershipHandler.findMembershipsByUser(userName);
    List<Group> groups = new ArrayList<Group>();
    for(Membership member: members){
      if(!member.getMembershipType().equals(membershipType)) continue;
      Group g = findGroupById(member.getGroupId());
      if(g!=null) groups.add(g);
    }
    if(log.isDebugEnabled())
      log.debug("----------FIND GROUP BY USERNAME AND TYPE: " + userName + " - " + membershipType + " - ");
    return groups;  
  }

  public Collection findGroups(Group parent) throws Exception {
    String parentId = "/";
    if(parent != null ) parentId = parent.getId();
    DBObjectQuery<GroupImpl> query = new DBObjectQuery<GroupImpl>(GroupImpl.class);
    query.addLIKE("PARENT_ID", parentId);
    DBPageList<GroupImpl> pageList = new DBPageList<GroupImpl>(20, this, query);
    if(log.isDebugEnabled()) {
      log.debug("----------FIND GROUP BY PARENT: " + parent);
      log.debug(" Size = " + pageList.getAvailable());
    }
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
    if(log.isDebugEnabled())
      log.debug("----------FIND GROUP BY USER: " + user + " - " + (groups!=null));
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
