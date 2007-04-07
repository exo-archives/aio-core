/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.database.DBObjectMapper;
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
  }
  
  static public class UserMapper implements DBObjectMapper<UserImpl> {

    public String[][] toParameters(UserImpl bean) throws Exception {
      return null;
    }

    public void mapUpdate(UserImpl bean, PreparedStatement pstm) throws Exception {
    }

    public void mapResultSet(ResultSet res, UserImpl bean) throws Exception {  
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