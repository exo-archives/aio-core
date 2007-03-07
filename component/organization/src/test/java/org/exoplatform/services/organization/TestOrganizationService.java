/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization;

import java.util.Collection;
import java.util.List;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.organization.impl.GroupImpl;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SARL
 * Author : Hoa  Pham
 *          hoapham@exoplatform.com,phamvuxuanhoa@yahoo.com
 * Oct 27, 2005
 */

public class TestOrganizationService extends BasicTestCase {
  
  static String Group1 = "Group1" ;
  static String Group2 = "Group2" ;
  static String Benj = "Benj";
  static String Tuan = "Tuan" ;
  
  OrganizationService service_;
  UserHandler userHandler_ ;
  UserProfileHandler profileHandler_ ;
  GroupHandler groupHandler_  ;
  MembershipTypeHandler mtHandler_ ;
  MembershipHandler membershipHandler_ ;
  
  boolean  runtest = true; 
  
  public TestOrganizationService(String s) {
    super(s);
  }

  public void setUp() throws Exception {
    if(!runtest)  return ;
    PortalContainer manager  = PortalContainer.getInstance();    
    service_ = (OrganizationService) manager.getComponentInstanceOfType(OrganizationService.class);
    LogUtil.setLevel("org.exoplatform.services.organization", LogService.DEBUG, true) ;
    LogUtil.setLevel("org.exoplatform.services.database", LogService.DEBUG, true) ;
    userHandler_ = service_.getUserHandler() ; 
    profileHandler_ =  service_.getUserProfileHandler() ;
    groupHandler_  =  service_.getGroupHandler() ;
    mtHandler_ = service_.getMembershipTypeHandler() ;
    membershipHandler_ = service_.getMembershipHandler()  ;
  }
  
  public void tearDown() throws Exception {
    if(!runtest)  return ;
    System.err.println("##############################################################") ;
  }
  
  protected String getDescription() {
    if(!runtest)  return "" ;
    return "test hibernate organization service";
  }
  
  public void testUserPageSize() throws Exception{    
    runtest = false;
    if(!runtest)  return ;
    /* Create an user with UserName: test*/
    String USER = "test" ;
    int s = 15;
    
    try{
      for( int i=0; i<s; i++)
        userHandler_.removeUser(USER+"_"+String.valueOf(i), true);
    }catch( Exception exp){}
    
    for( int i=0; i<s; i++) createUser(USER+"_"+String.valueOf(i)) ; 
    Query query = new Query() ;
    PageList users =  userHandler_.findUsers(query) ;    
    System.out.println("\n\n\n\n\n\n size: " + users.getAvailablePage()) ;
   
    List list  = users.getPage( 1);
    for( Object ele : list){
      User u = (User)ele;
      System.out.println( u.getUserName() + " and "+u.getEmail());
    }
    System.out.println("\n\n\n\n page 2:");
    list  = users.getPage( 4);
    System.out.println("size : "+list.size());
    for( Object ele : list){
      User u = (User)ele;
      System.out.println( u.getUserName() + " and "+u.getEmail());
    }    
    System.out.println("\n\n\n\n");    
  }
  
  
  public void testUser() throws Exception {    
    if(!runtest)  return ;
    /* Create an user with UserName: test*/
    String USER = "test" ;   
    
    createUser(USER) ;    
        
    User u = userHandler_.findUserByName(USER);    
    assertTrue("Found user instance", u != null);
    assertEquals("Expect user name is: ", USER, u.getUserName());   
    
    UserProfile up = profileHandler_.findUserProfileByName(USER);
    assertTrue("Expect user profile is found: ", up != null);
    
    Query query = new Query() ;
    PageList users =  userHandler_.findUsers(query) ; 
    assertTrue("Expect 1 user found ", users.getAvailable() >=  1);
    System.out.println("AVAILABLE PUSERS: " + users.getAvailable()) ;
   
    /* Update user's information */
    u.setFirstName("Exo(Update)");
    userHandler_.saveUser(u, false);
    up.getUserInfoMap().put("user.gender", "male");
    profileHandler_.saveUserProfile(up, true);
    up = profileHandler_.findUserProfileByName(USER);
    assertEquals("expect first name is", "Exo(Update)", u.getFirstName());
    assertEquals("Expect profile is updated: user.gender is ", "male", up.getUserInfoMap().get("user.gender"));
   
    PageList piterator = userHandler_.getUserPageList(10) ;
    assertTrue (piterator.currentPage().size() == 2) ;
    
    /* Remove a user:  
     * Expect result: user and it's profile will be removed 
     */
    userHandler_.removeUser(USER, true);
    assertEquals("User: USER is removed: ", null, userHandler_.findUserByName(USER)); 
    assertTrue(" user's profile of USER was removed:",profileHandler_.findUserProfileByName(USER)==null) ;
  }

