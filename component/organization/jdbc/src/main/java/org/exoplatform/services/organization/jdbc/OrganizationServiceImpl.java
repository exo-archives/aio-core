/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBTableManager;
import org.exoplatform.services.database.DatabaseService;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.organization.BaseOrganizationService;
import org.picocontainer.Startable;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:51:21 PM
 */
public class OrganizationServiceImpl extends  BaseOrganizationService implements  Startable {

  public OrganizationServiceImpl(ListenerService listenerService,  
                                 DatabaseService dbService) throws Exception {
    ExoDatasource datasource = dbService.getDatasource();  
    userDAO_ = new UserDAOImpl(listenerService, datasource, new UserMapper()) ;   
    groupDAO_ =  new GroupDAOImpl(listenerService, datasource, new GroupMapper()) ;
    membershipTypeDAO_ = new MembershipTypeDAOImpl(listenerService, datasource, new MembershipTypeMapper()) ;

    membershipDAO_ = new MembershipDAOImpl(listenerService, datasource, new MembershipMapper()) ;
    userProfileDAO_ =  new UserProfileDAOImpl(listenerService, datasource, new UserProfileMapper() ) ;
    
    DBTableManager dbManager = datasource.getDBTableManager() ;
    if(!dbManager.hasTable(UserImpl.class)) dbManager.createTable(UserImpl.class, true) ;
    if(!dbManager.hasTable(GroupImpl.class)) dbManager.createTable(GroupImpl.class, true) ;
    if(!dbManager.hasTable(MembershipTypeImpl.class)) dbManager.createTable(MembershipTypeImpl.class, true) ;
    if(!dbManager.hasTable(UserProfileData.class)) dbManager.createTable(UserProfileData.class, true) ;
    if(!dbManager.hasTable(MembershipImpl.class)) dbManager.createTable(MembershipImpl.class, true) ;
  }
  
  static class UserMapper implements DBObjectMapper<UserImpl> {

    public String[][] toParameters(UserImpl bean) throws Exception {
      Date date = bean.getCreatedDate();
      if(date == null) date = Calendar.getInstance().getTime();
      java.sql.Date createdDate = new java.sql.Date(date.getTime());
      
      date = bean.getLastLoginTime();
      if(date == null) date = Calendar.getInstance().getTime();
      java.sql.Date lastLogin = new java.sql.Date(date.getTime());
      return new String[][] {
          {"id", String.valueOf(bean.getDBObjectId()) },
          {"username", bean.getUserName() },
          {"password", bean.getPassword() },
          {"firstname",bean.getFirstName() },
          {"lastname", bean.getLastName() },
          {"email",    bean.getEmail() },
          {"createdDate", createdDate.toString() },
          {"lastLoginTime", lastLogin.toString()},
          {"organizationId", bean.getOrganizationId()}
      };
    }

    public void mapUpdate(UserImpl bean, PreparedStatement statement) throws Exception {
      statement.setString(1, bean.getUserName());
      statement.setString(2, bean.getPassword());
      statement.setString(3, bean.getFirstName());
      statement.setString(4, bean.getLastName());
      statement.setString(5, bean.getEmail());
      
      Date createdDate = bean.getCreatedDate();
      if(createdDate == null) createdDate = Calendar.getInstance().getTime();
      statement.setDate(6, new java.sql.Date(createdDate.getTime()));
      
      Date lastLoginTime = bean.getLastLoginTime();
      if(lastLoginTime == null) lastLoginTime = Calendar.getInstance().getTime();
      statement.setDate(7, new java.sql.Date(lastLoginTime.getTime()));
      
      statement.setString(8, bean.getOrganizationId());
    }

    public void mapResultSet(ResultSet res, UserImpl bean) throws Exception {  
      bean.setDBObjectId(res.getLong("id"));
      bean.setUserName(res.getString("username"));
      bean.setPassword(res.getString("password"));
      bean.setFirstName(res.getString("firstname"));
      bean.setLastName(res.getString("lastname"));
      bean.setEmail(res.getString("email"));
      
      Calendar calendar = Calendar.getInstance();
      res.getDate("createdDate", calendar);
      bean.setCreatedDate(calendar.getTime());
      
      res.getDate("lastLoginTime", calendar);
      bean.setLastLoginTime(calendar.getTime());
      
      bean.setOrganizationId(res.getString("organizationId"));
    } 
  }
  
  
  static class GroupMapper implements DBObjectMapper<GroupImpl> {

    public String[][] toParameters(GroupImpl bean) throws Exception {
      return new String[][] {
          {"groupId", bean.getId() },
          {"parentId",  bean.getParentId()},
          {"groupName",  bean.getGroupName()},
          {"label", bean.getLabel()},
          {"desc", bean.getDescription()}
      };
    }

