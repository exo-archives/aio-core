/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.impl;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 *
 * @hibernate.class  table="EXO_USER_PROFILE"
 */
public class UserProfileData {
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

  /**
   * @hibernate.id  generator-class="assigned"
   **/
  public String  getUserName() { return userName ; }
  public void  setUserName(String s) { this.userName = s ; } 

  /**
   * @hibernate.property length="65535" type="org.exoplatform.services.database.impl.TextClobType"
   **/
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