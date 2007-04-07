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
  
  protected long dbObjectId_ = -1 ;

  public long getDBObjectId() {  return dbObjectId_ ; }
  public void setDBObjectId(long id) { dbObjectId_ = id ; }
}