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

import org.exoplatform.services.organization.CryptPassword;

/**
 * Created by The eXo Platform SAS
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