  public void testGroup() throws Exception {    
    if(!runtest)  return ;   
    /* Create a parent group with name is: GroupParent */
    String parentName = "GroupParent" ;
    Group groupParent = groupHandler_.createGroupInstance() ;
    groupParent.setGroupName(parentName);
    groupParent.setDescription("This is description");
    groupHandler_.createGroup( groupParent, true);    
    assertTrue(((GroupImpl)groupParent).getId() != null);
    groupParent = groupHandler_.findGroupById( groupParent.getId());     
    assertEquals( groupParent.getGroupName(), "GroupParent");  
    
    /* Create a child group with name: Group1 */
    Group groupChild = groupHandler_.createGroupInstance() ;
    groupChild.setGroupName( Group1);
    groupHandler_.addChild( groupParent, groupChild, true);   
    groupChild = groupHandler_.findGroupById( groupChild.getId());   
    assertEquals(groupChild.getParentId(), groupParent.getId()); 
    assertEquals("Expect group child's name is: ", Group1, groupChild.getGroupName()) ;
    
    /* Update groupChild's information */
    groupChild.setLabel("GroupRenamed");
    groupChild.setDescription("new description ");
    groupHandler_.saveGroup(groupChild, true);    
    assertEquals( groupHandler_.findGroupById(groupChild.getId()).getLabel(),"GroupRenamed");
    
    /* Create a group child with name is: Group2*/
    groupChild = groupHandler_.createGroupInstance() ;
    groupChild.setGroupName( Group2);
    groupHandler_.addChild( groupParent, groupChild, true);   
    groupChild = groupHandler_.findGroupById( groupChild.getId());   
    assertEquals(groupChild.getParentId(), groupParent.getId()); 
    assertEquals("Expect group child's name is: ",Group2, groupChild.getGroupName()) ;
    
    /* find all child group in groupParent 
     * Expect result: 2 child group: group1, group2
     */
    Collection groups = groupHandler_.findGroups( groupParent) ;
    assertEquals("Expect number of child group in parent group is: ",2, groups.size());
    Object arraygroups[] = groups.toArray() ;
    assertEquals("Expect child group's name is: ", Group1, ((Group)arraygroups[0]).getGroupName()) ;
    assertEquals("Expect child group's name is: ", Group2, ((Group)arraygroups[1]).getGroupName()) ;
    
    /* Remove a groupchild */
    groupHandler_.removeGroup(groupHandler_.findGroupById("/"+ parentName +"/"+ Group1),true);
    assertEquals("Expect child group has been removed: ",null,groupHandler_.findGroupById("/"+Group1));
    assertEquals("Expect only 1 child group in parent group", 1, groupHandler_.findGroups(groupParent).size());
   
    /* Remove Parent group, all it's group child  will be removed */
    groupHandler_.removeGroup(groupParent, true) ;
    assertEquals("Expect ParentGroup is removed:",null, groupHandler_.findGroupById( groupParent.getId()));
    assertEquals("Expect all child group is removed: ",0, groupHandler_.findGroups(groupParent).size());
  }
  
