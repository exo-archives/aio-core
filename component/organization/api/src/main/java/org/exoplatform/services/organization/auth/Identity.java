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
package org.exoplatform.services.organization.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan.nguyen@exoplatform.com May 17, 2007
 */
public class Identity {
  private String                   sessionId_;

  private String                   username_;

  private Subject                  subject_;

  /**
   * Store user memberships as a Map where keys are membership types and values
   * are the list of groupIds for that membership type
   */
  private Map<String, Set<String>> groupsByMembershipMap_;

  public Identity(String sessionId, String username, String password) {
    this(sessionId, username, new Subject());
  }

  public Identity(String sessionId, String username, Subject subject) {
    sessionId_ = sessionId;
    username_ = username;
    subject_ = subject;
  }

  public String getSessionId() {
    return sessionId_;
  }

  public String getUsername() {
    return username_;
  }

  public Subject getSubject() {
    return subject_;
  }

  /**
   * Check if user has a membership for given group.
   * 
   * @param groupId id of the group to check
   * @return true if
   */
  public boolean isInGroup(String groupId) {
    Collection<Set<String>> values = groupsByMembershipMap_.values();
    for (Set<String> groups : values) {
      if (groups.contains(groupId)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if user has a given membership
   * 
   * @param membershipType type of the membership.
   * @param groupId id of the group of the membership.
   * @return true if a membership match is found.
   */
  public boolean hasMembership(String membershipType, String groupId) {
    Set<String> groupsForMembership = groupsByMembershipMap_.get(membershipType);
    if (groupsForMembership == null) {
      return false;
    }
    return (groupsForMembership.contains(groupId));
  }

  /**
   * Set memberships for this user.
   * 
   * @param memberships
   */
  public void setMemberships(Collection<Membership> memberships) {
    groupsByMembershipMap_ = new HashMap<String, Set<String>>();
    for (Membership membership : memberships) {
      String membershipType = membership.getMembershipType();
      Set<String> groupsForMembership = groupsByMembershipMap_.get(membershipType);
      if (groupsForMembership == null) {
        groupsForMembership = new HashSet<String>();
        groupsByMembershipMap_.put(membershipType, groupsForMembership);
      }
      groupsForMembership.add(membership.getGroupId());

    }
  }



}
