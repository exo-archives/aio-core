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
package org.exoplatform.services.organization.impl;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.services.organization.UserProfile;

/**
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: Aug 21, 2003 Time: 3:22:54 PM
 */
public class UserProfileImpl implements UserProfile {
  private String              userName;

  private Map<String, String> attributes;

  public UserProfileImpl() {
  }

  public UserProfileImpl(String userName) {
    this.userName = userName;
    attributes = new HashMap<String, String>();
  }

  public UserProfileImpl(String userName, Map<String, String> map) {
    this.userName = userName;
    attributes = map;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String s) {
    userName = s;
  }

  public Map<String, String> getUserInfoMap() {
    if (attributes == null)
      attributes = new HashMap<String, String>();
    return this.attributes;
  }

  public void setUserInfoMap(Map<String, String> map) {
    this.attributes = map;
  }

  public String getAttribute(String attName) {
    return attributes.get(attName);
  }

  public void setAttribute(String key, String value) {
    attributes.put(key, value);
  }

  public Map getAttributeMap() {
    return attributes;
  }
}
