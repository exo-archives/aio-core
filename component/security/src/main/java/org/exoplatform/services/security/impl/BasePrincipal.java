/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.security.impl;

import java.security.Principal;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 28 avr. 2004
 */
public class BasePrincipal implements Principal {

  private String name;

  public BasePrincipal(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return getName();
  }
}
