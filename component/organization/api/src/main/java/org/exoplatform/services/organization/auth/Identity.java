/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.auth;

import javax.security.auth.Subject;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * May 17, 2007  
 */
public class Identity {
  private String  sessionId_ ;
  private String  username_ ;
  private Subject subject_ ;
  
  public Identity(String sessionId, String username, String password) {
    this(sessionId, username, new Subject()) ;
  }
  
  public Identity(String sessionId, String username, Subject subject) {
    sessionId_ = sessionId ;
    username_ = username ;
    subject_  =  subject ;
  }
  
  public String getSessionId() { return sessionId_ ; }
  public String getUsername()  { return username_ ; }
  public Subject getSubject()  { return subject_ ;}
  
}
