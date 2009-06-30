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
package org.exoplatform.services.organization.jdbc.listeners;

import org.exoplatform.services.log.Log;

import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.log.ExoLogger;
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
 * Created by The eXo Platform SAS Author : Le Bien Thuy lebienthuy@gmail.com
 * Jun 28, 2007
 */
public class CreateUserListener extends Listener<UserDAOImpl, User> {
  private OrganizationService service_;

  protected static Log        log = ExoLogger.getLogger("organisation:CreateUserListener");

  public CreateUserListener(OrganizationService service) {
    service_ = service;
  }

  public void onEvent(Event<UserDAOImpl, User> event) throws Exception {
    log.info("Create User Profile: " + event.getData().getUserName());
    UserProfile profile = service_.getUserProfileHandler()
                                  .createUserProfileInstance(event.getData().getUserName());
    service_.getUserProfileHandler().saveUserProfile(profile, true);
    GroupHandler groupHandler = service_.getGroupHandler();
    Group g = groupHandler.findGroupById("/user");
    MembershipTypeHandler membershipTypeHandler = service_.getMembershipTypeHandler();
    MembershipType memberType = membershipTypeHandler.findMembershipType("member");

    if (g == null) {
      g = groupHandler.createGroupInstance();
      g.setGroupName("user");
      groupHandler.addChild(null, g, true);
    }
    if (memberType == null) {
      memberType = membershipTypeHandler.createMembershipTypeInstance();
      memberType.setName("member");
      membershipTypeHandler.createMembershipType(memberType, true);
    }
    MembershipHandler membershipHandler = service_.getMembershipHandler();
    membershipHandler.linkMembership(event.getData(), g, memberType, true);
  }
}
