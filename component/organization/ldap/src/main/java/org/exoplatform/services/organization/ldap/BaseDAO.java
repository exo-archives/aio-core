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
import java.util.Enumeration;
import java.util.List;

import javax.naming.CommunicationException;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.ServiceUnavailableException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.commons.logging.Log;

import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.impl.GroupImpl;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen.
 * tuan08@users.sourceforge.net Oct 14, 2005
 * @version andrew00x $
 */
public class BaseDAO {
  
  /**
   * Default max allowed number of connection errors.  
   */
  public static final int        DEFAULT_MAX_CONNECTION_ERROR = 1;

  /**
   * Logger.
   */
  private static final Log       LOG                          = ExoLogger.getLogger("core.BaseAO");

  /**
   * Mapping LDAP attributes to eXo organization service items (users, groups, etc). 
   */
  protected LDAPAttributeMapping ldapAttrMapping;

  /**
   * LDAP service.
   */
  protected LDAPService          ldapService;

  /**
   * See {@link NameParser}.
   */
  private NameParser             parser;

  /**
   * Max number of connection errors.
   * @see #getMaxConnectionError() 
   */
  private static int             maxConnectionError           = -1;

  /**
   * @param ldapAttrMapping {@link LDAPAttributeMapping}
   * @param ldapService {@link LDAPService}
   * @throws Exception if any error occurs
   */
  public BaseDAO(LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService) throws Exception {
    this.ldapAttrMapping = ldapAttrMapping;
    this.ldapService = ldapService;
    initializeNameParser();
  }
  
  /**
   * @return get max allowed number of connection error
   */
  public static int getMaxConnectionError() {
    return maxConnectionError >= 0 ? maxConnectionError : DEFAULT_MAX_CONNECTION_ERROR;
  }
  
  /**
   * @param connectionError @see #maxConnectionError @see
   *          {@link #getMaxConnectionError()}
   */
  public static void setMaxConnectionError(int connectionError) {
    maxConnectionError = connectionError;
  }
  
  /**
   * Check is supplied Exception thrown in consequence connection error.
   * 
   * @param e exception
   * @return true if exception is instance of {@link CommunicationException} or
   *         {@link ServiceUnavailableException}
   */
  public static boolean isConnectionError(Exception e) {
    return e instanceof CommunicationException || e instanceof ServiceUnavailableException;
  }
  
  /**
   * Construct object name from {@link Group} id.
   * 
   * @param groupId group id
   * @return object name
   */
  protected String getGroupDNFromGroupId(String groupId) {
    StringBuilder buffer = new StringBuilder();
    String[] groupParts = groupId.split("/");
    // TODO : http://jira.exoplatform.org/browse/COR-49
    for (int x = (groupParts.length - 1); x > 0; x--) {
      buffer.append("ou=" + groupParts[x] + ", ");
    }
    buffer.append(ldapAttrMapping.groupsURL);
    return buffer.toString();
  }

  /**
   * Get collection of {@link Attribute} with specified name from
   * {@link Attributes}.
   * 
   * @param attributes Attributes to be processed
   * @param attribute attribute name
   * @return List of Attribute, never null. If nothing found empty list returned.
   */
  protected List<Object> getAttributes(Attributes attributes, String attribute) {
    List<Object> results = new ArrayList<Object>();
    try {
      if (attributes == null)
        return results;
      Attribute attr = attributes.get(attribute);
      for (int x = 0; x < attr.size(); x++)
        results.add(attr.get(x));
    } catch (NamingException e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
    }
    return results;
  }

  /**
   * Restore Group from membership DN.
   * 
   * @param membershipDN membership Distinguished Name
   * @return Group
   * @throws Exception if any error occurs
   */
  @Deprecated
  protected Group getGroupFromMembershipDN(String membershipDN) throws Exception {
    String[] membershipParts = explodeDN(membershipDN, false);
    StringBuffer buffer = new StringBuffer();
    for (int x = 1; x < membershipParts.length; x++) {
      if (x == membershipParts.length - 1) {
        buffer.append(membershipParts[x]);
      } else {
        buffer.append(membershipParts[x] + ",");
      }
    }
    Group group = getGroupByDN(buffer.toString());
    return group;
  }

