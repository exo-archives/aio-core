/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.util.List;

import org.exoplatform.commons.utils.PageList;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public  class StandardSQLDAO<T extends DBObject> extends DAO<T> {
  
  protected Class<T> type_;
  
  public StandardSQLDAO(ExoDatasource datasource, Class<T> type) {
    super(datasource);
    this.type_ = type;
  }
  
  public StandardSQLDAO(ExoDatasource datasource, DBObjectMapper<T> mapper, Class<T> type) {
    super(datasource, mapper) ;
    this.type_ = type;
  }
  
  public T createInstance() throws Exception { return type_.newInstance(); }

  public T load(long id) throws Exception {
    return super.loadUnique(eXoDS_.getQueryBuilder().createSelectQuery(type_, id));
  }
  
  public PageList loadAll() throws Exception {
    QueryBuilder queryBuilder = eXoDS_.getQueryBuilder();
    String query = queryBuilder.createSelectQuery(type_, -1);
    StringBuilder queryCounter = new StringBuilder("SELECT COUNT(*) ");
    queryCounter.append(query.substring(query.indexOf("FROM")));
    return new DBPageList<T>(20, this, query, queryCounter.toString());
  }
  
  @SuppressWarnings("unchecked")
  public void update(List<T> list) throws Exception {
    if(list == null) throw new Exception("The given beans null ") ;
    if(list.size() < 1) return;
    for(T bean : list){
      if(bean.getDBObjectId() < 0) {
        throw new Exception("The given bean " + bean.getClass() + " doesn't have an id") ;
      }
    }
    execute(eXoDS_.getQueryBuilder().createUpdateQuery(type_), list);
  }    
  
  public void update(T bean) throws Exception {
    String query = eXoDS_.getQueryBuilder().createUpdateQuery(type_, bean.getDBObjectId());
    execute(query, bean);
  }
  
  @SuppressWarnings("unchecked")
  public void save(List<T> list) throws Exception {
    if(list == null) throw new Exception("The given beans null ") ;
    if(list.size() < 1) return;
    for(T bean  : list) {
      if(bean.getDBObjectId() != -1) continue;
      bean.setDBObjectId(eXoDS_.getIDGenerator().generateLongId(bean));
    }
    execute(eXoDS_.getQueryBuilder().createInsertQuery(type_), list);
  }
  
  public void save(T bean) throws Exception {
    if(bean.getDBObjectId() == -1) bean.setDBObjectId(eXoDS_.getIDGenerator().generateLongId(bean));
    execute(eXoDS_.getQueryBuilder().createInsertQuery(bean.getClass(), bean.getDBObjectId()), bean);
  }
  
  
  public T remove(long id) throws Exception {
    T bean = load(id);
    if(bean == null) return null;
    execute(eXoDS_.getQueryBuilder().createRemoveQuery(type_, id), (T)null);
    return bean ;
  }

  public void remove(T bean) throws Exception {
    execute(eXoDS_.getQueryBuilder().createRemoveQuery(type_, bean.getDBObjectId()), (T)null);
  }

  
//  protected void invokeEvent(String prefix, String action, T bean) throws Exception   {    
//    Table table = bean.getClass().getAnnotation(Table.class);    
//    StringBuilder builder = new StringBuilder(prefix).append('.').append(action).append('.').append(table.name());
//    DBObjectEvent<StandardSQLDAO, T> event  = new DBObjectEvent<StandardSQLDAO, T>(builder.toString(), this, bean);
//    listenerService_.broadcast(event);
//  }
  
  public Class<T> getType() { return type_; }
}