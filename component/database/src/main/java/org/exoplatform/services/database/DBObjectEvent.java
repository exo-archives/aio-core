/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import org.exoplatform.services.listener.Event;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Apr 6, 2007  
 */
public class DBObjectEvent<E extends DAO,T extends DBObject> extends Event<E,T> {
  
  public DBObjectEvent(String name, E e, T t) {
    super(name, e, t);
  }

}
