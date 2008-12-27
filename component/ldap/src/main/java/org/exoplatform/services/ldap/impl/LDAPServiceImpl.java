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
package org.exoplatform.services.ldap.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.ServiceUnavailableException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.ldap.CreateObjectCommand;
import org.exoplatform.services.ldap.DeleteObjectCommand;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS . Author : James Chamberlain
 * james@echamberlains.com Date: 11/2/2005
 */
public class LDAPServiceImpl implements LDAPService, ComponentRequestLifecycle {

  private static final Log    LOG        = ExoLogger.getLogger(LDAPServiceImpl.class.getName());

  private Map<String, String> env        = new HashMap<String, String>();

  private int                 serverType = DEFAULT_SERVER;

  /**
   * @param params See {@link InitParams}
   */
  public LDAPServiceImpl(InitParams params) {
    LDAPConnectionConfig config = (LDAPConnectionConfig) params.getObjectParam("ldap.config")
                                                               .getObject();

    String url = config.getProviderURL();
    serverType = toServerType(config.getServerName());

    boolean ssl = url.toLowerCase().startsWith("ldaps");
    if (serverType == ACTIVE_DIRECTORY_SERVER && ssl) {
      String keystore = System.getProperty("java.home");
      keystore += File.separator + "lib" + File.separator + "security" + File.separator + "cacerts";
      System.setProperty("javax.net.ssl.trustStore", keystore);
    }

    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.SECURITY_AUTHENTICATION, config.getAuthenticationType());
    env.put(Context.SECURITY_PRINCIPAL, config.getRootDN());
    env.put(Context.SECURITY_CREDENTIALS, config.getPassword());
    // TODO move it in configuration ?
    env.put("com.sun.jndi.ldap.connect.timeout", "60000");
    
    env.put("com.sun.jndi.ldap.connect.pool", "true");
    env.put("java.naming.ldap.version", config.getVerion());
    env.put("java.naming.ldap.attributes.binary", "tokenGroups");
    env.put(Context.REFERRAL, config.getReferralMode());

    Pattern pattern = Pattern.compile("\\p{Space}*,\\p{Space}*", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(url);
    if (ssl)
      url = matcher.replaceAll("/ ldaps://");
    else
      url = matcher.replaceAll("/ ldap://");
    url += "/";
    env.put(Context.PROVIDER_URL, url);

    if (serverType == ACTIVE_DIRECTORY_SERVER && ssl)
      env.put(Context.SECURITY_PROTOCOL, "ssl");
  }

  /**
   * {@inheritDoc}
   */
  public LdapContext getLdapContext() throws NamingException {
    // This method can be used for getting context from thread-local variables,
    // etc. instead create new instance of LdapContext. Currently just create
    // new one (use from pool if 'com.sun.jndi.ldap.connect.pool' is 'true').
    // Override this method if need other behavior.
    return getLdapContext(true);
  }
  
  /**
   * {@inheritDoc}
   */
  public LdapContext getLdapContext(boolean renew) throws NamingException {
    // Force create new context.  
    return new InitialLdapContext(new Hashtable<String, String>(env), null);
  }
  
