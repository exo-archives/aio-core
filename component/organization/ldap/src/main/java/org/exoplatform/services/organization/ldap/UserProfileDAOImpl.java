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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.naming.NameNotFoundException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InvalidAttributeValueException;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapContext;

import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileEventListener;
import org.exoplatform.services.organization.UserProfileEventListenerHandler;
import org.exoplatform.services.organization.UserProfileHandler;
import org.exoplatform.services.organization.impl.UserProfileData;
import org.exoplatform.services.organization.impl.UserProfileImpl;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Oct 14, 2005
 */
public class UserProfileDAOImpl extends BaseDAO implements UserProfileHandler,
    UserProfileEventListenerHandler {

  private List<UserProfileEventListener> listeners_;

  public UserProfileDAOImpl(LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService) {
    super(ldapAttrMapping, ldapService);
    listeners_ = new ArrayList<UserProfileEventListener>(3);
  }

  final public UserProfile createUserProfileInstance() {
    return new UserProfileImpl();
  }

  public UserProfile createUserProfileInstance(String userName) {
    return new UserProfileImpl(userName);
  }

  public void createUser(UserProfile profile, boolean broadcast) throws Exception {
    LdapContext ctx = ldapService_.getLdapContext();
    String profileDN = ldapAttrMapping_.membershipTypeNameAttr + "=" + profile.getUserName() + ","
        + ldapAttrMapping_.profileURL;
    ctx.createSubcontext(profileDN, ldapAttrMapping_.profileToAttributes(profile));
  }

  public void saveUserProfile(UserProfile profile, boolean broadcast) throws Exception {
    LdapContext ctx = ldapService_.getLdapContext();
    String profileDN = ldapAttrMapping_.membershipTypeNameAttr + "=" + profile.getUserName() + ","
        + ldapAttrMapping_.profileURL;
    try {
      Attributes attrs = ctx.getAttributes(profileDN);
      if (attrs == null) {
        createUser(profile, broadcast);
        return;
      }
      UserProfileData upd = new UserProfileData();
      upd.setUserProfile(profile);
      ModificationItem[] mods = new ModificationItem[1];
      mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                                     new BasicAttribute(ldapAttrMapping_.ldapDescriptionAttr,
                                                        upd.getProfile()));
      ctx.modifyAttributes(profileDN, mods);
    } catch (NameNotFoundException notfound) {
      createUser(profile, broadcast);
    } catch (InvalidAttributeValueException invalid) {
      invalid.printStackTrace();
    }
  }

  public UserProfile removeUserProfile(String userName, boolean broadcast) throws Exception {
    LdapContext ctx = ldapService_.getLdapContext();
    String profileDN = ldapAttrMapping_.membershipTypeNameAttr + "=" + userName + ","
        + ldapAttrMapping_.profileURL;
    try {
      Attributes attrs = ctx.getAttributes(profileDN);
      if (attrs == null)
        return null;
      UserProfile profile = ldapAttrMapping_.attributesToProfile(attrs).getUserProfile();
      ctx.destroySubcontext(profileDN);
      return profile;
    } catch (Exception exo) {
    }
    return null;
  }

  public UserProfile findUserProfileByName(String userName) throws Exception {
    LdapContext ctx = ldapService_.getLdapContext();
    String profileDN = ldapAttrMapping_.membershipTypeNameAttr + "=" + userName + ","
        + ldapAttrMapping_.profileURL;
    try {
      Attributes attrs = ctx.getAttributes(profileDN);
      if (attrs == null)
        return null;
      return ldapAttrMapping_.attributesToProfile(attrs).getUserProfile();
    } catch (Exception exo) {
    }
    return null;
  }

  public Collection findUserProfiles() throws Exception {
    return null;
  }

  public void addUserProfileEventListener(UserProfileEventListener listener) {
    listeners_.add(listener);
  }

  /**
   * {@inheritDoc}
   */
  public List<UserProfileEventListener> getUserProfileListeners() {
    return Collections.unmodifiableList(listeners_);
  }
}
