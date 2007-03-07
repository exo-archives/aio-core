/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.organization.ldap;

import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.Control;

import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.User;

/**
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
 */

public class ADUserDAOImpl extends UserDAOImpl {	
  
  int UF_ACCOUNTDISABLE = 0x0002;  
  int UF_PASSWD_NOTREQD = 0x0020;
  int UF_NORMAL_ACCOUNT = 0x0200;
  int UF_PASSWORD_EXPIRED = 0x800000;
  
  public ADUserDAOImpl(LDAPAttributeMapping ldapAttrMapping, LDAPService ldapService){
    super(ldapAttrMapping, ldapService);
    LDAPUserPageList.SEARCH_CONTROL = Control.CRITICAL;
  }
  
  public void createUser(User user, boolean broadcast) throws Exception {   
    String userDN = "cn="+user.getUserName()+","+ldapAttrMapping_.userURL;    
    Attributes attrs = ldapAttrMapping_.userToAttributes(user);
    attrs.put("userAccountControl", Integer.toString(
        UF_NORMAL_ACCOUNT+ UF_PASSWD_NOTREQD + UF_PASSWORD_EXPIRED+ UF_ACCOUNTDISABLE));
    attrs.remove(ldapAttrMapping_.userPassword);    
    if(broadcast) preSave(user, true);
    ldapService_.getLdapContext().createSubcontext(userDN, attrs);
    if(broadcast) postSave(user, true) ;   
    saveUserPassword(user, userDN);     
    
  }  
  
  void saveUserPassword(User user, String userDN) throws Exception {
    Object v = ldapService_.getLdapContext().getEnvironment().get(Context.SECURITY_PROTOCOL);
    if(v == null) return;
    String security = String.valueOf(v);
    if(!security.equalsIgnoreCase("ssl")) return;    
    String newQuotedPassword = "\""+user.getPassword()+"\"";
    byte[] newUnicodePassword = newQuotedPassword.getBytes("UTF-16LE");      
    ModificationItem[] mods = new ModificationItem[2];   
    mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
        new BasicAttribute(ldapAttrMapping_.userPassword, newUnicodePassword));
    mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
        "userAccountControl",Integer.toString(UF_NORMAL_ACCOUNT + UF_PASSWORD_EXPIRED)));    
    ldapService_.getLdapContext().modifyAttributes(userDN, mods);
  } 
}
