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

package org.exoplatform.services.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

/**
 * Created by The eXo Platform SAS .<br/> User Session encapsulates user's
 * principals such as name, groups along with JAAS subject (useful in J2EE
 * environment) as well as other optional attributes
 * 
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public class Identity {

  private String userId;
  private Collection<MembershipEntry> memberships;
  private Subject subject;
  private Collection<String> roles;

  // private RolesExtractor rolesExtractor;

  public Identity(String userId) {
    this(userId, new HashSet<MembershipEntry>(), new HashSet<String>());
  }

  public Identity(String userId, Collection<MembershipEntry> memberships) {
    this(userId, memberships, new HashSet<String>());
  }

  public Identity(String userId, Collection<MembershipEntry> memberships, Collection<String> roles) {
    this.userId = userId;
    this.memberships = memberships;
    this.roles = roles;
  }

  /**
   * @return user name
   */
  public String getUserId() {
    return userId;
  }

  /**
   * @param group
   * @param membershipType
   * @return true if user has given membershipType for given group, false
   *         otherwise
   */
  public boolean isMemberOf(String group, String membershipType) {
    return containsMembership(new MembershipEntry(group, membershipType));
  }

  public boolean isMemberOf(MembershipEntry me) {
    return containsMembership(me);
  }

  /**
   * @param group
   * @return true if user has any membershipType for given group, false
   *         otherwise
   */
  public boolean isMemberOf(String group) {
    return containsMembership(new MembershipEntry(group));
  }

  /**
   * @return set of groups to which this user belongs to
   */
  public Set<String> getGroups() {
    Set<String> groups = new HashSet<String>();
    for (MembershipEntry m : memberships) {
      groups.add(m.getGroup());
    }
    return groups;
  }

  /**
   * @deprecated for back compatibility
   */
  public void setMemberships(Collection<MembershipEntry> memberships) {
    this.memberships = memberships;
  }

  public Collection<MembershipEntry> getMemberships() {
    return memberships;
  }

//  /**
//   * Sets the roles extractor component for J2EE environment using
//   * 
//   * @param rolesExtractor
//   */
//  public void setRolesExtractor(RolesExtractor rolesExtractor) {
//    this.rolesExtractor = rolesExtractor;
//  }

  /**
   * 
   * @param rolesExtractor
   * @return set of J2EE roles extracted from this user's groups using giving
   *         extraction algorithm
   */
  public Collection<String> getRoles() {
//    if (this.rolesExtractor == null)
//      return new HashSet<String>();
//    return rolesExtractor.extractRoles(getGroups());
    return roles;
  }

  /**
   * @deprecated
   */
  public Subject getSubject() {
    return subject;
  }

  /**
   * @deprecated
   */
  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  private boolean containsMembership(MembershipEntry checkMe) {
    for (MembershipEntry membership : memberships) {
      if (checkMe.equals(membership))
        return true;
      // else
      // if(membership.getMembershipType().equals(MembershipEntry.ANY_TYPE))
      // return membership.getGroup().equals(checkMe.getGroup());
    }
    return false;
  }

}
