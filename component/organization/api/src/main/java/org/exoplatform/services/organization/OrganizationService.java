/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization;

import org.exoplatform.container.component.ComponentPlugin;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:46:04 PM
 * 
 * Concept: The eXo platform  organization has  5 main components: user , user profile, group, 
 * membership type and memebership. 
 * <pre>
 * |-----------------|    |---------------|  |---------------|    |---------------|
 * |                 |    |               |  |               |    |               |
 * |                 |    |               |  |               |==| |               | 
 * |      USER       |<=> | USER PROFILE  |  |     GROUP     |  | |MEMBERSHIP TYPE|
 * |                 |    |               |  |               |<=| |               |
 * |                 |    |               |  |               |    |               |
 * |---------------- |    |---------------|  |---------------|    |---------------|
 *              \                                  /            /
 *               \                                /            /
 *                \                              /            /
 *                 \                            /            /
 *                  \                          /            /
 *                   \                        /            /
 *                    \ |----------------------------------|
 *                      |                                  |
 *                      |            MEMBERSHIP            |
 *                      |     The membership hold the      |
 *                      |     relation of the user, group  |
 *                      |     and membership type          |
 *                      |                                  |
 *                      |--------------------------------- |
 * </pre>
 * 
 * The user component contain and manage the basic  information of an user such the username , 
 * password, first name, last name, email..
 * 
 * The user profile component contain and manage the extra user  information such the user personal 
 * information, businese information..  The  third party developers can also add the  information of 
 * an user for thier application use.  
 * 
 * The group component contains and manage a  tree  of the groups.
 * 
 * The membership type contains and manage  a list  of  the predefined membership 
 * 
 * The membership component contains and manage the relation of the user , group and membership type.
 * An user can have one or more membership in a  group, for example: user A can have the 'member' and
 * 'admin' membership in group  /user. An user is  in a  group if he has at least one membership in
 * that group.
 * 
 * This is the main interface of the organization service. From this interface, the developer
 * can access the sub interface UserHandler to manage the user, UserProfile handler to manage
 * the user profile, GroupHandler to manage the  group and the MembershipHandler to manage the user
 * group and memberhip relation.
 */
public interface OrganizationService  {
  /**
   * This method  return an UserHandler object that use to manage  the user  opeation such 
   * create, update ,  detele , find  user.
   * @see UserHandler
   **/
  public  UserHandler  getUserHandler() ;
  
  /**
   * @return a UserProfileHandler object that use to manage  the  information of the user 
   * @see UserProfileHandler
   */
  public UserProfileHandler getUserProfileHandler() ;
  /**
   * @return return an GroupHandler implementation instance.
   * @see GroupHandler
   */
  public GroupHandler  getGroupHandler() ;
  /**
   * @return return a MembershipTypeHandler implementation instance
   * @see MembershipTypeHandler
   */
  public MembershipTypeHandler getMembershipTypeHandler() ;
  /**
   * @return return a MembershipHandler implementation instance
   * @see MembershipHandler
   */
  public MembershipHandler getMembershipHandler()  ;
  /**
   * Use this method to register  an listener to the UserHandler, GroupHandler or 
   * MembershipHandler. The listener must be and instance of @see UserEventistener , 
   * @see GroupEventListener or @see MembershipEventListener.
   * @param listener A customized  listener instance
   * @throws Exception
   */
  public void addListenerPlugin(ComponentPlugin listener) throws Exception ;
}