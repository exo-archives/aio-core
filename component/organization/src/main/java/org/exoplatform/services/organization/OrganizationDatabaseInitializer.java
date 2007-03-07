/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization;

import java.util.List;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.container.xml.InitParams;
/**
 * Jul 20, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: OrganizationDatabaseInitializer.java 13079 2007-03-01 15:30:35Z tuan08 $
 */
public class OrganizationDatabaseInitializer 
extends BaseComponentPlugin implements OrganizationServiceInitializer, ComponentPlugin {
  
  private OrganizationConfig config_ ;
  
  private static int CHECK_EMPTY = 0, CHECK_ENTRY = 1;
  
  private int checkDatabaseAlgorithm_ = CHECK_EMPTY;
  private boolean  printInfo_ =   true ; 
  
  public OrganizationDatabaseInitializer(InitParams params) throws Exception {
    String checkConfig = params.getValueParam("checkDatabaseAlgorithm").getValue();    
    if(checkConfig.trim().equalsIgnoreCase("entry")) {
      checkDatabaseAlgorithm_ = CHECK_ENTRY; 
    } else {
      checkDatabaseAlgorithm_ = CHECK_EMPTY;   
    }
    String printInfoConfig = params.getValueParam("printInformation").getValue();
    if(printInfoConfig.trim().equalsIgnoreCase("true")) printInfo_ = true; else printInfo_ = false;
    config_ = (OrganizationConfig) params.getObjectParamValues(OrganizationConfig.class).get(0);     
  }
  
  public void init(OrganizationService service) throws Exception {
    if(checkDatabaseAlgorithm_ == CHECK_EMPTY && checkExistDatabase(service)) {
      return;
    }
    String alg = "check empty database" ;
    if(checkDatabaseAlgorithm_ == CHECK_ENTRY) alg = "check entry database" ;
    printInfo("=======> Initialize the  organization service data  using algorithm " + alg) ;
    createGroups(service) ;    
    createMembershipTypes(service) ;
    createUsers(service) ;  
    printInfo("<=======") ;
  }
  
  private boolean checkExistDatabase(OrganizationService service) throws Exception {
    PageList users = service.getUserHandler().getUserPageList(10);  
    if(users != null && users.getAvailable() > 0) return true;    
    return false;
  }
  
  
  private void createGroups(OrganizationService orgService) throws Exception {
    printInfo("  Init  Group Data") ;
    List groups = config_.getGroup() ;
    for(int i = 0 ; i < groups.size() ; i++) {
      OrganizationConfig.Group data = (OrganizationConfig.Group) groups.get(i);
      String  groupId = null ;
      String parentId = data.getParentId() ;
      if(parentId == null || parentId.length() == 0)  groupId = "/" + data.getName() ;
      else  groupId = data.getParentId() + "/" + data.getName() ;
      
      if(orgService.getGroupHandler().findGroupById(groupId) == null) {
        Group group = orgService.getGroupHandler().createGroupInstance();      
        group.setGroupName(data.getName());
        group.setDescription(data.getDescription()) ;
        if(parentId == null || parentId.length() == 0){     
          orgService.getGroupHandler().addChild(null, group, true) ;
        } else {
          Group parentGroup =  orgService.getGroupHandler().findGroupById(parentId) ;
          orgService.getGroupHandler().addChild(parentGroup, group, true) ;
        }
        printInfo("    Create Group " + groupId ) ;
      } else {
        printInfo("    Group " + groupId + " is existed, ignore the entry") ; 
      }
    }
  }
  
  private void createMembershipTypes(OrganizationService service) throws Exception {
    printInfo("  Init  Membership Type  Data") ;
    List types = config_.getMembershipType() ;
    for(int i = 0 ; i < types.size() ; i++) {
      OrganizationConfig.MembershipType data = (OrganizationConfig.MembershipType) types.get(i);
      if(service.getMembershipTypeHandler().findMembershipType(data.getType()) == null) {
        MembershipType type = service.getMembershipTypeHandler().createMembershipTypeInstance();
        type.setName(data.getType()) ; 
        type.setDescription(data.getDescription());  
        service.getMembershipTypeHandler().createMembershipType(type, true) ;
        printInfo("    Created Membership Type " + data.getType()) ;
      } else {
        printInfo("    Membership Type " + data.getType() + " is existed, ignore the entry") ;
      }
    }
  }
  
  private void createUsers(OrganizationService service) throws Exception {
    printInfo("  Init  User  Data") ;
    List users = config_.getUser() ;
    MembershipHandler mhandler = service.getMembershipHandler() ;
    for(int i = 0 ; i < users.size() ; i++) {
      OrganizationConfig.User data = (OrganizationConfig.User) users.get(i);      
      User user = service.getUserHandler().createUserInstance() ;
      user.setUserName(data.getUserName()) ;
      user.setPassword(data.getPassword()) ;
      user.setFirstName(data.getFirstName()) ;
      user.setLastName(data.getLastName()) ;
      user.setEmail(data.getEmail()) ;
      if(service.getUserHandler().findUserByName(data.getUserName()) == null) {
        service.getUserHandler().createUser(user, true);
        printInfo("    Created user " + data.getUserName()) ;
      }  else {
        printInfo("    User " + data.getUserName() + " is existed, ignore the entry") ;
      }
      
      String groups = data.getGroups();
      String[] entry = groups.split(",") ;      
      for (int j = 0; j < entry.length; j++) {
        String[] temp = entry[j].trim().split(":");        
        String membership = temp[0] ;
        String groupId = temp[1] ;
        if(mhandler.findMembershipByUserGroupAndType(data.getUserName(), groupId, membership) == null) {
          Group group = service.getGroupHandler().findGroupById(groupId); 
          MembershipType mt = service.getMembershipTypeHandler().createMembershipTypeInstance() ;
          mt.setName(membership) ; 
          mhandler.linkMembership(user,group,mt, true) ;
          printInfo("    Created membership " + data.getUserName() + ", " + groupId + ", " + membership) ;
        } else {
          printInfo("    Ignore membership " + data.getUserName() + ", " + groupId + ", " + membership) ;
        }
      }            
      
    }
  }
  
  private  void  printInfo(String message) {
    if(printInfo_) System.out.println(message) ;
  }
}
