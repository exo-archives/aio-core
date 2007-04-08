/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBTableManager;
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
                                 ExoDatasource datasource, CacheService cservice) throws Exception {
    userDAO_ = new UserDAOImpl(listenerService, datasource, new UserMapper()) ;   
    userProfileDAO_ =  new UserProfileDAOImpl() ;
    groupDAO_ =  new GroupDAOImpl() ;
    membershipDAO_ = new MembershipDAOImpl() ;
    membershipTypeDAO_ = new MembershipTypeDAOImpl() ;
    
    DBTableManager dbManager = datasource.getDBTableManager() ;
    if(!dbManager.hasTable(UserImpl.class)) dbManager.createTable(UserImpl.class, true) ;
    
  }
  
  static public class UserMapper implements DBObjectMapper<UserImpl> {

    public String[][] toParameters(UserImpl bean) throws Exception {
      java.sql.Date createdDate = new java.sql.Date(bean.getCreatedDate().getTime());
      java.sql.Date lastLogin = new java.sql.Date(bean.getLastLoginTime().getTime());
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
  
  
  static public class GroupMapper implements DBObjectMapper<GroupImpl> {

    public String[][] toParameters(GroupImpl bean) throws Exception {
      return null;
    }

    public void mapUpdate(GroupImpl bean, PreparedStatement pstm) throws Exception {
    }

    public void mapResultSet(ResultSet res, GroupImpl bean) throws Exception {  
    } 
  }
  
  


}