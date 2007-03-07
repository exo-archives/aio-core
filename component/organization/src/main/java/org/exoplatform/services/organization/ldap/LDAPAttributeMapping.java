/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
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
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 13, 2005
 */
public class LDAPAttributeMapping {
  
   String userLDAPClasses ;
   String profileLDAPClasses ;
   String groupLDAPClasses ;
   String membershipTypeLDAPClasses;
   String membershipLDAPClasses;
  
   static String[] USER_LDAP_CLASSES ;
   static String[] PROFILE_LDAP_CLASSES;
   static String[] GROUP_LDAP_CLASSES;
   static String[] MEMBERSHIPTYPE_LDAP_CLASSES;
   static String[] MEMBERSHIP_LDAP_CLASSES;  
  
   String baseURL, groupsURL, membershipTypeURL, userURL, profileURL  ;
  
   String userDNKey;
   
   String userAuthenticationAttr;
   String userUsernameAttr;
   String userPassword;
   String userFirstNameAttr;
   String userLastNameAttr;
   String userDisplayNameAttr;
   String userMailAttr;
   String userObjectClassFilter;
  
   String membershipTypeMemberValue;
   String membershipTypeRoleNameAttr ;
   String membershipTypeNameAttr;
   String membershipTypeObjectClassFilter;
   String membershiptypeObjectClass;
  
   String groupObjectClass, groupObjectClassFilter;
  
   String membershipObjectClass, membershipObjectClassFilter;
  
   String ldapCreatedTimeStampAttr, ldapModifiedTimeStampAttr, ldapDescriptionAttr;
  
  final public Attributes userToAttributes(User user) {
    BasicAttributes attrs = new BasicAttributes();
    if(USER_LDAP_CLASSES == null)  USER_LDAP_CLASSES = userLDAPClasses.split(","); 
    attrs.put(new ObjectClassAttribute(USER_LDAP_CLASSES));
    attrs.put("cn", user.getUserName());
    attrs.put(userDisplayNameAttr, user.getFullName());
    attrs.put(userUsernameAttr, user.getUserName());    
    attrs.put(userPassword, user.getPassword());
    attrs.put(userLastNameAttr, user.getLastName());
    attrs.put(userFirstNameAttr, user.getFirstName());
    attrs.put(userMailAttr, user.getEmail());
    attrs.put(ldapDescriptionAttr, "Account for "+user.getFullName());   
    return attrs;
  }
  
  final public User attributesToUser(Attributes attrs) throws Exception {
    if(attrs == null || attrs.size() == 0)  return null ;
    UserImpl user = new UserImpl() ;
    user.setUserName(getAttributeValueAsString(attrs, userUsernameAttr)) ;
    user.setLastName(getAttributeValueAsString(attrs, userLastNameAttr)) ;
    user.setFirstName(getAttributeValueAsString(attrs, userFirstNameAttr)) ;
    user.setFullName(getAttributeValueAsString(attrs, userDisplayNameAttr)) ;
    user.setEmail(getAttributeValueAsString(attrs, userMailAttr)) ;    
    user.setPassword(getAttributeValueAsString(attrs, userPassword)) ;  
    user.setCreatedDate(Calendar.getInstance().getTime());
    user.setLastLoginTime(Calendar.getInstance().getTime());
    return user ;
  }  
  
  final public Attributes groupToAttributes(Group group) {
    BasicAttributes attrs = new BasicAttributes();
    if(GROUP_LDAP_CLASSES == null) GROUP_LDAP_CLASSES = groupLDAPClasses.split(","); 
    attrs.put(new ObjectClassAttribute(GROUP_LDAP_CLASSES));
    attrs.put("ou", group.getGroupName());
    String desc = group.getDescription();
    if (desc != null && desc.length() > 0) attrs.put("description", desc);
    String lbl = group.getLabel();
    if( lbl != null && lbl.length() > 0)  attrs.put("l", lbl);
    return attrs;
  }
  
  final public Group attributesToGroup(Attributes attrs) throws Exception {
    if(attrs == null ||  attrs.size() == 0)  return null ;
    Group group = new GroupImpl();    
    group.setGroupName(getAttributeValueAsString(attrs, "ou"));
    group.setDescription(getAttributeValueAsString(attrs, "description"));    
    group.setLabel(getAttributeValueAsString(attrs, "l"));    
    return group;    
  }
  
  final public Attributes membershipTypeToAttributes(MembershipType mt) {    
    BasicAttributes attrs = new BasicAttributes();
    if(MEMBERSHIPTYPE_LDAP_CLASSES == null)
      MEMBERSHIPTYPE_LDAP_CLASSES = membershipTypeLDAPClasses.split(",");
    attrs.put(new ObjectClassAttribute(MEMBERSHIPTYPE_LDAP_CLASSES));  
    attrs.put(membershipTypeNameAttr, mt.getName());    
    String desc = mt.getDescription();
    if (desc != null && desc.length() > 0) attrs.put( "description", desc);        
    return attrs;
  }
  
  final public MembershipType attributesToMembershipType(Attributes attrs){
    if(attrs == null ||  attrs.size() == 0)  return null ;
    MembershipType m = new MembershipTypeImpl();
    m.setName(getAttributeValueAsString(attrs, membershipTypeNameAttr));
    m.setDescription(getAttributeValueAsString(attrs, "description"));
    m.setCreatedDate(new Date());
    m.setModifiedDate(new Date());
    return m;
  }
  
  final public Attributes membershipToAttributes(Membership m, String userDN) {
    BasicAttributes attrs = new BasicAttributes();
    if(MEMBERSHIP_LDAP_CLASSES == null) MEMBERSHIP_LDAP_CLASSES = membershipLDAPClasses.split(","); 
    attrs.put(new ObjectClassAttribute(MEMBERSHIP_LDAP_CLASSES));  
    attrs.put(membershipTypeRoleNameAttr, m.getMembershipType());
    attrs.put(membershipTypeMemberValue, userDN);    
    return attrs;
  }
  
  final public Attributes profileToAttributes( UserProfile profile) {
    BasicAttributes attrs = new BasicAttributes();
    if(PROFILE_LDAP_CLASSES == null) PROFILE_LDAP_CLASSES = profileLDAPClasses.split(","); 
    attrs.put(new ObjectClassAttribute(PROFILE_LDAP_CLASSES));
    attrs.put("sn", profile.getUserName());
    UserProfileData upd = new UserProfileData() ;  
    upd.setUserProfile( profile) ;    
    attrs.put(ldapDescriptionAttr, upd.getProfile());
    return attrs;
  }
  
  final public UserProfileData attributesToProfile( Attributes attrs) {   
    if(attrs == null ||  attrs.size() == 0)  return null ;
    UserProfileData upd = new UserProfileData() ;    
    upd.setProfile(getAttributeValueAsString(attrs, ldapDescriptionAttr));    
    return upd;
  }
  
  final public String getAttributeValueAsString(Attributes attributes, String name) {
    if (attributes == null)  return "";
    Attribute attr = attributes.get(name);
    if (attr == null) return "";
    try {
      Object obj =  attr.get(); 
      if(obj instanceof byte[]) return new String((byte[]) obj) ;
      return (String) obj ;
    } catch (Exception e) {      
      return "";
    }   
  }  
}
