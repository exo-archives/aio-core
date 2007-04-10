/*
 * Created on Feb 3, 2005
 */
package org.exoplatform.services.organization.impl.mock;

import java.util.ArrayList;
import java.util.Collection;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.organization.BaseOrganizationService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupEventListener;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.services.organization.impl.UserImpl;
/**
 * @author benjaminmestrallet
 */
public class DummyOrganizationService extends BaseOrganizationService {
  public DummyOrganizationService() {
    this.userDAO_ = new  UserHandlerImpl() ;
    this.groupDAO_ = new GroupHandlerImpl() ;
  }
  

//  synchronized public void addListener(ComponentPlugin listener) {
//  }
  
//  public void addListener(OrganizationDatabaseInitializer listener) {}


//  public void removeListener(OrganizationDatabaseInitializer listener) {  
//  }

  static public class UserHandlerImpl implements UserHandler {
    public User createUserInstance() {  return new UserImpl(); }
    public User createUserInstance(String username) {  return new UserImpl(username); }
    public void createUser(User user, boolean broadcast) throws Exception {  }
    public void saveUser(User user, boolean broadcast) throws Exception {   }
    public User removeUser(String userName, boolean broadcast) throws Exception { return null; }
    public User findUserByName(String userName) throws Exception {   return null;   }
    public PageList findUsersByGroup(String groupId) throws Exception { return null; }
    public PageList getUserPageList(int pageSize) throws Exception { return null; }
    public PageList findUsers(Query query) throws Exception { return null; }
    public void addUserEventListener(UserEventListener listener) { }

    public boolean authenticate(String username, String password) throws Exception {
      //System.out.println("authenticate: " + username + " " + password);
      if("exo".equals(username) && "exo".equals(password)
        ||"exo1".equals(username) && "exo1".equals(password)
        ||"exo2".equals(username) && "exo2".equals(password)
        ||"admin".equals(username) && "admin".equals(password)
        ||"weblogic".equals(username) && "11111111".equals(password)
        ||"__anonim".equals(username) 
         )
        return true;
      return false;
    }
  }
  
  public static class GroupHandlerImpl implements GroupHandler {
    public Group createGroupInstance() {  return null; }
    public void createGroup(Group group, boolean broadcast) throws Exception { }
    public void addChild(Group parent, Group child, boolean broadcast) throws Exception {}
    public void saveGroup(Group group, boolean broadcast) throws Exception {  }
    public Group removeGroup(Group group, boolean broadcast) throws Exception { return null; }
    public Collection findGroupByMembership(String userName, String membershipType) throws Exception {return null;}
    public Group findGroupById(String groupId) throws Exception {   return null;  }
    public Collection findGroups(Group parent) throws Exception { return null; }
    public void addGroupEventListener(GroupEventListener listener) {  }
    public Collection  getAllGroups() { return null ; }  
 
    public Collection findGroupsOfUser(String user) throws Exception {
      ArrayList groups = new ArrayList(1);
      if(user.startsWith("exo"))
        groups.add(new DummyGroup("/exo", "exo"));
      if(user.startsWith("admin"))
        groups.add(new DummyGroup("/admin", "admin"));
      return groups;
    } 
  }
  
  public static class DummyGroup implements Group{

    private String id  ;
    private String parentId  ;
    private String groupName ;
    private String label ;
    private String desc ;
    

    public DummyGroup(String id, String name) {
      this.groupName = name;
      this.id = id;
    }
    

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    

    public String getParentId() { return parentId ; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    
    public String getGroupName() { return groupName; }
    public void setGroupName(String name) { this.groupName = name; }

    public String getLabel() { return label ; }
    public void   setLabel(String s) { label = s ; }
    
    public String getDescription() { return desc ; }
    public void   setDescription(String s)  { desc = s ; }


    public String toString() {
      return "Group[" + id + "|" + groupName + "]";
    }
  }
}
