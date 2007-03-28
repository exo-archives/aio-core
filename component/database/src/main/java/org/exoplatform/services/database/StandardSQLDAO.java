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

import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.annotation.TableField;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
abstract public  class StandardSQLDAO<T extends DBObject>  extends DAO<T> {
  
  public StandardSQLDAO(ExoDatasource ds) {
    super(ds) ;
  }

  public  T load(Class<T> type, long id) throws Exception {
    Table table = type.getAnnotation(Table.class) ;
    String loadQuery =  "SELECT * FROM " + table.name() + " WHERE id = " +  id ;
    System.out.println("LOAD QUERY: " + loadQuery) ;
    T bean =  createInstance(type) ;
    Connection conn = datasource_.getConnection() ;
    Statement statement = conn.createStatement() ;
    ResultSet res =  statement.executeQuery(loadQuery) ;
    if(!res.next())  return null ;
    mapResultSet(res, bean) ;
    statement.close() ;
    res.close() ;
    return bean ;
  }
    
  public List<T> load(Class<T> type, String loadQuery) throws Exception {
    Connection conn = datasource_.getConnection() ;
    Statement statement = conn.createStatement() ;
    ResultSet res =  statement.executeQuery(loadQuery) ;
    List<T> list = new ArrayList<T>();
    while (res.next()) {
      T bean =  createInstance(type) ;
      mapResultSet(res, bean) ;
      list.add(bean) ;
    }
    statement.close() ;
    res.close() ;
    return list ;
  }
  
  @SuppressWarnings("unchecked")
  public  T update(T bean) throws Exception {
    if(bean.getId() < 0) {
      throw new Exception("The given bean " + bean.getClass() + " doesn't have an id") ;
    }
    Connection conn = getExoDatasource().getConnection() ;
    Class<T>  type = (Class<T>)bean.getClass() ;
    PreparedStatement statement = conn.prepareStatement(getUpdateQuery(type, bean.getId())) ;
    mapUpdate(bean, statement) ;
    statement.executeUpdate() ;
    statement.close() ;
    datasource_.commit(conn) ;
    datasource_.closeConnection(conn) ;
    return bean ;
  }
  
  @SuppressWarnings("unchecked")
  public  T save(T bean,  long id) throws Exception {
    Connection conn = datasource_.getConnection() ;
    Class<T>  type = (Class<T>)bean.getClass() ;
    PreparedStatement statement = conn.prepareStatement(getInsertQuery(type, id)) ;
    bean.setId(id) ;
    mapUpdate(bean, statement) ;
    System.out.println("INSERT STATUS: " + statement.executeUpdate() + ", id = " + id) ;
    statement.close() ;
    datasource_.commit(conn) ;
    datasource_.closeConnection(conn) ;
    return bean ;
  }
  
  public  T remove(T bean, long id) throws Exception {
    return bean ;
  }
  
  
  protected String  getUpdateQuery(Class<T> type, long id) throws Exception {
    Table table = type.getAnnotation(Table.class) ;
    String updateQuery = 
      "UPDATE " + table.name() + " SET " ; 
    TableField[]  fields =  table.field() ;
    for(int i = 0; i <  fields.length; i++) {
      TableField  field =  fields[i] ;
      updateQuery += field.name() + " = ?" ;
      if (i !=  fields.length - 1)  updateQuery += ", " ;      
    }
    updateQuery += 
      " WHERE id = " +  id ;
    return updateQuery ;
  }
  
  protected String  getInsertQuery(Class<T> type, long id) throws Exception {
    Table table = type.getAnnotation(Table.class) ;
    TableField[]  fields =  table.field() ;
    StringBuilder query = new StringBuilder(500) ;
    query. 
      append("INSERT INTO " + table.name() + "(id, ") ;
    for(int i = 0; i <  fields.length; i++) {
      TableField  field =  fields[i] ;
      query.append(field.name()) ;
      if (i !=  fields.length - 1)  query.append(", ") ;
    }
    query.append(")") ;
    query.append(" VALUES(").append(id).append(", ");
    for(int i = 0; i <  fields.length; i++) {
//      TableField  field =  fields[i] ;
      query.append("?") ;
//      if(i ==  field.length() - 1)  query.append(", ") ;
      if (i !=  fields.length - 1)  query.append(", ") ;
    }
    query.append(")") ;
    System.out.println("INSERT QUERY: " + query.toString());
    return query.toString() ;
  }
  
  protected String  getRemoveQuery(Class<T> type, long id) throws Exception {
    Table table = type.getAnnotation(Table.class) ;
    return "DELETE FROM " + table.name() + " WHERE id = '" +  id  + "'" ;
  }
}