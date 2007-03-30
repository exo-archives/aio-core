/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.util.List;

import org.exoplatform.commons.utils.PageList;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Mar 30, 2007  
 */
public abstract class DBPageList<T extends DBObject> extends PageList {
  
  protected DAO<T> dao_;
  
  protected DBPageList(int pageSize, DAO<T> dao){
    super(pageSize);
    dao_ = dao;
  }
  
  public List getAll() throws Exception { return dao_.loadAll(); }

  protected void setAvailablePage(String sqlCounter) throws Exception {
    Integer counter = dao_.<Integer>loadValue(sqlCounter);
    super.setAvailablePage(counter.intValue());
  }
  
}
