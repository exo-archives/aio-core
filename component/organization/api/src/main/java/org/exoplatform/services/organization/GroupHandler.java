/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization;

import java.util.Collection;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 13, 2005
 * This class  is acted as a sub component of the organization service. It is used to manage the group  
 * and broadcast  the group event to all the registered listener in the organization service. 
 * The group event can be: new group event,  update group event and delete  group event. Each event should 
 * have 2 phases:  pre event and post event. The method createGroup(..) , saveGroup(..) and 
 * removeGroup broadcast the event at each phase so the listeners can handle the event properly  
 */
public interface GroupHandler {
  /**
   * @return a new object instance that implement the Group interface
   */
  public Group createGroupInstance();
  /**
   * @deprecated  This method should not be used ,  use the addChild(..)  method and pass the null as
   * the parent if you want to add the group to the  root level.
   */
  public void createGroup(Group group, boolean broadcast) throws Exception ;
  /**
   * Use  this  method to create a new  group. The developer  should  call createGroupInstance() method to
   * create  a group instance, initialize the  group  properties such owner , label.. and then call
   * this method to persist the group. Use  this  method only when  you are  creating  a new group.  If you 
   * want to  update a group  , use the saveGroup(..)  method.
   * @param parent The parent group  of the new  group. use 'null'  if  you want to create the group at 
   * the root level.
   * @param child The group  that  you want to create.
   * @param broadcast Broacast the new group event  to all the registered listener if broadcast is true
   * @throws Exception An exception is throwed if the method  fail  to persist the new  group or 
   * there is already one child group with the same  group name in the database or any registered  
   * listener fail to handle the event. 
   */
  public void addChild(Group parent, Group child, boolean broadcast) throws Exception;
  /**
   * Use this method to  update  the properties of an existed group. Usually  you should  use the method
   * findGroupById(..)  to  find the  group,  use the methods set  to change the data of the  group and then
   * call this method to  persisted the updated information.  You  should not call this method with  the 
   * group instance  you get from the createGroupInstance() 
   * @param group The group object with the updated information.
   * @param broadcast Broadcast the event to all the registered listener if  the broadcast value is true
   * @throws Exception An exception is thorwed  if  the method cannot access the database or any 
   * listener fail to handle the event
   */
  public void saveGroup(Group group, boolean broadcast) throws Exception ;
  /**
   * Use this method to remove a  group from the group database. If the group  has  the 
   * children  group.  The method should not remove the group and throw and exception
   * @param group  The  group  to be removed. The group parameter should be obtained form
   * the findGroupId(..) method. When the groupn is removed, the memberships of the group 
   * should be removed as well.
   * @param broadcast Broadcast the event to  the registered listener if the broadcast value
   * is 'true'
   * @return Return the removed group.
   * @throws Exception An exception is throwed if the method fail to remove the group
   * from the database, the group is not existed in the database, or any listener fail
   * to handle the event.
   * 
   * TODO Currently the implementation simply remove the children  group without  
   * broadcasting the event. We should add the parameter 'recursive' to the parameter list
   * so the third party can have more control. Also  should we broadcast the membership remove event 
   */
  public Group removeGroup(Group group, boolean broadcast) throws Exception ;
  /**
   * Use this method to  find all the groups  of an user with the specified membership type
   * @param userName The user that the method  should search for.
   * @param membershipType The type of the membership. Since an user can have one or more membership
   * in a  group, this parameter is necessary. If the membershipType is null,  it should mean any 
   * membership type.
   * @return A collection of the  found groups
   * @throws Exception An exception is throwed  if  the  method cannot access the database.
   * 
   * TODO currently the implementation should not handle the case  of membershipType is null.  Also
   * we should merge this method with the findGroupsOfUser method. 
   */
  public Collection findGroupByMembership(String userName, String membershipType) throws Exception ;
  /**
   * Use this method to search for a group
   * @param groupId the  id  of the group that you want to search for
   * @return null  if no record matched the group id or  the  found group
   * @throws Exception An exception is  throwed if the method cannot access the database or more 
   * than one group is  found.
   */
  public Group findGroupById(String groupId) throws Exception ;
  /**
   * Use this method to find  all the children group of a  group.
   * @param parent  The group that  you want to search. Use  null if  you want to search 
   * from the root.
   * @return A collection of the children group 
   * @throws Exception An exception is throwed is the method cannot access the database
   */
  public Collection findGroups(Group parent) throws Exception ;
  /**
   * use this method  to  look all the group  that the user has at least one membership.
   * @param user The username of the user
   * @return A collection of the found group. The return collection cannot  be null, but it can be empty
   * if no  group is found.
   * @throws Exception An exception is throwed  if  the method cannot access the database.
   */
  public Collection findGroupsOfUser(String user) throws Exception ;
  /**
   * Use this method  to get all the  groups. But the third party should not use this method
   */
  public Collection getAllGroups() throws Exception;
  
  /**
   * Use this method to register a group event listener
   * @param listener the  group event listener instance. 
   */
  public void addGroupEventListener(GroupEventListener listener) ; 
}