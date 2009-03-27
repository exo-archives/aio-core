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
package org.exoplatform.services.organization.ldap;

import java.util.Calendar;
import java.util.Date;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

import org.exoplatform.services.ldap.ObjectClassAttribute;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.impl.GroupImpl;
import org.exoplatform.services.organization.impl.MembershipTypeImpl;
import org.exoplatform.services.organization.impl.UserImpl;
import org.exoplatform.services.organization.impl.UserProfileData;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Oct 13, 2005
 */
public class LDAPAttributeMapping {

  public String   userLDAPClasses;

  public String   profileLDAPClasses;

  public String   groupLDAPClasses;

  public String   membershipTypeLDAPClasses;

  public String   membershipLDAPClasses;

  static String[] USER_LDAP_CLASSES;

  static String[] PROFILE_LDAP_CLASSES;

  static String[] GROUP_LDAP_CLASSES;

  static String[] MEMBERSHIPTYPE_LDAP_CLASSES;

  static String[] MEMBERSHIP_LDAP_CLASSES;

  public String   baseURL, groupsURL, membershipTypeURL, userURL, profileURL;

  // TODO remove initialization in major release. It may be not initialized from
  // for AD.
  String          userDNKey      = "CN";

  //TODO remove initialization in major release. Should be initialized from
  // configuration.
  String          groupDNKey     = "OU";
  
  //
  
  String          userUsernameAttr;

  String          userPassword;

  String          userFirstNameAttr;

  String          userLastNameAttr;

  String          userDisplayNameAttr;

  String          userMailAttr;

  String          userObjectClassFilter;

  String          membershipTypeMemberValue;

  String          membershipTypeRoleNameAttr;

  String          membershipTypeNameAttr;

  String          membershipTypeObjectClassFilter;

  String          membershiptypeObjectClass;

  String          groupObjectClass, groupObjectClassFilter;

  String          membershipObjectClass, membershipObjectClassFilter;

  String          ldapCreatedTimeStampAttr, ldapModifiedTimeStampAttr, ldapDescriptionAttr;

  // TODO remove initialization in major release. Should be initialized from
  // configuration.
  String          groupNameAttr  = "ou";

  String          groupLabelAttr = "l";

  /**
   * Create LDAP attributes that represents user in LDAP context.
   * 
   * @param user User
   * @return LDAP Attributes
   */
  public final Attributes userToAttributes(User user) {
    BasicAttributes attrs = new BasicAttributes();
    if (USER_LDAP_CLASSES == null)
      USER_LDAP_CLASSES = userLDAPClasses.split(",");
    attrs.put(new ObjectClassAttribute(USER_LDAP_CLASSES));
    attrs.put(userDNKey, user.getUserName());
    attrs.put(userDisplayNameAttr, user.getFullName());
    attrs.put(userUsernameAttr, user.getUserName());
    attrs.put(userPassword, user.getPassword());
    attrs.put(userLastNameAttr, user.getLastName());
    attrs.put(userFirstNameAttr, user.getFirstName());
    attrs.put(userMailAttr, user.getEmail());
    attrs.put(ldapDescriptionAttr, "Account for " + user.getFullName());
    return attrs;
  }

  /**
   * Create User from LDAP attributes.
   * 
   * @param attrs {@link Attributes}
   * @return User
   */
  public final User attributesToUser(Attributes attrs) {
    if (attrs == null || attrs.size() == 0)
      return null;
    UserImpl user = new UserImpl();
    user.setUserName(getAttributeValueAsString(attrs, userUsernameAttr));
    user.setLastName(getAttributeValueAsString(attrs, userLastNameAttr));
    user.setFirstName(getAttributeValueAsString(attrs, userFirstNameAttr));
    user.setFullName(getAttributeValueAsString(attrs, userDisplayNameAttr));
    user.setEmail(getAttributeValueAsString(attrs, userMailAttr));
    user.setPassword(getAttributeValueAsString(attrs, userPassword));
    user.setCreatedDate(Calendar.getInstance().getTime());
    user.setLastLoginTime(Calendar.getInstance().getTime());
    return user;
  }

  /**
   * Create LDAP attributes that represents group in LDAP context.
   * 
   * @param group Group
   * @return LDAP attributes
   */
  public final Attributes groupToAttributes(Group group) {
    BasicAttributes attrs = new BasicAttributes();
    if (GROUP_LDAP_CLASSES == null)
      GROUP_LDAP_CLASSES = groupLDAPClasses.split(",");
    attrs.put(new ObjectClassAttribute(GROUP_LDAP_CLASSES));
    attrs.put(groupNameAttr, group.getGroupName());
    String desc = group.getDescription();
    if (desc != null && desc.length() > 0)
      attrs.put(ldapDescriptionAttr, desc);
    String lbl = group.getLabel();
    if (lbl != null && lbl.length() > 0)
      attrs.put(groupLabelAttr, lbl);
    return attrs;
  }