  public void testMembershipType() throws Exception {    
    if(!runtest)  return ;
    /* Create a membershipType */
    String testType = "testType" ; 
    MembershipType mt = mtHandler_.createMembershipTypeInstance();
    mt.setName(testType) ;
    mt.setDescription("This is a test") ;
    mt.setOwner("exo") ;    
    mtHandler_.createMembershipType(mt, true);
    assertEquals("Expect mebershiptype is:", testType, mtHandler_.findMembershipType(testType).getName()) ;
    
    /* Update MembershipType's information */
    String desc = "This is a test (update)" ;
    mt.setDescription(desc) ;
    mtHandler_.saveMembershipType(mt, true);
    assertEquals("Expect membershiptype's description", 
                  desc, mtHandler_.findMembershipType(testType).getDescription());
    
    /* create another membershipType */
    mt = mtHandler_.createMembershipTypeInstance() ;
    mt.setName("anothertype") ;
    mt.setOwner("exo") ;
    mtHandler_.createMembershipType(mt, true) ;
    
    /* find all membership type 
     * Expect result: 3 membershipType: "testmembership", "anothertype" 
     *                and "member"(default membership type)
     */
    Collection ms = mtHandler_.findMembershipTypes() ;
    assertEquals("Expect 3 membership in collection: ", 3 , ms.size());

    /* remove "testmembership" */
    mtHandler_.removeMembershipType(testType, true) ;
    assertEquals("Membership type has been removed:", null, mtHandler_.findMembershipType(testType)) ;
    assertEquals("Expect 2 membership in collection(1 is default): ", 2, mtHandler_.findMembershipTypes().size()) ;
    
    /* remove "anothertype" */
    mtHandler_.removeMembershipType("anothertype", true) ;
    assertEquals("Membership type has been removed:", null, mtHandler_.findMembershipType("anothertype")) ;
    assertEquals("Expect 1 membership in collection(default type): ", 1, mtHandler_.findMembershipTypes().size()) ;
    /*All membershipType was removed(except default membership)*/
  }