    public void mapUpdate(GroupImpl bean, PreparedStatement statement) throws Exception {
      statement.setString(1, bean.getId());
      statement.setString(2, bean.getParentId());
      statement.setString(3, bean.getGroupName());
      statement.setString(4, bean.getLabel());
      statement.setString(5, bean.getDescription());
    }

    public void mapResultSet(ResultSet res, GroupImpl bean) throws Exception {
      bean.setDBObjectId(res.getLong("id"));
      bean.setId(res.getString("groupId"));
      bean.setParentId(res.getString("parentId"));
      bean.setGroupName(res.getString("groupName"));
      bean.setLabel(res.getString("label"));
      bean.setDescription(res.getString("desc"));
    } 
  }
  
  static class MembershipTypeMapper implements DBObjectMapper<MembershipTypeImpl> {

    public String[][] toParameters(MembershipTypeImpl bean) throws Exception {
      Date date = bean.getCreatedDate();
      if(date == null) date = Calendar.getInstance().getTime();
      java.sql.Date createdDate = new java.sql.Date(date.getTime());
      
      date = bean.getModifiedDate();
      if(date == null) date = Calendar.getInstance().getTime();
      java.sql.Date modifiedDate = new java.sql.Date(date.getTime());
      return new String[][] {
          {"name", bean.getName() },
          {"owner",  bean.getOwner()},
          {"description",  bean.getDescription()},
          {"createdDate", createdDate.toString() },
          {"lastLoginTime", modifiedDate.toString()}     
      };
    }
    
    public void mapUpdate(MembershipTypeImpl bean, PreparedStatement statement) throws Exception {
      statement.setString(1, bean.getName());
      statement.setString(2, bean.getOwner());
      statement.setString(3, bean.getDescription());
      
      Date createdDate = bean.getCreatedDate();
      if(createdDate == null) createdDate = Calendar.getInstance().getTime();
      statement.setDate(4, new java.sql.Date(createdDate.getTime()));
      
      Date lastLoginTime = bean.getModifiedDate();
      if(lastLoginTime == null) lastLoginTime = Calendar.getInstance().getTime();
      statement.setDate(5, new java.sql.Date(lastLoginTime.getTime()));
    }
    
    public void mapResultSet(ResultSet res, MembershipTypeImpl bean) throws Exception {
      bean.setDBObjectId(res.getLong("id"));
      bean.setName(res.getString("name"));
      bean.setOwner(res.getString("owner"));
      bean.setDescription(res.getString("description"));
      
      Calendar calendar = Calendar.getInstance();
      res.getDate("createdDate", calendar);
      bean.setCreatedDate(calendar.getTime());
      
      res.getDate("modifiedDate", calendar);
      bean.setModifiedDate(calendar.getTime());
    }
  }
  
  static class MembershipMapper implements DBObjectMapper<MembershipImpl> {

    public String[][] toParameters(MembershipImpl bean) throws Exception {
      return new String[][] {
          {"membershipId", bean.getId() },
          {"membershipType",  bean.getMembershipType()},
          {"groupId",  bean.getGroupId()},
          {"userName", bean.getUserName()}
      };
    }
    
    public void mapUpdate(MembershipImpl bean, PreparedStatement statement) throws Exception {
      statement.setString(1, bean.getId());
      statement.setString(2, bean.getMembershipType());
      statement.setString(3, bean.getGroupId());
      statement.setString(4, bean.getUserName());
    }
    
    public void mapResultSet(ResultSet res, MembershipImpl bean) throws Exception {
      bean.setDBObjectId(res.getLong("id"));
      bean.setId(res.getString("membershipId"));
      bean.setMembershipType(res.getString("membershipType"));
      bean.setGroupId(res.getString("groupId"));
      bean.setUserName(res.getString("userName"));
    }
    
  }
  
  static class UserProfileMapper implements DBObjectMapper<UserProfileData> {

    public String[][] toParameters(UserProfileData bean) throws Exception {
      return new String[][] {
          {"userName", bean.getUserName() },
          {"profile",  bean.getProfile()}
      };
    }
    
    public void mapUpdate(UserProfileData bean, PreparedStatement statement) throws Exception {
      statement.setString(1, bean.getUserName());
      statement.setString(2, bean.getProfile());
    }
    
    public void mapResultSet(ResultSet res, UserProfileData bean) throws Exception {
      bean.setDBObjectId(res.getLong("id"));
      bean.setUserName(res.getString("userName"));
      bean.setProfile(res.getString("profile"));
    }
    
  }

}