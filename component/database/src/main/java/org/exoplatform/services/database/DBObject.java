/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * Mar 15, 2007  
 */
abstract public class DBObject {
  
  private long id_ = -1 ;

  public long getId() {  return id_ ; }
  public void setId(long id) { id_ = id ; }
}