  /**
   * Restore Group from membership DN.
   * 
   * @param ctx {@link LdapContext}
   * @param membershipDN membership Distinguished Name
   * @return Group
   * @throws NamingException if any naming errors occurs
   */
  protected Group getGroupFromMembershipDN(LdapContext ctx, String membershipDN) throws NamingException {
    String[] membershipParts = explodeDN(membershipDN, false);
    StringBuffer buffer = new StringBuffer();
    for (int x = 1; x < membershipParts.length; x++) {
      if (x == membershipParts.length - 1) {
        buffer.append(membershipParts[x]);
      } else {
        buffer.append(membershipParts[x] + ",");
      }
    }
    Group group = getGroupByDN(ctx, buffer.toString());
    return group;
  }

  /**
   * Get Group what reflected to object with specified Distinguished Name. If
   * connection to LDAP server was lost then this method will try restore
   * connection at least <tt>MAX_CONNECTION_ERROR</tt> times.
   * 
   * @param groupDN group Distinguished Name
   * @return Group or null it nothing found
   * @throws Exception if any error occurs
   */
  @Deprecated
  protected Group getGroupByDN(String groupDN) throws Exception {
    LdapContext ctx = ldapService.getLdapContext();
    // process situation when connection to LDAP server was closed by timeout , etc
    try {
      for (int err = 0;; err++) {
        try {
          return getGroupByDN(ctx, groupDN);
        } catch (NamingException e) {
          // check is allowed to try one more time
          if (isConnectionError(e) && err < getMaxConnectionError())
            ctx = ldapService.getLdapContext(true);
          else
            // not connection exception or error occurs more than MAX_CONNECTION_ERROR
            throw e;
        }
      }
    } finally {
      ldapService.release(ctx);
    }
  }

  /**
   * Get Group what reflected to object with specified Distinguished Name.
   * 
   * @param ctx {@link LdapContext}
   * @param groupDN group Distinguished Name
   * @return Group or null it nothing found
   * @throws NamingException if any naming errors occurs
   */
  protected Group getGroupByDN(LdapContext ctx, String groupDN) throws NamingException {
    StringBuffer idBuffer = new StringBuffer();
    String parentId = null;
    String[] baseParts = explodeDN(ldapAttrMapping.groupsURL, true);
    String[] membershipParts = explodeDN(groupDN, true);
    for (int x = (membershipParts.length - baseParts.length - 1); x > -1; x--) {
      idBuffer.append("/" + membershipParts[x]);
      if (x == 1)
        parentId = idBuffer.toString();
    }
    try {
      Attributes attrs = ctx.getAttributes(groupDN);
      GroupImpl group = new GroupImpl();
      group.setGroupName(membershipParts[0]);
      group.setId(idBuffer.toString());
      // TODO needs to use mapping there :
      // http://jira.exoplatform.org/browse/COR-49
      group.setDescription(ldapAttrMapping.getAttributeValueAsString(attrs, "description"));
      group.setLabel(ldapAttrMapping.getAttributeValueAsString(attrs, "l"));
      group.setParentId(parentId);
      return group;
    } catch (NameNotFoundException e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      // Object with specified Distinguished Name not found. Null will be
      // returned. This result we regard as successful, just nothing found.
      return null;
    }
  }
  
  /**
   * Parse supplied DN to hierarchical representation.
   * 
   * @param nameDN DN of object
   * @param removeTypes remove object types or not
   * @return hierarchical String array
   * @throws NamingException if any {@link NamingException} occurs
   */
  protected String[] explodeDN(String nameDN, boolean removeTypes) throws NamingException {
    Name dn = parser.parse(nameDN);
    Enumeration<String> enumeration = dn.getAll();
    List<String> list = new ArrayList<String>();
    while (enumeration.hasMoreElements()) {
      String ldap = enumeration.nextElement();
      if (removeTypes) {
        int position = ldap.indexOf("=");
        String value = ldap.substring(position + 1);
        list.add(0, value);
      } else
        list.add(0, ldap);
    }
    String[] explodedDN = new String[list.size()];
    list.toArray(explodedDN);
    return explodedDN;
  }
  