  public void testMembership() throws Exception {    
    if(!runtest)  return ;
    /* Create 2 user: benj and tuan*/
    User user = createUser(Benj) ;
    User user2 = createUser(Tuan) ;
    
    /* Create "Group1" */
    Group group = groupHandler_.createGroupInstance() ;
    group.setGroupName(Group1);
    groupHandler_.createGroup(group, true);
    /*Create "Group2" */
    group = groupHandler_.createGroupInstance() ;
    group.setGroupName( Group2);
    groupHandler_.createGroup(group, true);
    
    /* Create membership1 and assign Benj to "Group1" with this membership */
    String testType = "testmembership" ;
    MembershipType mt = mtHandler_.createMembershipTypeInstance() ;
    mt.setName( testType) ;
    mtHandler_.createMembershipType(mt, true);
     
    membershipHandler_.linkMembership( user, groupHandler_.findGroupById("/"+Group1), mt, true);     
    membershipHandler_.linkMembership( user, groupHandler_.findGroupById("/"+Group2), mt, true); 
    membershipHandler_.linkMembership( user2, groupHandler_.findGroupById("/"+Group2), mt, true);
    
    mt = mtHandler_.createMembershipTypeInstance() ;
    mt.setName("membershipType2") ;
    mtHandler_.createMembershipType(mt, true);
    membershipHandler_.linkMembership( user, groupHandler_.findGroupById("/"+ Group2), mt, true);    
    
    mt = mtHandler_.createMembershipTypeInstance() ;
    mt.setName( "membershipType3") ;
    Membership membership3 = membershipHandler_.createMembershipInstance();
    membership3.setMembershipType( mt.getName()) ;       
    membershipHandler_.linkMembership( user, groupHandler_.findGroupById("/"+Group2), mt, true);
  
    /* find all memberships in group2
     * Expect result: 4 membership: 3 for Benj(testmebership, membershipType2, membershipType3)
     *                            : 1 for Tuan(testmembership)
     * */
    System.out.println(" --------- find memberships by group -------------");
    Collection<Membership> mems = membershipHandler_.findMembershipsByGroup(groupHandler_.findGroupById("/"+Group2));    
    assertEquals("Expect number of membership in group 2 is: ",4, mems.size()) ;
    
    /* find all memberships in "Group2" relate with Benj
     * Expect result: 3 membership
     */
    System.out.println(" --------- find memberships by user and group--------------");
    mems = membershipHandler_.findMembershipsByUserAndGroup(Benj, "/"+Group2);
    assertEquals("Expect number of membership in "+Group2+" relate with benj is: ", 3, mems.size());
    
    /* find all memberships of Benj in all group
     * Expect result: 5 membership: 3 memberships in "Group2", 1 membership in "Users" (default)
     *                            : 1 membership in "group1"                
     */
    System.out.println(" --------- find memberships by user-------------");
    mems = membershipHandler_.findMembershipsByUser(Benj) ;
    assertEquals("expect membership is: " , 5, mems.size()) ;
    
    /* find memberships of Benj in "Group2" with membership type: testType
     *  Expect result: 1 membership with membershipType is "testType" (testmembership)
     * */
    System.out.println("---------- find membership by User, Group and Type-----------") ;
    Membership membership = 
                 membershipHandler_.findMembershipByUserGroupAndType(Benj, "/"+Group2, testType);
    assertTrue("Expect membership is found:", membership != null);
    assertEquals("Expect membership type is: ", testType, membership.getMembershipType()) ;
    assertEquals("Expect groupId of this membership is: ","/"+Group2 ,membership.getGroupId());
    assertEquals("Expect user of this membership is: ", Benj, membership.getUserName());

    /* find all groups of Benj
     * Expect result: 3 group: "Group1", "Group2" and "user" ("user" is default group)  
     * */
    System.out.println(" --------- find groups by user -------------");
    Collection<Group> groups = groupHandler_.findGroupsOfUser(Benj) ;
    assertEquals("expect group is: " , 3,  groups.size());
      
    /* find all groups has membership type "TYPE" relate with Benj 
     * expect result: 2 group: "Group1" and "Group2"
     */
    System.out.println("---------- find group of a user by membership-----------") ;
    groups = groupHandler_.findGroupByMembership( Benj, testType) ; 
    assertEquals("expect group is: " , 2,  groups.size());
    
    /* remove a membership */
    System.out.println("----------------- removed a membership ---------------------") ;
   String memId = membershipHandler_.findMembershipByUserGroupAndType(Benj,"/"+Group2,
                                                                      "membershipType3").getId() ;
   membershipHandler_.removeMembership( memId, true) ;
   assertTrue("Membership was removed: ",
      membershipHandler_.findMembershipByUserGroupAndType(Benj,"/"+Group2, "membershipType3") == null );
    
   /* remove a user
     * Expect result: all membership related with user will be remove
     * */
     System.out.println("----------------- removed a user----------------------") ;
     userHandler_.removeUser(Tuan, true) ;
     assertTrue("This user was removed", userHandler_.findUserByName(Tuan)== null) ;
     mems = membershipHandler_.findMembershipsByUser(Tuan) ;
     assertTrue ("All membership related with this user was removed:", mems.isEmpty()) ;    
    
   /*  Remove a group
     * Expect result: all membership associate with this group will be removed
     * */
    System.out.println("----------------- removed a group------------") ;
    groupHandler_.removeGroup(groupHandler_.findGroupById("/" + Group1),true) ;
    assertTrue ("This group was removed", groupHandler_.findGroupById("/" + Group1)==null) ;
    
    /*Remove a MembershipType
     * Expect result: All membership have this type will be removed*/
     
    System.out.println("----------------- removed a membershipType------------") ;
    mtHandler_.removeMembershipType(testType, true) ;
    assertTrue("This membershipType was removed: ", mtHandler_.findMembershipType(testType)==null) ;
    //     Check all memberships associate with all groups 
    //     * to guarantee that no membership associate with removed membershipType
    groups = groupHandler_.findGroups(groupHandler_.findGroupById("/")) ;    
    for(Group g: groups) {
      mems = membershipHandler_.findMembershipsByGroup(g);      
      for(Membership m:mems) {
       assertFalse("MembershipType of this membership is not: "+ testType,
                       m.getMembershipType().equalsIgnoreCase(testType)) ;
      }
    }
  } 
  
  public User createUser(String userName) throws Exception {   
    User user = userHandler_.createUserInstance() ;
    user.setUserName(userName) ;
    user.setPassword("default") ;
    user.setFirstName("default") ;
    user.setLastName("default") ;
    user.setEmail("exo@exoportal.org") ;
    userHandler_.createUser(user, true);
    return user ;
  }
}
