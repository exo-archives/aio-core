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
/**
 * Created by The eXo Platform SAS
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:51:21 PM
 */
public class OrganizationServiceImpl extends  BaseOrganizationService {

  public OrganizationServiceImpl(ListenerService listenerService,  
                                 DatabaseService dbService) throws Exception {
    ExoDatasource datasource = dbService.getDatasource();  
    userDAO_ = new UserDAOImpl(listenerService, datasource, new UserMapper()) ;   
    groupDAO_ =  new GroupDAOImpl(listenerService, datasource, new GroupMapper()) ;
    membershipTypeDAO_ = new MembershipTypeDAOImpl(listenerService, datasource, new MembershipTypeMapper()) ;

    membershipDAO_ = new MembershipDAOImpl(listenerService, datasource, new MembershipMapper()) ;
    userProfileDAO_ =  new UserProfileDAOImpl(listenerService, datasource, new UserProfileMapper() ) ;
    
    DBTableManager dbManager = datasource.getDBTableManager() ;
//    try{
      if(!dbManager.hasTable(UserImpl.class))
        dbManager.createTable(UserImpl.class, false) ;
//      int k = 3/0;
//    } catch(Exception e) {
//      e.printStackTrace();
//    }
   
    if(!dbManager.hasTable(GroupImpl.class)) 
      dbManager.createTable(GroupImpl.class, false) ;
    if(!dbManager.hasTable(MembershipTypeImpl.class)) 
      dbManager.createTable(MembershipTypeImpl.class, false) ;
    if(!dbManager.hasTable(UserProfileData.class)) 
      dbManager.createTable(UserProfileData.class, false) ;
    if(!dbManager.hasTable(MembershipImpl.class))
      dbManager.createTable(MembershipImpl.class, false) ;
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
          {"ID", String.valueOf(bean.getDBObjectId()) },
          {"USER_NAME", bean.getUserName() },
          {"PASSWORD", bean.getPassword() },
          {"FIRST_NAME",bean.getFirstName() },
          {"LAST_NAME", bean.getLastName() },
          {"EMAIL",    bean.getEmail() },
          {"CREATED_DATE", createdDate.toString() },
          {"LAST_LOGIN_TIME", lastLogin.toString()},
          {"ORGANIZATION_ID", bean.getOrganizationId()}
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
      bean.setDBObjectId(res.getLong("ID"));
      bean.setUserName(res.getString("USER_NAME"));
      bean.setPassword(res.getString("PASSWORD"));
      bean.setFirstName(res.getString("FIRST_NAME"));
      bean.setLastName(res.getString("LAST_NAME"));
      bean.setEmail(res.getString("EMAIL"));
      
      Calendar calendar = Calendar.getInstance();
      res.getDate("CREATED_DATE", calendar);
      bean.setCreatedDate(calendar.getTime());
      
      res.getDate("LAST_LOGIN_TIME", calendar);
      bean.setLastLoginTime(calendar.getTime());
      
      bean.setOrganizationId(res.getString("ORGANIZATION_ID"));
    } 
  }
  
  
  static class GroupMapper implements DBObjectMapper<GroupImpl> {

    public String[][] toParameters(GroupImpl bean) throws Exception {
      return new String[][] {
          {"GROUP_ID", bean.getId() },
          {"PARENT_ID",  bean.getParentId()},
          {"GROUP_NAME",  bean.getGroupName()},
          {"LABEL", bean.getLabel()},
          {"GROUP_DESC", bean.getDescription()}
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
      bean.setDBObjectId(res.getLong("ID"));
      bean.setId(res.getString("GROUP_ID"));
      bean.setParentId(res.getString("PARENT_ID"));
      bean.setGroupName(res.getString("GROUP_NAME"));
      bean.setLabel(res.getString("LABEL"));
      bean.setDescription(res.getString("GROUP_DESC"));
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
          {"MT_NAME", bean.getName() },
          {"MT_OWNER",  bean.getOwner()},
          {"MT_DESCRIPTION",  bean.getDescription()},
          {"CREATED_DATE", createdDate.toString() },
          {"LAST_LOGIN_TIME", modifiedDate.toString()}     
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
      bean.setDBObjectId(res.getLong("ID"));
      bean.setName(res.getString("MT_NAME"));
      bean.setOwner(res.getString("MT_OWNER"));
      bean.setDescription(res.getString("MT_DESCRIPTION"));
      
      Calendar calendar = Calendar.getInstance();
      res.getDate("CREATED_DATE", calendar);
      bean.setCreatedDate(calendar.getTime());
      
      res.getDate("MODIFIED_DATE", calendar);
      bean.setModifiedDate(calendar.getTime());
    }
  }
  
  static class MembershipMapper implements DBObjectMapper<MembershipImpl> {

    public String[][] toParameters(MembershipImpl bean) throws Exception {
      return new String[][] {
          {"MEMBERSHIP_ID", bean.getId() },
          {"MEMBERSHIP_TYPE",  bean.getMembershipType()},
          {"GROUP_ID",  bean.getGroupId()},
          {"USER_NAME", bean.getUserName()}
      };
    }
    
    public void mapUpdate(MembershipImpl bean, PreparedStatement statement) throws Exception {
      statement.setString(1, bean.getId());
      statement.setString(2, bean.getMembershipType());
      statement.setString(3, bean.getGroupId());
      statement.setString(4, bean.getUserName());
    }
    
    public void mapResultSet(ResultSet res, MembershipImpl bean) throws Exception {
      bean.setDBObjectId(res.getLong("ID"));
      bean.setId(res.getString("MEMBERSHIP_ID"));
      bean.setMembershipType(res.getString("MEMBERSHIP_TYPE"));
      bean.setGroupId(res.getString("GROUP_ID"));
      bean.setUserName(res.getString("USER_NAME"));
    }
    
  }
  
  static class UserProfileMapper implements DBObjectMapper<UserProfileData> {

    public String[][] toParameters(UserProfileData bean) throws Exception {
      return new String[][] {
          {"USER_NAME", bean.getUserName() },
          {"PROFILE",  bean.getProfile()}
      };
    }
    
    public void mapUpdate(UserProfileData bean, PreparedStatement statement) throws Exception {
      statement.setString(1, bean.getUserName());
      statement.setString(2, bean.getProfile());
    }
    
    public void mapResultSet(ResultSet res, UserProfileData bean) throws Exception {
      bean.setDBObjectId(res.getLong("ID"));
      bean.setUserName(res.getString("USER_NAME"));
      bean.setProfile(res.getString("PROFILE"));
    }
    
  }

}