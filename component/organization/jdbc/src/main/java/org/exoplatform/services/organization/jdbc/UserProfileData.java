/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import org.exoplatform.services.database.DBObject;
import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.annotation.TableField;
import org.exoplatform.services.organization.impl.UserProfileImpl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Apr 8, 2007  
 */
@Table(
    name = "UserProfile" ,
    field = {
        @TableField(name = "userName", type = "string", length = 100, unique = true, nullable = false),
        @TableField(name = "profile", type = "string", length = 2000)
    }
)
public class UserProfileData extends DBObject {
  //TODO:  use jibx
  static transient private XStream xstream_ ; 
  
  private String userName ;
  
  private String profile ;

  public UserProfileData() {
  }
  
  public UserProfileData(String userName) {
    StringBuffer b = new StringBuffer() ;
    b.append("<user-profile>\n").
      append("  <userName>").append(userName).append("</userName>\n");
    b.append("</user-profile>\n");
    this.userName = userName ;
    this.profile = b.toString() ;
  }

  public String  getUserName() { return userName ; }
  public void  setUserName(String s) { this.userName = s ; } 

  public String getProfile() { return profile ; }
  public void setProfile(String s) { profile = s; }

  public org.exoplatform.services.organization.UserProfile getUserProfile() {
    XStream xstream = getXStream() ;
    UserProfileImpl up = (UserProfileImpl)xstream.fromXML( profile) ;
    return up ;
  }

  public void setUserProfile(org.exoplatform.services.organization.UserProfile up) { 
    if(up == null) profile = "";
    UserProfileImpl impl = (UserProfileImpl) up ;
    userName = up.getUserName() ;
    XStream xstream = getXStream() ;
    profile = xstream.toXML(impl) ;  
  }
  
  static private XStream  getXStream() {
    if (xstream_ == null) {
      xstream_ = new XStream(new XppDriver());
      xstream_.alias("user-profile", UserProfileImpl.class);
    }
    return xstream_ ;
  }
}
