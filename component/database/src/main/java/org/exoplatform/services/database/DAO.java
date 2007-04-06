/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.listener.ListenerService;

import com.sun.rowset.CachedRowSetImpl;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public abstract class DAO<T extends DBObject> {
  
  protected ExoDatasource datasource_ ;
  protected DBObjectMapper<T> mapper_; 
  
  protected ListenerService listeners_;
  
  public DAO(ListenerService listeners, ExoDatasource datasource) {
    datasource_ = datasource ;
    mapper_ = new ReflectionMapper<T>();
    listeners_ = listeners;
  }
  
  public DAO(ListenerService listenerService, ExoDatasource datasource, DBObjectMapper<T> mapper) {
    datasource_ = datasource ;
    mapper_ = mapper;
    listeners_ = listenerService;
  }
  
  public ExoDatasource getExoDatasource() { return datasource_ ; }
    
  abstract public T load(long id) throws Exception ;
  abstract public List<T> loadAll() throws Exception ;
  
  abstract public void update(T bean) throws Exception ;
  abstract public void update(List<T> beans) throws Exception; 
  
  abstract public void save(T bean) throws Exception ;
  abstract public void save(List<T> beans) throws Exception; 
  
  abstract public void remove(T bean) throws Exception ;
  abstract public T remove(long id) throws Exception ;
  
  abstract public T createInstance() throws Exception ;
  
  public List<T> loadByQuery(String query) throws Exception {
    List<T> list = new ArrayList<T>();
    loadInstances(query, list);
    return list;
  }
  
  @SuppressWarnings("unchecked")
  public void loadPageList(DBPageList<T> pageList, String query) throws Exception {
    Connection connection = null;
    Statement statement = null;
    try {
      connection = datasource_.getConnection() ;
      statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      ResultSet resultSet = statement.executeQuery(query);
      
      CachedRowSet crs = new CachedRowSetImpl();
      crs.setPageSize(pageList.getPageSize());
      crs.populate(resultSet, (pageList.getCurrentPage() - 1) * pageList.getPageSize() + 1);
      
      List<T>  list = pageList.currentPage();
      while (resultSet.next()) {
        T bean = createInstance() ;
        mapper_.mapResultSet(resultSet, bean) ;
        list.add(bean) ;
      }
      resultSet.close() ;
    } catch (Exception e) {
      throw e;
    } finally {
      if (statement != null) statement.close();
      if (connection != null) datasource_.closeConnection(connection) ; 
    }
  }
  
  @SuppressWarnings("unchecked")
  public <E> E loadValue(String query) throws Exception {
    Connection connection = null;
    Statement statement = null;
    try{
      connection = datasource_.getConnection() ;
      statement = connection.createStatement() ;
      ResultSet resultSet =  statement.executeQuery(query) ;
      if(!resultSet.next()) return null ;
      E value =  (E)resultSet.getObject(1);
      statement.close() ;
      resultSet.close() ;
      return value ;
    } catch (Exception e) {
      throw e;
    } finally {
      if (statement != null) statement.close();
      if (connection != null) datasource_.closeConnection(connection) ; 
    }
  }
  
  protected T loadInstance(String query) throws Exception {
    Connection connection = null;
    Statement statement = null;
    try{
      connection = datasource_.getConnection() ;
      statement = connection.createStatement() ;
      ResultSet resultSet =  statement.executeQuery(query) ;
      if(!resultSet.next()) return null ;
      T bean =  createInstance() ;
      invokeEvent("load", bean);
      mapper_.mapResultSet(resultSet, bean) ;
      statement.close() ;
      resultSet.close() ;
      return bean ;
    } catch (Exception e) {
      throw e;
    } finally {
      if (statement != null) statement.close();
      if (connection != null) datasource_.closeConnection(connection) ; 
    }
  }
  
  protected void loadInstances(String loadQuery, List<T> list) throws Exception {
    Connection connection = null;
    Statement statement = null;
    try{
      connection = datasource_.getConnection() ;
      statement = connection.createStatement() ;
      ResultSet resultSet =  statement.executeQuery(loadQuery) ;
      while (resultSet.next()) {
        T bean = createInstance() ;
        mapper_.mapResultSet(resultSet, bean) ;
        invokeEvent("load", bean);
        list.add(bean) ;
      }
      resultSet.close() ;
    } catch (Exception e) {
      throw e;
    } finally {
      if (statement != null) statement.close();
      if (connection != null) datasource_.closeConnection(connection) ; 
    }
  }
  
  protected void execute(String query, T bean) throws Exception {
    Connection connection = null;
    PreparedStatement statement = null;
    try{
      connection = datasource_.getConnection() ;
      statement = connection.prepareStatement(query) ;
      if(bean != null){
        mapper_.mapUpdate(bean, statement) ;
      }
      System.out.println(" Executed query "+query) ;
      statement.executeUpdate() ;
      datasource_.commit(connection) ;
    } catch (Exception e) {
      throw e;
    } finally {
      if (statement != null) statement.close();
      if (connection != null) datasource_.closeConnection(connection) ; 
    }
  }
  
  protected void execute(String sqlAction, String template, List<T> beans) throws Exception {
    Connection connection = null;
    Statement statement = null;
    try{
      connection = datasource_.getConnection() ;
      statement = connection.createStatement() ;
      QueryBuilder builder = datasource_.getQueryBuilder();
      for(T bean : beans) {
        String query = builder.mapDataToSql(template, mapper_.toParameters(bean));
        statement.addBatch(query);
        System.out.println(" addBatch "+query) ;
      }
      int [] values = statement.executeBatch();
      for(int i = 0; i < values.length; i++) {
        if(values[i] != 1) continue;
        invokeEvent(sqlAction, beans.get(i));
      }
      datasource_.commit(connection) ;
    } catch (Exception e) {
      throw e;
    } finally {
      if (statement != null) statement.close();
      if (connection != null) datasource_.closeConnection(connection) ; 
    }
  }
  
  protected void invokeEvent(String prefix, T bean)  {    
    Table table = bean.getClass().getAnnotation(Table.class);
    DBObjectEvent<T> event  = new DBObjectEvent<T>(bean);
    StringBuilder builder = new StringBuilder(prefix).append('.').append(table.name());
    listeners_.invoke(builder.toString(), event);
  }
  
}