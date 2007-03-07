/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.ldap;

import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 16, 2005
 */
public class ObjectClassAttributes extends BasicAttributes {
  public ObjectClassAttributes() {
    
  }
  
  public ObjectClassAttributes(String[] classes) {
    setClasses(classes) ;
  }
  
  public void setClasses(String[] classes) {
    BasicAttribute attr  = new BasicAttribute("objectClass") ;
    for(String clazz:  classes ) attr.add(clazz) ;
    put(attr) ;
  }
  
  public void setClasses(String classes) {
    String[] clazz = classes.split(",") ;
    BasicAttribute attr  = new BasicAttribute("objectClass") ;
    for(String c:  clazz ) attr.add(c) ;
    put(attr) ;
  }
  
  public void addAttribute(String key, Object value) { put(key, value) ; }
}