  /**
   * {@inheritDoc}
   */
  public void release(LdapContext ctx) {
    // Just close since we are not pooling anything by self.
    // Override this method if need other behavior.
    try {
      if (ctx != null)
        ctx.close();
    } catch (NamingException e) {
      LOG.warn("Exception occur when try close LDAP context. ", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public InitialContext getInitialContext() throws NamingException {
    Hashtable<String, String> props = new Hashtable<String, String>(env);
    props.put(Context.OBJECT_FACTORIES, "com.sun.jndi.ldap.obj.LdapGroupFactory");
    props.put(Context.STATE_FACTORIES, "com.sun.jndi.ldap.obj.LdapGroupFactory");
    return new InitialLdapContext(props, null);
  }

  /**
   * {@inheritDoc}
   */
  public boolean authenticate(String userDN, String password) throws NamingException {
    Hashtable<String, String> props = new Hashtable<String, String>(env);
    props.put(Context.SECURITY_AUTHENTICATION, "simple");
    props.put(Context.SECURITY_PRINCIPAL, userDN);
    props.put(Context.SECURITY_CREDENTIALS, password);
    props.put("com.sun.jndi.ldap.connect.pool", "false");
    try {
      new InitialLdapContext(props, null);
      return true;
    } catch (NamingException e) {
      if (LOG.isDebugEnabled())
        e.printStackTrace();
      return false;
    }
  }

  /**
   * {@inheritDoc}
   */
  public int getServerType() {
    return serverType;
  }

  /**
   * Delete objects from context.
   * 
   * @param plugin see {@link DeleteObjectCommand} {@link ComponentPlugin}
   * @throws NamingException if {@link NamingException} occurs
   */
  public void addDeleteObject(ComponentPlugin plugin) throws NamingException {
    if (false&&plugin instanceof DeleteObjectCommand) {
      DeleteObjectCommand command = (DeleteObjectCommand) plugin;
      List<String> objectsToDelete = command.getObjectsToDelete();
      if (objectsToDelete == null || objectsToDelete.size() == 0)
        return;
      LdapContext ctx = getLdapContext();
      for (String name : objectsToDelete) {
        try {
          try {
            unbind(ctx, name);
          } catch (CommunicationException e1) {
            // create new LDAP context
            ctx = getLdapContext(true);
            // try repeat operation where communication error occurs
            unbind(ctx, name);
          } catch (ServiceUnavailableException e2) {
            // do the same as for CommunicationException
            ctx = getLdapContext(true);
            //
            unbind(ctx, name);
          }
        } catch (Exception e3) {
          // Catch all exceptions here.
          // Just inform about exception if it is not connection problem.
          LOG.error("Remove object (" + name + ") failed. ", e3);
        }
      }
      // close context
      release(ctx);
    }
  }

  private void unbind(LdapContext ctx, String name) throws NamingException {
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
    NamingEnumeration<SearchResult> results = ctx.search(name, "(objectclass=*)", constraints);
    while (results.hasMore()) {
      SearchResult sr = results.next();
      unbind(ctx, sr.getNameInNamespace());
    }
    // close search results enumeration
    results.close();
    ctx.unbind(name);
  }

  /**
   * Create objects in context.
   * 
   * @param plugin see {@link CreateObjectCommand} {@link ComponentPlugin}
   * @throws NamingException if {@link NamingException} occurs
   */
  public void addCreateObject(ComponentPlugin plugin) throws NamingException {
    if (plugin instanceof CreateObjectCommand) {
      CreateObjectCommand command = (CreateObjectCommand) plugin;
      Map<String, Attributes> objectsToCreate = command.getObjectsToCreate();
      if (objectsToCreate == null || objectsToCreate.size() == 0)
        return;
      LdapContext ctx = getLdapContext();
      for (Map.Entry<String, Attributes> e : objectsToCreate.entrySet()) {
        String name = e.getKey();
        Attributes attrs = e.getValue();
        try {
          try {
            ctx.createSubcontext(name, attrs);
          } catch (CommunicationException e1) {
            // create new LDAP context
            ctx = getLdapContext(true);
            // try repeat operation where communication error occurs
            ctx.createSubcontext(name, attrs);
          } catch (ServiceUnavailableException e2) {
            // do the same as for CommunicationException
            ctx = getLdapContext(true);
            //
            ctx.createSubcontext(name, attrs);
          }
        } catch (Exception e3) {
          // Catch all exceptions here.
          // just inform about exception if it is not connection problem.
          LOG.error("Create object (" + name + ") failed. ", e3);
        }
      }
      release(ctx);
    }
  }
  
  /**
   * {@inheritDoc}
   * 
   * @deprecated Will be removed
   */
  public void startRequest(ExoContainer container) {
  }

  /**
   * {@inheritDoc}
   * 
   * @deprecated Will be removed
   */
  public void endRequest(ExoContainer container) {
//     LdapContext context = tlocal_.get();
//    if (context != null) {
//      try {
//        context.close();
//        tlocal_.set(null);
//      } catch (Exception ex) {
//        ex.printStackTrace();
//      }
//    }
  }

  private int toServerType(String name) {
    name = name.trim();
    if (name == null || name.length() < 1)
      return DEFAULT_SERVER;
    if (name.equalsIgnoreCase("ACTIVE.DIRECTORY"))
      return ACTIVE_DIRECTORY_SERVER;
    // if(name.equalsIgnoreCase("OPEN.LDAP"))return OPEN_LDAP_SERVER;
    // if(name.equalsIgnoreCase("NETSCAPE.DIRECTORY")) return NETSCAPE_SERVER;
    // if(name.equalsIgnoreCase("REDHAT.DIRECTORY")) return REDHAT_SERVER;
    return DEFAULT_SERVER;
  }

}
