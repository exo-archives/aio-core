/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.util.ArrayList;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Mar 30, 2007  
 */
public class StandardPageList<T extends DBObject> extends DBPageList<T> {

  public StandardPageList(int pageSize, StandardSQLDAO<T> dao){
    super(pageSize, dao);
    currentListPage_ = new ArrayList<T>();
  }

  protected void populateCurrentPage(int currentPage) throws Exception {
    this.currentPage_ = currentPage;
    StandardSQLDAO<T> standardSQLDAO = (StandardSQLDAO<T>) dao_;
    QueryBuilder queryBuilder = standardSQLDAO.getExoDatasource().getQueryBuilder();
    String query = queryBuilder.createSelectQuery(standardSQLDAO.getType(), -1); 
    currentListPage_.clear(); 
    standardSQLDAO.loadPageList(this, query);
  }

}
