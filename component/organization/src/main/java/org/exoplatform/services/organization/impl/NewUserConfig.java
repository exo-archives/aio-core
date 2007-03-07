/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 27, 2004
 * @version $Id: NewUserConfig.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class NewUserConfig {
  
  private List role ;
  
  private List group ;
  
  private HashSet ignoredUser ;
  
  public NewUserConfig() {
    role = new ArrayList(3) ;
    group = new ArrayList(3) ;
    ignoredUser = new HashSet() ;
  }
  
  public  List getRole()  { return role ; }
  public List getGroup() { return group ; }
  public HashSet getIgnoredUser() { return ignoredUser ; }
  public void    setIgnoredUser(String user) { ignoredUser.add(user) ; }
  
  public boolean isIgnoreUser(String user) { return ignoredUser.contains(user) ; }
  
  static public class JoinGroup {
    public String groupId ; 
    public String membership ;
    public boolean validateGroupId = true ;
    public boolean validateMembership = true ;
    
    public JoinGroup() { 
    }
    
    public String getGroupId() { return groupId ; }
    public void   setGroupId(String s) { groupId = s  ; }
    
    public String getMembership() { return membership ; }
    public void   setMembership(String s) { membership = s ; }
    
    public boolean getValidateGroupId() { return validateGroupId ; }
    public void    setValidateGroupId(boolean b) { validateGroupId = b ; }
    public void    setValidateGroupId(String b) { validateGroupId = "true".equals(b) ; }
    
    public boolean getValidateMembership() { return validateMembership ; }
    public void    setValidateMembership(boolean b) { validateMembership = b ; }
    public void    setValidateMembership(String b) { validateMembership = "true".equals(b) ; }
  }
} 