  /**
   * Find user with supplied name.
   * 
   * @param username user name
   * @return User or null if nothing found
   * @throws Exception if any error occurs
   */
  @Deprecated
  protected User getUserFromUsername(String username) throws Exception {
    LdapContext ctx = ldapService.getLdapContext();
    try {
      for (int err = 0;; err++) {
        try {
          return getUserFromUsername(ctx, username);
        } catch (NamingException e) {
          if (isConnectionError(e) && err < getMaxConnectionError())
            ctx = ldapService.getLdapContext(true);
          else
            throw e;
        }
      }
    } finally {
      ldapService.release(ctx);
    }
  }

  /**
   * Find user with supplied name.
   * 
   * @param ctx {@link LdapContext}
   * @param username user name
   * @return User or null if nothing found
   * @throws Exception if any error occurs
   */
  protected User getUserFromUsername(LdapContext ctx, String username) throws Exception {
    NamingEnumeration<SearchResult> answer = null;
    try {
      answer = findUser(ctx, username, true);
      while (answer.hasMoreElements()) {
        String userDN = answer.next().getNameInNamespace();
        Attributes attrs = ctx.getAttributes(userDN);
        return ldapAttrMapping.attributesToUser(attrs);
      }
      return null;
    } finally {
      if (answer != null)
        answer.close();
    }
  }

  /**
   * Get Object Distinguished Name from user name.
   * 
   * @param username user name
   * @return object Distinguished Name how it looks in directory context
   * @throws NamingException if any {@link NamingException} occurs
   */
  protected String getDNFromUsername(String username) throws NamingException {
    NamingEnumeration<SearchResult> answer = null;
    try {
      answer = findUser(username, false);
      if (answer.hasMoreElements())
        return answer.next().getNameInNamespace();
      return null;
    } finally {
      if (answer != null)
        answer.close();
    }
  }

  /**
   * Get Object Distinguished Name from user name.
   * 
   * @param ctx {@link LdapContext}
   * @param username user name
   * @return object Distinguished Name how it looks in directory context
   * @throws NamingException if any {@link NamingException} occurs
   */
  protected String getDNFromUsername(LdapContext ctx, String username) throws NamingException {
    NamingEnumeration<SearchResult> answer = null;
    try {
      answer = findUser(ctx, username, false);
      if (answer.hasMoreElements())
        return answer.next().getNameInNamespace();
      return null;
    } finally {
      if (answer != null)
        answer.close();
    }
  }

  /**
   * Find user with specified name.
   * 
   * @param username user name
   * @param hasAttribute has object some external attributes to be used for
   *          searching
   * @return {@link NamingEnumeration} with search results
   * @throws NamingException if any {@link NamingException} occurs
   */
  private NamingEnumeration<SearchResult> findUser(String username, boolean hasAttribute) throws NamingException {
    LdapContext ctx = ldapService.getLdapContext();
    try {
      for (int err = 0;; err++) {
        try {
          return findUser(ctx, username, hasAttribute);
        } catch (NamingException e) {
          if (isConnectionError(e) && err < getMaxConnectionError())
            ctx = ldapService.getLdapContext(true);
          else
            throw e;
        }
      }
    } finally {
      ldapService.release(ctx);
    }
  }

