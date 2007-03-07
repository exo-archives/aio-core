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
 * 
 * This class  is acted as a sub component of the organization service. It is used to manage the 
 * membership type  and broadcast  the membership type event to all the registered listener in 
 * the organization service. The membership type event can be: new mebership type,  
 * update the membership type  and delete the membership type event. Each event should 
 * have 2 phases:  pre event and post event. The method createMembershipType(..) , 
 * saveMembershipType(..) and removeMembershipType broadcast the event at each phase so 
 * the listeners can handle the event properly
 * 
 * TODO Currently the membership type handler do not support the listener and broadcasting. We should
 * implement this and broadcast  the event properly. 
 */
public interface MembershipTypeHandler {
  /**
   * @return a new object instance that implement the MembershipType interface
   */
  public MembershipType createMembershipTypeInstance();
  /**
   * Use this method to persist a new membership  type. The developer usually should call the method
   * createMembershipTypeInstance, to create a new MembershipType, set the memerbership type data and
   * call this method to persist the membership type.
   * @param mt The new membership type that the developer want to persist
   * @param broadcast Broadcast the event if  the broadcast value is 'true'
   * @return Return the MembershiptType object that contains the updated informations. Note that 
   * the return membership type cannot be the same with the mt as the method can set the created date
   * and modified date automatically.
   * @throws Exception An exception is throwed if the method cannot access the database or a listener 
   * fail to handle the event
   */
  public MembershipType createMembershipType(MembershipType mt, boolean broadcast) throws Exception ;
  /**
   * Use this method to  update an existed MembershipType data. Usually the developer should call 
   * findMembershipType(..)  to get the membership type object and change the data of the membership type
   * and call this  method to  update the data.
   * @param mt The membership type object to update.
   * @param broadcast Broadcast the event to  all the registered listener if the broadcast value is 'true' 
   * @return Return the updated membership  type object.
   * @throws Exception An exception is throwed if  the method cannot access the database or any listener
   * fail to handle the event.
   */
  public MembershipType saveMembershipType(MembershipType mt, boolean broadcast) throws Exception  ;
  /**
   * Use this method to remove a membership type.
   * @param name  the membership type name
   * @param broadcast Broadcast the event to the registered listener if the broadcast value is 'true'
   * @return The membership type object which has been removed from the database
   * @throws Exception An exception is throwed if the method cannot access the database or the membership
   * type is not found in the database or any listener fail  to handle the event.
   */
  public MembershipType removeMembershipType(String name, boolean broadcast) throws Exception ;
  /**
   * Use this method to search  for  a membership type with the specified name. 
   * @param name the name of the membership type.
   * @return null if no membership  type that matched the name or the found membership type.
   * @throws Exception An exception is  throwed if the method cannot access the database or more than
   * one membership type is found.
   */
  public MembershipType findMembershipType(String name) throws Exception ;
  /**
   * Use this method  to get all the membership types in the database
   * @return  A collection of  the membership  type. The collection cannot be null.  If there is
   * no membership type in the database,  the collection should be empty.
   * @throws Exception Ususally an exception is throwed when the method cannot access the database.
   */
  public Collection     findMembershipTypes() throws Exception ;
}
