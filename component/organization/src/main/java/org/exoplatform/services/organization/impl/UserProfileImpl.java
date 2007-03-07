/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.impl;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.services.organization.UserProfile;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 21, 2003
 * Time: 3:22:54 PM
 */
public class UserProfileImpl  implements UserProfile  {
	private String userName ;
  private Map<String,String> attributes ;

  public UserProfileImpl() {
  }
  
  public UserProfileImpl(String userName , Map<String,String> map) {
  	this.userName = userName ;
    attributes = map;
  }

  public String getUserName() { return userName ; }
  public void   setUserName(String s) { userName = s; }
  
  public Map<String, String>  getUserInfoMap() {
  	if(attributes == null) attributes = new HashMap<String, String>() ;
  	return this.attributes ; 
  }
  
  public void setUserInfoMap(Map<String, String> map) { this.attributes = map ; }
  
  public String getAttribute(String attName){
    return attributes.get(attName);
  }

  public void setAttribute(String key, String value){
    attributes.put(key, value);
  }
  
  public Map getAttributeMap() { return attributes ; }
}