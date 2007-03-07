/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.security.impl;

import org.exoplatform.services.security.RolePrincipal;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 28 avr. 2004
 */
public class RolePrincipalImpl extends BasePrincipal
    implements RolePrincipal{
  public RolePrincipalImpl(String name) {
    super(name);
  }
}
