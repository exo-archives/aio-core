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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.ObjectPageList;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBObjectQuery;
import org.exoplatform.services.database.DBPageList;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.database.StandardSQLDAO;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.organization.UserHandler;

/**
 * Created by The eXo Platform SAS
 * Apr 7, 2007  
 */
public class UserDAOImpl extends StandardSQLDAO<UserImpl> implements  UserHandler {
  
  protected static Log log = ExoLogger.getLogger("organization:UserDAOImpl");
  
  protected ListenerService listenerService_;
  
  public UserDAOImpl(ListenerService lService, ExoDatasource datasource, DBObjectMapper<UserImpl> mapper) {
    super(datasource, mapper, UserImpl.class);
    listenerService_ = lService;
  }
  
  public User createUserInstance() { return new UserImpl(); }

  public User createUserInstance(String username) { return new UserImpl(username); }
  
  public void createUser(User user, boolean broadcast) throws Exception {
	if(log.isDebugEnabled())
      log.debug("----------- CREATE USER " + user.getUserName());
    UserImpl userImpl = (UserImpl)user;
    if(broadcast) listenerService_.broadcast(UserHandler.PRE_CREATE_USER_EVENT, this, userImpl);
    super.save(userImpl);
    if(broadcast) listenerService_.broadcast(UserHandler.POST_CREATE_USER_EVENT, this, userImpl);
  }
  
  public boolean authenticate(String username, String password) throws Exception {
    User user = findUserByName(username);   
    if(user == null) return false ;    
    
    boolean authenticated = user.getPassword().equals(password) ;
	if(log.isDebugEnabled())
      log.debug("+++++++++++AUTHENTICATE USERNAME " + username + " AND PASS " + password + " - " + authenticated);
    if(authenticated){
      UserImpl userImpl = (UserImpl)user;
      userImpl.setLastLoginTime(Calendar.getInstance().getTime());      
      saveUser(userImpl, false);
    }
    return authenticated;
  }

  public User findUserByName(String userName) throws Exception {
    DBObjectQuery<UserImpl> query = new DBObjectQuery<UserImpl>(UserImpl.class);
    query.addLIKE("USER_NAME", userName);
    User user = loadUnique(query.toQuery());;
	if(log.isDebugEnabled())
      log.debug("+++++++++++FIND USER BY USER NAME " + userName + " - " + (user!=null));
    return user;
  }

  /**
   * Query(
   *   name = "" ,
   *   standardSQL = "..."
   *   oracleSQL = "..."
   * )
   * 
   */
  public PageList findUsers(org.exoplatform.services.organization.Query orgQuery) throws Exception {
    DBObjectQuery dbQuery = new DBObjectQuery<UserImpl>(UserImpl.class);
    dbQuery.addLIKE("USER_NAME", orgQuery.getUserName()) ;
    dbQuery.addLIKE("FIRST_NAME", orgQuery.getFirstName() ) ;
    dbQuery.addLIKE("LAST_NAME", orgQuery.getLastName()) ;
    dbQuery.addLIKE("EMAIL", orgQuery.getEmail()) ;
    dbQuery.addGT("LAST_LOGIN_TIME", orgQuery.getFromLoginDate()) ;
    dbQuery.addLT("LAST_LOGIN_TIME", orgQuery.getToLoginDate()) ;
    return new DBPageList<UserImpl>(20, this, dbQuery.toQuery(), dbQuery.toCountQuery());
  }

  @SuppressWarnings("unchecked")
  public PageList findUsersByGroup(String groupId) throws Exception {
	if(log.isDebugEnabled())
      log.debug("+++++++++++FIND USER BY GROUP_ID " + groupId);
    PortalContainer manager  = PortalContainer.getInstance();    
    OrganizationService service = (OrganizationService) manager.getComponentInstanceOfType(OrganizationService.class);
    MembershipHandler membershipHandler = service.getMembershipHandler();
    GroupHandler groupHandler = service.getGroupHandler();
    Group group = groupHandler.findGroupById(groupId);
    List<Membership> members = (List<Membership>) membershipHandler.findMembershipsByGroup(group);
    List<User> users = new ArrayList<User>();
    for(Membership member: members){
      User g = findUserByName(member.getUserName());
      if(g!=null) users.add(g);
    }
    return new ObjectPageList(users, 10);
  }

  public PageList getUserPageList(int pageSize) throws Exception {
    return new DBPageList<UserImpl>(pageSize, this, new DBObjectQuery<UserImpl>(UserImpl.class));
  }

  public User removeUser(String userName, boolean broadcast) throws Exception {
    UserImpl userImpl = (UserImpl) findUserByName(userName);
    if(userImpl == null) return null;
    if(broadcast) listenerService_.broadcast(UserHandler.PRE_DELETE_USER_EVENT, this, userImpl);
    super.remove(userImpl);
    if(broadcast) listenerService_.broadcast(UserHandler.POST_DELETE_USER_EVENT, this, userImpl);
    return userImpl;
  }

  public void saveUser(User user, boolean broadcast) throws Exception {
    UserImpl userImpl = (UserImpl)user;
    if(broadcast) listenerService_.broadcast(UserHandler.PRE_UPDATE_USER_EVENT, this, userImpl);
    super.update(userImpl);
    if(broadcast) listenerService_.broadcast(UserHandler.POST_UPDATE_USER_EVENT, this, userImpl);
  }

  @SuppressWarnings("unused")
  public void addUserEventListener(UserEventListener listener) {}

}
