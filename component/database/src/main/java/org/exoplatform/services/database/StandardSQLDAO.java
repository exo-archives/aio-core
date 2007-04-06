/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.util.List;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public  class StandardSQLDAO<T extends DBObject> extends DAO<T> {
  
  private Class<T> type_;
  
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
    return super.loadInstance(datasource_.getQueryBuilder().createSelectQuery(type_, id));
  }
  
  public List<T> loadAll() throws Exception {
    return super.loadByQuery(datasource_.getQueryBuilder().createSelectQuery(type_, -1));
  }
  
  @SuppressWarnings("unchecked")
  public void update(List<T> list) throws Exception {
    if(list == null) throw new Exception("The given beans null ") ;
    if(list.size() < 1) return;
    for(T bean : list){
      if(bean.getId() < 0) {
        throw new Exception("The given bean " + bean.getClass() + " doesn't have an id") ;
      }
    }
    execute(datasource_.getQueryBuilder().createUpdateQuery(type_), list);
  }    
  
  public void update(T bean) throws Exception {
    String query = datasource_.getQueryBuilder().createUpdateQuery(type_, bean.getId());
    execute(query, bean);
  }
  
  @SuppressWarnings("unchecked")
  public void save(List<T> list) throws Exception {
    if(list == null) throw new Exception("The given beans null ") ;
    if(list.size() < 1) return;
    for(T bean  : list) {
      if(bean.getId() == -1) bean.setId(datasource_.getIDGenerator().generateLongId(bean));
    }
    execute(datasource_.getQueryBuilder().createInsertQuery(type_), list);
  }
  
  public void save(T bean) throws Exception {
    if(bean.getId() == -1) bean.setId(datasource_.getIDGenerator().generateLongId(bean));
    execute(datasource_.getQueryBuilder().createInsertQuery(bean.getClass(), bean.getId()), bean);
  }
  
  public T remove(long id) throws Exception {
    T value = load(id);
    if(value == null) return null;
    execute(datasource_.getQueryBuilder().createRemoveQuery(type_, id), (T)null);
    return value ;
  }

  public void remove(T bean) throws Exception {
    execute(datasource_.getQueryBuilder().createRemoveQuery(type_, bean.getId()), (T)null); 
  }

  public Class<T> getType() { return type_; }
}