/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc.listeners;

import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.MembershipTypeHandler;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.jdbc.UserDAOImpl;
/**
 * Created by The eXo Platform SARL
 * Author : Le Bien Thuy
 *          lebienthuy@gmail.com
 * Jun 28, 2007  
 */
public class CreateUserListener extends Listener<UserDAOImpl, User> {
  private OrganizationService service_ ;
  
  public CreateUserListener(OrganizationService service) {
    service_ = service ;
  }
  
  public void onEvent(Event<UserDAOImpl, User> event) throws Exception {
//    System.out.println("\n\nCreate User Profile: " + event.getData().getUserName() + "\n\n");
    UserProfile profile = service_.getUserProfileHandler().createUserProfileInstance(event.getData().getUserName());
    service_.getUserProfileHandler().saveUserProfile(profile, true);
    GroupHandler groupHandler = service_.getGroupHandler();
    Group g = groupHandler.findGroupById("/users");
    MembershipTypeHandler membershipTypeHandler = service_.getMembershipTypeHandler();
    MembershipType memberType = membershipTypeHandler.findMembershipType("member");
    
//    if(g == null || memberType == null) return;
    
    if(g == null ) { 
      g = groupHandler.createGroupInstance();
      g.setGroupName("users");
      groupHandler.addChild(null, g, true);
    }
    if(memberType == null) { 
      memberType = membershipTypeHandler.createMembershipTypeInstance();
      memberType.setName("member");
      membershipTypeHandler.createMembershipType(memberType, true);
    }
    MembershipHandler membershipHandler = service_.getMembershipHandler();
    membershipHandler.linkMembership(event.getData(), g, memberType, true);
  }
}
