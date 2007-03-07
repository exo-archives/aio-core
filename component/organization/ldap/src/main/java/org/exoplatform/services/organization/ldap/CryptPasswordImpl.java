/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.ldap;

import org.exoplatform.services.organization.CryptPassword;

/**
 * Created by The eXo Platform SARL
 * Author : Thuannd
 *         nhudinhthuan@yahoo.com
 * Feb 20, 2006
 */
public class CryptPasswordImpl implements CryptPassword {
  
  public byte[] encrypt(String password) throws Exception{
    byte passwordDigest[] = null;
    String newQuotedPassword = "\"" + password + "\"";
    passwordDigest = newQuotedPassword.getBytes("UTF-16LE");
    return passwordDigest;
  }
  
  public String decrypt(byte[] data) throws Exception{
    String password = new String(data, "UTF-16LE");
    if(password.length() > 2)
      password = password.substring(1, password.length()-1);   
    return password;
  }

}
