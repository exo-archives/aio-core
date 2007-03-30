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

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public abstract  class DAO<T extends DBObject> {
  
  protected ExoDatasource datasource_ ;
  protected Mapper<T> mapper_; 
  
  public DAO(ExoDatasource datasource, Mapper<T> mapper) {
    datasource_ = datasource ;
    mapper_ = mapper;
  }
  
  public ExoDatasource getExoDatasource() { return datasource_ ; }
    
  abstract public T load(Class<T> type, long id) throws Exception ;
  abstract public T loadUnique(Class<T> type, String query) throws Exception ;
  
  abstract public void update(T bean) throws Exception ;
  abstract public void update(List<T> beans) throws Exception; 
  
  abstract public void save(T bean) throws Exception ;
  abstract public void save(List<T> beans) throws Exception; 
  
  abstract public void remove(T bean) throws Exception ;
  abstract public T remove(Class<T> type, long id) throws Exception ;
  
  abstract public T createInstance(Class<T> type) throws Exception ;
  
  public List<T> loadByQuery(Class<T> type, String query) throws Exception {
    List<T> list = new ArrayList<T>();
    loadInstances(query, type, list);
    return list;
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
  
  protected void execute(String query, List<T> beans) throws Exception {
    Connection connection = null;
    Statement statement = null;
    try{
      connection = datasource_.getConnection() ;
      statement = connection.createStatement() ;
      for(T bean : beans) {
        mapper_.mapSQL(bean, query) ;
      }
      statement.executeBatch();
      datasource_.commit(connection) ;
      System.out.println(" Executed queries "+query) ;
    } catch (Exception e) {
      throw e;
    } finally {
      if (statement != null) statement.close();
      if (connection != null) datasource_.closeConnection(connection) ; 
    }
  }
  
  protected T loadInstance(String query, Class<T> type) throws Exception {
    Connection connection = null;
    Statement statement = null;
    try{
      connection = datasource_.getConnection() ;
      statement = connection.createStatement() ;
      ResultSet resultSet =  statement.executeQuery(query) ;
      if(!resultSet.next()) return null ;
      T bean =  createInstance(type) ;
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
  
  protected void loadInstances(String loadQuery, Class<T> type, List<T> list) throws Exception {
    Connection connection = null;
    Statement statement = null;
    try{
      connection = datasource_.getConnection() ;
      statement = connection.createStatement() ;
      ResultSet resultSet =  statement.executeQuery(loadQuery) ;
      while (resultSet.next()) {
        T bean = createInstance(type) ;
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
  
}