  /**
   * Create group from LDAP attributes.
   * 
   * @param attrs {@link Attributes}
   * @return Group
   */
  public final Group attributesToGroup(Attributes attrs) {
    if (attrs == null || attrs.size() == 0)
      return null;
    Group group = new GroupImpl();
    group.setGroupName(getAttributeValueAsString(attrs, groupNameAttr));
    group.setDescription(getAttributeValueAsString(attrs, ldapDescriptionAttr));
    group.setLabel(getAttributeValueAsString(attrs, groupLabelAttr));
    return group;
  }

  /**
   * Create LDAP attributes that represents {@link MembershipType} in LDAP
   * context.
   * 
   * @param mt MemebrshipType
   * @return LDAP attributes
   */
  public final Attributes membershipTypeToAttributes(MembershipType mt) {
    BasicAttributes attrs = new BasicAttributes();
    if (MEMBERSHIPTYPE_LDAP_CLASSES == null)
      MEMBERSHIPTYPE_LDAP_CLASSES = membershipTypeLDAPClasses.split(",");
    attrs.put(new ObjectClassAttribute(MEMBERSHIPTYPE_LDAP_CLASSES));
    attrs.put(membershipTypeNameAttr, mt.getName());
    String desc = mt.getDescription();
    if (desc != null && desc.length() > 0)
      attrs.put(ldapDescriptionAttr, desc);
    return attrs;
  }

  /**
   * Create MembershipType from LDAP attributes.
   * 
   * @param attrs {@link Attributes}
   * @return MemebrshipType
   */
  public final MembershipType attributesToMembershipType(Attributes attrs) {
    if (attrs == null || attrs.size() == 0)
      return null;
    MembershipType m = new MembershipTypeImpl();
    m.setName(getAttributeValueAsString(attrs, membershipTypeNameAttr));
    m.setDescription(getAttributeValueAsString(attrs, ldapDescriptionAttr));
    m.setCreatedDate(new Date());
    m.setModifiedDate(new Date());
    return m;
  }

  /**
   * Create LDAP attributes that represents user Membership in LDAP context.
   * 
   * @param m Membership
   * @param userDN user Distinguished Name
   * @return DAP attributes
   */
  public final Attributes membershipToAttributes(Membership m, String userDN) {
    BasicAttributes attrs = new BasicAttributes();
    if (MEMBERSHIP_LDAP_CLASSES == null)
      MEMBERSHIP_LDAP_CLASSES = membershipLDAPClasses.split(",");
    attrs.put(new ObjectClassAttribute(MEMBERSHIP_LDAP_CLASSES));
    attrs.put(membershipTypeRoleNameAttr, m.getMembershipType());
    attrs.put(membershipTypeMemberValue, userDN);
    return attrs;
  }

  /**
   * Create LDAP attributes that represents UserProfile in LDAP context.
   * 
   * @param profile UserProfile
   * @return LDAP attributes.
   */
  public final Attributes profileToAttributes(UserProfile profile) {
    BasicAttributes attrs = new BasicAttributes();
    if (PROFILE_LDAP_CLASSES == null)
      PROFILE_LDAP_CLASSES = profileLDAPClasses.split(",");
    attrs.put(new ObjectClassAttribute(PROFILE_LDAP_CLASSES));
    // TODO http://jira.exoplatform.org/browse/COR-49
    // Comment: at the time profiles are stored in bd.
    // 27.03.2009
    attrs.put("sn", profile.getUserName());
    UserProfileData upd = new UserProfileData();
    upd.setUserProfile(profile);
    attrs.put(ldapDescriptionAttr, upd.getProfile());
    return attrs;
  }

  /**
   * Create UserProfileData from LDAP attributes.
   * 
   * @param attrs {@link Attributes}
   * @return {@link UserProfileData}
   */
  public final UserProfileData attributesToProfile(Attributes attrs) {
    if (attrs == null || attrs.size() == 0)
      return null;
    UserProfileData upd = new UserProfileData();
    upd.setProfile(getAttributeValueAsString(attrs, ldapDescriptionAttr));
    return upd;
  }

  /**
   * Get LDAP attribute as String with specified name from {@link Attributes}.
   * 
   * @param attributes {@link Attributes}
   * @param name attribute name
   * @return attribute as string
   */
  public final String getAttributeValueAsString(Attributes attributes, String name) {
    if (attributes == null)
      return "";
    Attribute attr = attributes.get(name);
    if (attr == null)
      return "";
    try {
      Object obj = attr.get();
      if (obj instanceof byte[])
        return new String((byte[]) obj);
      return (String) obj;
    } catch (Exception e) {
      return "";
    }
  }
}