  /**
   * Find user with specified name.
   * 
   * @param ctx {@link LdapContext}
   * @param username user name
   * @param hasAttribute has object some external attributes to be used for
   *          searching
   * @return {@link NamingEnumeration} with search results
   * @throws NamingException if any {@link NamingException} occurs
   */
  private NamingEnumeration<SearchResult> findUser(LdapContext ctx,
                                                   String username,
                                                   boolean hasAttribute) throws NamingException {
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
    if (!hasAttribute) {
      constraints.setReturningAttributes(new String[] {""});
      constraints.setDerefLinkFlag(true);
    }
    String filter = "(&(" + ldapAttrMapping.userUsernameAttr + "=" + username + ")" + "("
        + ldapAttrMapping.userObjectClassFilter + "))";
    return ctx.search(ldapAttrMapping.userURL, filter, constraints);
  }
  
  
  /**
   * Remove supplied sub-context.
   * 
   * @param ctx {@link LdapContext}
   * @param dn Distinguished Name of sub-context to be removed
   * @throws NamingException if any {@link NamingException} occurs
   */
  protected void removeAllSubtree(LdapContext ctx, String dn) throws NamingException {
    NamingEnumeration<SearchResult> answer = null;
    try {
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
      answer = ctx.search(dn, "(objectclass=*)", constraints);
      while (answer.hasMoreElements()) {
        SearchResult sr = answer.next();
        removeAllSubtree(ctx, sr.getNameInNamespace());
      }
      ctx.destroySubcontext(dn);
    } finally {
      if (answer != null)
        answer.close();
    }
  }

  /**
   * @param dn escape special characters for DN
   * @return DN string after escaping
   */
  public static String escapeDN(String dn) {
    if (dn == null)
      return dn;
    StringBuilder buf = new StringBuilder(dn.length());
    for (int i = 0; i < dn.length(); i++) {
      char c = dn.charAt(i);
      switch (c) {
      case '\\':
        buf.append("\\5c");
        break;
      case '*':
        buf.append("\\2a");
        break;
      case '(':
        buf.append("\\28");
        break;
      case ')':
        buf.append("\\29");
        break;
      case '\0':
        buf.append("\\00");
        break;
      default:
        buf.append(c);
        break;
      }
    }
    return buf.toString();
  }

  /**
   * Find user by DN.
   * 
   * @param ctx {@link LdapContext}
   * @param userDN user DN
   * @return {@link User}
   * @throws Exception if any errors occurs
   */
  protected User findUserByDN(LdapContext ctx, String userDN) throws Exception {
    if (userDN == null)
      return null;
    try {
      Attributes attrs = ctx.getAttributes(userDN);
      User user = ldapAttrMapping.attributesToUser(attrs);
      user.setFullName(user.getFirstName() + " " + user.getLastName());
      return user;
    } catch (NameNotFoundException e) {
      return null;
    }
  }

  /**
   * Check does {@link Attributes} contains specified user DN.
   * 
   * @param attrs {@link Attributes}
   * @param userDN user object Distinguished Name
   * @return true if Attributes contains <tt>userDN</tt> false otherwise
   * @throws NamingException if any {@link NamingException} occurs
   */
  protected boolean haveUser(Attributes attrs, String userDN) throws NamingException {
    if (attrs == null || userDN == null)
      return false;
    List<Object> members = this.getAttributes(attrs, ldapAttrMapping.membershipTypeMemberValue);
    for (int i = 0; i < members.size(); i++) {
      if (String.valueOf(members.get(i)).trim().equalsIgnoreCase(userDN))
        return true;
    }
    return false;
  }

  /**
   * @return LDAP search filter
   */
  protected String membershipClassFilter() {
    String mbfilter = ldapAttrMapping.membershipObjectClassFilter;
    if (mbfilter == null)
      return null;
    if (!mbfilter.startsWith("("))
      mbfilter = "(" + mbfilter;
    if (!mbfilter.endsWith(")"))
      mbfilter += ")";
    return mbfilter;
  }

  /**
   * @throws NamingException in error occurs during parser initializing
   */
  private void initializeNameParser() throws NamingException {
    LdapContext ctx = ldapService.getLdapContext();
    // process situation when connection to LDAP server was closed by timeout, etc
    try {
      for (int err = 0;; err++) {
        try {
          parser = ctx.getNameParser("");
          return;
        } catch (NamingException e) {
          // check is allowed to try one more time
          if (isConnectionError(e) && err < getMaxConnectionError())
            // update LdapContext
            ctx = ldapService.getLdapContext(true);
          else
            // not connection exception or error occurs more than
            // MAX_CONNECTION_ERROR times
            throw e;
        }
      }
    } finally {
      ldapService.release(ctx);
    }
  }

}
