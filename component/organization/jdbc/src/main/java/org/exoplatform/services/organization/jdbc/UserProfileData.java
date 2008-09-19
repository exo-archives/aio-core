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
package org.exoplatform.services.organization.jdbc;

import org.exoplatform.services.database.DBObject;
import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.annotation.TableField;
import org.exoplatform.services.organization.impl.UserProfileImpl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Created by The eXo Platform SAS Author : Nhu Dinh Thuan
 * nhudinhthuan@exoplatform.com Apr 8, 2007
 */
@Table(name = "EXO_USER_PROFILE", field = {
    @TableField(name = "USER_NAME", type = "string", length = 100, unique = true, nullable = false),
    @TableField(name = "PROFILE", type = "string", length = 2000) })
public class UserProfileData extends DBObject {
  // TODO: use jibx
  static transient private XStream xstream_;

  private String                   userName;

  private String                   profile;

  public UserProfileData() {
  }

  public UserProfileData(String userName) {
    StringBuffer b = new StringBuffer();
    b.append("<user-profile>\n").append("  <userName>").append(userName).append("</userName>\n");
    b.append("</user-profile>\n");
    this.userName = userName;
    this.profile = b.toString();
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String s) {
    this.userName = s;
  }

  public String getProfile() {
    return profile;
  }

  public void setProfile(String s) {
    profile = s;
  }

  public org.exoplatform.services.organization.UserProfile getUserProfile() {
    XStream xstream = getXStream();
    UserProfileImpl up = (UserProfileImpl) xstream.fromXML(profile);
    return up;
  }

  public void setUserProfile(org.exoplatform.services.organization.UserProfile up) {
    if (up == null)
      profile = "";
    UserProfileImpl impl = (UserProfileImpl) up;
    userName = up.getUserName();
    XStream xstream = getXStream();
    profile = xstream.toXML(impl);
  }

  static private XStream getXStream() {
    if (xstream_ == null) {
      xstream_ = new XStream(new XppDriver());
      xstream_.alias("user-profile", UserProfileImpl.class);
    }
    return xstream_;
  }
}
