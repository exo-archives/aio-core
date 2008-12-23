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
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.exoplatform.commons.utils.ObjectPageList;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.services.organization.impl.UserImpl;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Oct 14, 2005
 */
public class UserDAOImpl extends BaseDAO implements UserHandler {

  /**
   * User event listeners.
   * 
   * @see UserEventListener.
   */
  private List<UserEventListener> listeners = new ArrayList<UserEventListener>(5);

  public UserDAOImpl(LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService) throws Exception {
    super(ldapAttrMapping, ldapService);
  }

  /**
   * {@inheritDoc}
   */
  public void addUserEventListener(UserEventListener listener) {
    listeners.add(listener);
  }

  /**
   * {@inheritDoc}
   */
  public User createUserInstance() {
    return new UserImpl();
  }

  /**
   * {@inheritDoc}
   */
  public User createUserInstance(String username) {
    return new UserImpl(username);
  }

  /**
   * {@inheritDoc}
   */
  public void createUser(User user, boolean broadcast) throws Exception {
    String dnKeyValue = getDNKeyValue(user);
    String userDN = ldapAttrMapping.userDNKey + "=" + dnKeyValue + "," + ldapAttrMapping.userURL;
    Attributes attrs = ldapAttrMapping.userToAttributes(user);
    LdapContext ctx = getLdapContext();
    try {
      for (int err = 0;; err++) {
        try {
          if (broadcast)
            preSave(user, true);
          ctx.createSubcontext(userDN, attrs);
          if (broadcast)
            postSave(user, true);
          break;
        } catch (NamingException e) {
          if (isConnectionError(e) && err < getMaxConnectionError())
            ctx = getLdapContext(true);
          else
            throw e;
        }
      }
    } finally {
      release(ctx);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void saveUser(User user, boolean broadcast) throws Exception {
    LdapContext ctx = getLdapContext();
    String userDN = null;
    try {
      for (int err = 0;; err++) {
        try {
          userDN = getDNFromUsername(ctx, user.getUserName());
          if (userDN == null)
            return;
          User existingUser = getUserFromUsername(ctx, user.getUserName());
          ModificationItem[] mods = createUserModification(user, existingUser);
          if (broadcast)
            preSave(user, false);
          ctx.modifyAttributes(userDN, mods);
          if (broadcast)
            postSave(user, false);
          break;
        } catch (NamingException e) {
          if (isConnectionError(e) && err < getMaxConnectionError())
            ctx = getLdapContext(true);
          else
            throw e;
        }
      }
    } finally {
      release(ctx);
    }
    // TODO really need this ?
    if (!user.getPassword().equals("PASSWORD"))
      saveUserPassword(user, userDN);
  }

  void saveUserPassword(User user, String userDN) throws Exception {
    ModificationItem[] mods = new ModificationItem[] { new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                                                                            new BasicAttribute(ldapAttrMapping.userPassword,
                                                                                               user.getPassword())) };
    LdapContext ctx = getLdapContext();
    try {
      for (int err = 0;; err++) {
        try {
          ctx.modifyAttributes(userDN, mods);
          break;
        } catch (NamingException e) {
          if (isConnectionError(e) && err < getMaxConnectionError())
            ctx = getLdapContext(true);
          else
            throw e;
        }
      }
    } finally {
      release(ctx);
    }
  }

  /**
   * {@inheritDoc}
   */
  public User removeUser(String userName, boolean broadcast) throws Exception {
    LdapContext ctx = getLdapContext();
    try {
      for (int err = 0;; err++) {
        try {
          User user = getUserFromUsername(ctx, userName);
          if (user == null)
            return null;
          
          String userDN = getDNFromUsername(ctx, userName);

          if (broadcast)
            preDelete(user);
          ctx.destroySubcontext(userDN);
          if (broadcast)
            postDelete(user);
          return user;
        } catch (NamingException e) {
          if (isConnectionError(e) && err < getMaxConnectionError())
            ctx = getLdapContext(true);
          else
            throw e;
        }
      }
    } finally {
      release(ctx);
    }
  }

  /**
   * {@inheritDoc}
   */
  public User findUserByName(String userName) throws Exception {
    LdapContext ctx = getLdapContext();
    try {
      for (int err = 0;;err++) {
        try {
          return getUserFromUsername(ctx, userName);
        } catch (NamingException e) {
          if (isConnectionError(e) && err < getMaxConnectionError())
            ctx = getLdapContext(true);
          else
            throw e;
        }
      }
    } finally {
      release(ctx);
    }
  }

  /**
   * {@inheritDoc}
   */
  public PageList findUsersByGroup(String groupId) throws Exception {
    ArrayList<User> users = new ArrayList<User>();
    TreeMap<String, User> map = new TreeMap<String, User>();

    LdapContext ctx = getLdapContext();
    try {
      for (int err = 0;; err++) {
        map.clear();
        try {
          String searchBase = this.getGroupDNFromGroupId(groupId);
          String filter = ldapAttrMapping.membershipObjectClassFilter;
          SearchControls constraints = new SearchControls();
          constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
          NamingEnumeration<SearchResult> results = ctx.search(searchBase, filter, constraints);
          while (results.hasMoreElements()) {
            SearchResult sr = results.next();
            Attributes attrs = sr.getAttributes();
            List<Object> members = this.getAttributes(attrs,
                                                      ldapAttrMapping.membershipTypeMemberValue);
            for (int x = 0; x < members.size(); x++) {
              User user = findUserByDN(ctx, (String) members.get(x));
              if (user != null)
                map.put(user.getUserName(), user);
            }
          }
          break;
        } catch (NamingException e) {
          if (isConnectionError(e) && err < getMaxConnectionError())
            ctx = getLdapContext(true);
          else 
            throw e;
        }
      }
    } finally {
      release(ctx);
    }

    for (Iterator<String> i = map.keySet().iterator(); i.hasNext();)
      users.add(map.get(i.next()));
    return new ObjectPageList(users, 20);
  }

  /**
   * {@inheritDoc}
   */
  public PageList getUserPageList(int pageSize) throws Exception {
    String searchBase = ldapAttrMapping.userURL;
    String filter = ldapAttrMapping.userObjectClassFilter;
    return new LDAPUserPageList(ldapAttrMapping, ldapService, searchBase, filter, pageSize);
  }

  /**
   * {@inheritDoc}
   */
  public PageList findUsers(Query q) throws Exception {
    String filter = null;
    ArrayList<String> list = new ArrayList<String>();
    if (q.getUserName() != null && q.getUserName().length() > 0) {
      list.add("(" + ldapAttrMapping.userUsernameAttr + "=" + q.getUserName() + ")");
    }
    if (q.getFirstName() != null && q.getFirstName().length() > 0) {
      list.add("(" + ldapAttrMapping.userFirstNameAttr + "=" + q.getFirstName() + ")");
    }
    if (q.getLastName() != null && q.getLastName().length() > 0) {
      list.add("(" + ldapAttrMapping.userLastNameAttr + "=" + q.getLastName() + ")");
    }
    if (q.getEmail() != null && q.getEmail().length() > 0) {
      list.add("(" + ldapAttrMapping.userMailAttr + "=" + q.getEmail() + ")");
    }

    if (list.size() > 0) {
      StringBuilder buffer = new StringBuilder();
      buffer.append("(&");
      if (list.size() > 1) {
        for (int x = 0; x < list.size(); x++) {
          if (x == (list.size() - 1))
            buffer.append(list.get(x));
          else
            buffer.append(list.get(x) + " || ");
        }
      } else
        buffer.append(list.get(0));

      buffer.append("(" + ldapAttrMapping.userObjectClassFilter + ") )");
      filter = buffer.toString();
    } else
      filter = ldapAttrMapping.userObjectClassFilter;
    String searchBase = ldapAttrMapping.userURL;
    return new LDAPUserPageList(ldapAttrMapping, ldapService, searchBase, filter, 20);
  }

  /**
   * {@inheritDoc}
   */
  public boolean authenticate(String username, String password) throws Exception {
    String userDN = getDNFromUsername(username);
    if (userDN == null)
      return false;
    try {
      return ldapService.authenticate(userDN, password);
    } catch (Exception exp) {
      return false;
    }
  }
  
  // helpers
  
  private String getDNKeyValue(User user) {
    String dnKeyValue = user.getUserName();
    if (!ldapAttrMapping.userDNKey.equals(ldapAttrMapping.userUsernameAttr)) {
      if (ldapAttrMapping.userDNKey.equals(ldapAttrMapping.userLastNameAttr)) {
        dnKeyValue = user.getLastName();
      } else if (ldapAttrMapping.userDNKey.equals(ldapAttrMapping.userFirstNameAttr)) {
        dnKeyValue = user.getFirstName();
      } else if (ldapAttrMapping.userDNKey.equals(ldapAttrMapping.userMailAttr)) {
        dnKeyValue = user.getEmail();
      } else if (ldapAttrMapping.userDNKey.equals(ldapAttrMapping.userDisplayNameAttr)) {
        dnKeyValue = user.getFullName();
      }
    }
    return dnKeyValue;
  }

  /**
   * @param user user for saving
   * @param existingUser existing user
   * @return array of {@link ModificationItem}
   */
  private ModificationItem[] createUserModification(User user, User existingUser) {
    ArrayList<ModificationItem> modifications = new ArrayList<ModificationItem>();

    // update displayName & description
    if (!user.getFullName().equals(existingUser.getFullName())) {
      ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                                                  new BasicAttribute(ldapAttrMapping.userDisplayNameAttr,
                                                                     user.getFullName()));
      modifications.add(mod);
      mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                                 new BasicAttribute(ldapAttrMapping.ldapDescriptionAttr,
                                                    user.getFullName()));
      modifications.add(mod);
    }
    // update account name
    if (!user.getUserName().equals(existingUser.getUserName())) {
      ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                                                  new BasicAttribute(ldapAttrMapping.userUsernameAttr,
                                                                     user.getUserName()));
      modifications.add(mod);
    }
    // update last name
    if (!user.getLastName().equals(existingUser.getLastName())) {
      ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                                                  new BasicAttribute(ldapAttrMapping.userLastNameAttr,
                                                                     user.getLastName()));
      modifications.add(mod);
    }
    // update first name
    if (!user.getFirstName().equals(existingUser.getFirstName())) {
      ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                                                  new BasicAttribute(ldapAttrMapping.userFirstNameAttr,
                                                                     user.getFirstName()));
      modifications.add(mod);
    }
    // update email
    if (!user.getEmail().equals(existingUser.getEmail())) {
      ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                                                  new BasicAttribute(ldapAttrMapping.userMailAttr,
                                                                     user.getEmail()));
      modifications.add(mod);
    }
    ModificationItem[] mods = new ModificationItem[modifications.size()];
    return modifications.toArray(mods);
  }

  // listeners
  
  protected void preSave(User user, boolean isNew) throws Exception {
    for (UserEventListener listener : listeners)
      listener.preSave(user, isNew);
  }

  protected void postSave(User user, boolean isNew) throws Exception {
    for (UserEventListener listener : listeners)
      listener.postSave(user, isNew);
  }

  protected void preDelete(User user) throws Exception {
    for (UserEventListener listener : listeners)
      listener.preDelete(user);
  }

  protected void postDelete(User user) throws Exception {
    for (UserEventListener listener : listeners)
      listener.postDelete(user);
  }
}
