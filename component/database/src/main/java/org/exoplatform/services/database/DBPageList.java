/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.exoplatform.commons.utils.PageList;

import com.sun.rowset.CachedRowSetImpl;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Mar 30, 2007  
 */
public class DBPageList<T extends DBObject> extends PageList {
  
  protected DAO<T> dao_;
  
  protected String query_;
  
  public DBPageList(int pageSize, DAO<T> dao, DBObjectQuery<T> query) throws Exception {
    super(pageSize);
    dao_ = dao;
    query_ = query.toQuery();
    
    Integer counter = dao_.<Integer>loadDBField(query.toCountQuery());
    super.setAvailablePage(counter.intValue());
  }
  
  public DBPageList(int pageSize, DAO<T> dao, String query, String queryCounter) throws Exception {
    super(pageSize);
    dao_ = dao;
    query_ = query;
    
    Integer counter = dao_.<Integer>loadDBField(queryCounter);
    super.setAvailablePage(counter.intValue());
  }
  
  protected void populateCurrentPage(int currentPage) throws Exception {
    this.currentPage_ = currentPage;   
    currentListPage_.clear(); 
    loadPageList(this, query_);
  }
  
  @SuppressWarnings("unchecked")
  private void loadPageList(DBPageList<T> pageList, String query) throws Exception {
    Connection connection = null;
    try {
      connection = dao_.getExoDatasource().getConnection() ;
      Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      ResultSet resultSet = statement.executeQuery(query);

      CachedRowSet crs = new CachedRowSetImpl();
      crs.setPageSize(pageList.getPageSize());
      crs.populate(resultSet, (pageList.getCurrentPage() - 1) * pageList.getPageSize() + 1);

      List<T>  list = pageList.currentPage();
      while (resultSet.next()) {
        T bean = dao_.createInstance() ;
        dao_.getDBObjectMapper().mapResultSet(resultSet, bean) ;
        list.add(bean) ;
      }
      resultSet.close() ;
      statement.close();
    } catch (Exception e) {
      throw e;
    } finally {
      dao_.getExoDatasource().closeConnection(connection) ;  
    }
  }
  
  public List<T> getAll() throws Exception { 
    Connection connection = null;
    try {
      connection = dao_.getExoDatasource().getConnection() ;
      Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      ResultSet resultSet = statement.executeQuery(query_);
      
      List<T>  list = new ArrayList<T>();
      while (resultSet.next()) {
        T bean = dao_.createInstance() ;
        dao_.getDBObjectMapper().mapResultSet(resultSet, bean) ;
        list.add(bean) ;
      }
      resultSet.close() ;
      statement.close();
      return list;
    } catch (Exception e) {
      throw e;
    } finally {
      dao_.getExoDatasource().closeConnection(connection) ;  
    }
  }

}
