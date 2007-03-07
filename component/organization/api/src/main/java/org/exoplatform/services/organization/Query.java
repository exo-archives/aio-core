/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization;

import java.util.Date ;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public class Query {
  private String userName_ ;
  private String fname_ ;
  private String lname_ ;
  private String email_ ;
  private Date from_ ;
  private Date to_ ;
  
  public Query() {
  }
  
  public String getUserName() { return userName_ ; }
  public void   setUserName(String s) { userName_ = s ; }
  
  public String getFirstName() { return fname_ ; }
  public void   setFirstName(String s) { fname_ = s ; }
  
  public String getLastName() { return lname_ ; }
  public void   setLastName(String s) { lname_ = s ; }
  
  public String getEmail() { return email_ ; }
  public void   setEmail(String s) { email_ = s ; }
  
  public Date getFromLoginDate() { return from_ ; }
  public void setFromLoginDate(Date d) { from_ = d ; }
  
  public Date getToLoginDate() { return to_ ; }
  public void setToLoginDate(Date d) { to_ = d ; }
}