/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.util.HashMap;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 22, 2004
 * @version $Id: XResources.java 5332 2006-04-29 18:32:44Z geaz $
 */
@SuppressWarnings("serial")
public class XResources extends HashMap<Class,Object> {

  public Object getResource(Class cl)  { return get(cl) ; }
  public XResources  addResource(Class cl , Object resource) {
    put(cl, resource) ;
    return this ;
  }
  
  public Object removeResource(Class cl) {  return remove(cl) ; }
}  