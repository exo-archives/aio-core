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
public class StandardDBObjectPageList<T extends DBObject> extends PageList {
  
  protected DAO<T> dao_;
  
  protected String query_;
  
  protected StandardDBObjectPageList(int pageSize, DAO<T> dao, String query, String queryCounter) throws Exception {
    super(pageSize);
    dao_ = dao;
    query_ = query;
    
    Integer counter = dao_.<Integer>loadDBField(queryCounter);
    super.setAvailablePage(counter.intValue());
  }
  
  protected void populateCurrentPage(int currentPage) throws Exception {
    this.currentPage_ = currentPage;   
    currentListPage_.clear(); 
    dao_.loadPageList(this, query_);
  }
  
  public List getAll() throws Exception { return null; }

}
