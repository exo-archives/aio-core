/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public class IDGenerator {
  public <T extends DBObject> long generateLongId(T bean) throws Exception {
    return 0;
  }

  public <T extends DBObject> long generatetLongId(Class<T> type) throws Exception {
    return 0;
  }

  
  public <T extends DBObject> String generateUUID(T bean) throws Exception {
    return null;
  }

  public <T extends DBObject> String generateUUID(Class<T> type) throws Exception {
    return null;
  }
}