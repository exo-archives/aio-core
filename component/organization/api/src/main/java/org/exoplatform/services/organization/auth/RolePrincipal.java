/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.services.organization.auth;

import java.io.Serializable;
import java.security.Principal;

/**
 * Created y the eXo platform team
 * User:  Tuan Nguyen
 * Date: May 6th, 2007
 */
public class RolePrincipal implements Principal, Serializable {

  private String name;

  public RolePrincipal(String name) {
    this.name = name;
  }

  public String getName() { return name; }

  public String toString() {  return getName();  }
}
