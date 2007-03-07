/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.ldap;

import javax.naming.directory.BasicAttribute;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 15, 2005
 */
public class ObjectClassAttribute extends BasicAttribute {
  public ObjectClassAttribute(String[] classes) {
    super("objectClass") ;
    for(String clazz:  classes ) add(clazz) ;
  }
}
