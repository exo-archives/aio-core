/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import java.sql.Connection;
import java.util.Collection;

import org.exoplatform.commons.exception.UniqueObjectException;
import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBObjectQuery;
import org.exoplatform.services.database.DBPageList;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.database.StandardSQLDAO;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupEventListener;
import org.exoplatform.services.organization.GroupHandler;

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

    DBObjectQuery<GroupImpl> query = new DBObjectQuery<GroupImpl>(GroupImpl.class);
    if(parent != null){
      query.addLIKE("id", parent.getId());
      Group parentGroup = super.loadUnique(connection, query.toQuery());
      groupId = parentGroup.getId() + "/" + child.getGroupName() ;
      childImpl.setParentId(parentGroup.getId()) ;
    }else if(child.getId() != null){
      groupId = child.getId();    
    }

    query.getParameters().clear();
    query.addLIKE("id", groupId);
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
    query.addLIKE("id", groupId);
    return super.loadUnique(query.toQuery());
  }

  public Collection findGroupByMembership(String userName, String membershipType) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection findGroups(Group parent) throws Exception {
    DBObjectQuery<GroupImpl> query = new DBObjectQuery<GroupImpl>(GroupImpl.class);
    query.addLIKE("parentId", parent.getId());
    DBPageList<GroupImpl> pageList = new DBPageList<GroupImpl>(20, this, query);
    return pageList.getAll();
  }

  public Collection findGroupsOfUser(String user) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection getAllGroups() throws Exception {
    DBObjectQuery<GroupImpl> query = new DBObjectQuery<GroupImpl>(GroupImpl.class);
    DBPageList<GroupImpl> pageList = new DBPageList<GroupImpl>(20, this, query);
    return pageList.getAll();
  }
  
  public void saveGroup(Group group, boolean broadcast) throws Exception {
    GroupImpl groupImpl = (GroupImpl)group;
    if(broadcast) listenerService_.broadcast("organization.group.preUpdate", this, groupImpl);
    super.save(groupImpl);
    if(broadcast) listenerService_.broadcast("organization.group.postUpdate", this, groupImpl);
  }
  
  public Group removeGroup(Group group, boolean broadcast) throws Exception {
    GroupImpl groupImpl = (GroupImpl)group;
    if(broadcast) listenerService_.broadcast("organization.group.preDelete", this, groupImpl);
    super.remove(groupImpl);
    if(broadcast) listenerService_.broadcast("organization.group.postDelete", this, groupImpl);
    return group;
  }

  @SuppressWarnings("unused")
  public void addGroupEventListener(GroupEventListener listener) { }

}
