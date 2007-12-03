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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.ldap.CreateObjectCommand;
import org.exoplatform.services.ldap.DeleteObjectCommand;
import org.exoplatform.services.ldap.LDAPService;

/**
 * Created by The eXo Platform SAS        .
 * Author : James Chamberlain
 *          james@echamberlains.com
 * Date: 11/2/2005
 * 
 */
public class LDAPServiceImpl implements LDAPService, ComponentRequestLifecycle {

  private ThreadLocal<LdapContext> tlocal_ = new ThreadLocal<LdapContext >() ;

  private Map<String, String> env = null;

  private int serverType = DEFAULT_SERVER;  

  public LDAPServiceImpl(InitParams params) throws Exception {
    LDAPConnectionConfig config = (LDAPConnectionConfig)params.getObjectParam("ldap.config").getObject() ;

    String  url =  config.getProviderURL();    
    serverType = toServerType(config.getServerName());    

    boolean ssl = url.toLowerCase().startsWith("ldaps");
    if(serverType == ACTIVE_DIRECTORY_SERVER && ssl){
      String keystore = System.getProperty("java.home");
      keystore += File.separator+"lib"+File.separator+"security"+File.separator+"cacerts";   
      System.setProperty("javax.net.ssl.trustStore", keystore);
    }

    env = new HashMap<String, String>();
    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.SECURITY_AUTHENTICATION, config.getAuthenticationType());
    env.put(Context.SECURITY_PRINCIPAL, config.getRootDN());
    env.put(Context.SECURITY_CREDENTIALS,  config.getPassword());
    env.put("com.sun.jndi.ldap.connect.timeout", "60000");
    env.put("com.sun.jndi.ldap.connect.pool", "true");
    env.put("java.naming.ldap.version", config.getVerion());
    env.put("java.naming.ldap.attributes.binary","tokenGroups");
    env.put(Context.REFERRAL, config.getReferralMode());    

    Pattern pattern = Pattern.compile("\\p{Space}*,\\p{Space}*", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(url);
    if(ssl)
      url = matcher.replaceAll("/ ldaps://");
    else 
      url = matcher.replaceAll("/ ldap://");
    url += "/";    
    env.put(Context.PROVIDER_URL, url);   

    if(serverType == ACTIVE_DIRECTORY_SERVER && ssl) env.put(Context.SECURITY_PROTOCOL, "ssl");   
  }


  public LdapContext getLdapContext() throws Exception {
    //new Exception("===================================").printStackTrace() ;
    LdapContext context = tlocal_.get() ;    
    if(context == null) {
      context = new InitialLdapContext(new Hashtable<String, String>(env), null) ;     
      tlocal_.set(context) ;
    } else {     
      context.setRequestControls(null);    
    }
    return context;
  }


  public InitialContext getInitialContext() throws Exception {
    Hashtable<String, String> props = new Hashtable<String, String>(env);
    props.put(Context.OBJECT_FACTORIES, "com.sun.jndi.ldap.obj.LdapGroupFactory");
    props.put(Context.STATE_FACTORIES, "com.sun.jndi.ldap.obj.LdapGroupFactory");
    return  new InitialLdapContext(props, null);
  }


  public boolean authenticate(String userDN, String password) throws Exception {
    Hashtable<String, String> props = new Hashtable<String, String>(env);
    props.put(Context.SECURITY_AUTHENTICATION, "simple");
    props.put(Context.SECURITY_PRINCIPAL, userDN);
    props.put(Context.SECURITY_CREDENTIALS, password);
    props.put("com.sun.jndi.ldap.connect.pool", "false");
    new InitialLdapContext(props, null);
    return true;
  }

  public void addDeleteObject(ComponentPlugin plugin) throws Exception {
    DeleteObjectCommand command = (DeleteObjectCommand) plugin ;
    LdapContext ctx = getLdapContext() ;
    command.deleteObjects(ctx) ;
  }

  public void addCreateObject(ComponentPlugin plugin) throws Exception {
    CreateObjectCommand command = (CreateObjectCommand) plugin ;
    LdapContext ctx = getLdapContext() ;
    command.addObjects(ctx) ;
  }


  public void startRequest(ExoContainer container) {   }

  public void endRequest(ExoContainer container) {
    LdapContext context = tlocal_.get() ;
    if(context != null) {
      try {
        context.close() ;
        tlocal_.set(null);
      } catch(Exception ex) {
        ex.printStackTrace() ;
      }
    }
  }

  private int toServerType(String name){  
    name = name.trim();
    if(name == null || name.length() < 1)return DEFAULT_SERVER;     
    if(name.equalsIgnoreCase("ACTIVE.DIRECTORY")) return ACTIVE_DIRECTORY_SERVER;
//  if(name.equalsIgnoreCase("OPEN.LDAP"))return OPEN_LDAP_SERVER;
//  if(name.equalsIgnoreCase("NETSCAPE.DIRECTORY"))  return NETSCAPE_SERVER;
//  if(name.equalsIgnoreCase("REDHAT.DIRECTORY"))  return REDHAT_SERVER;   
    return DEFAULT_SERVER;
  }

  public int getServerType() {    
    return serverType; 
  } 
}
