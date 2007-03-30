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
public  class StandardSQLDAO<T extends DBObject>  extends DAO<T> {
  
  public StandardSQLDAO(ExoDatasource datasource, Mapper<T> mapper) {
    super(datasource, mapper) ;
  }
  
  public T createInstance(Class<T> type) throws Exception { return type.newInstance(); }

  public T loadUnique(Class<T> type, String query) throws Exception {
    return super.loadInstance(query, type);
  }

  public  T load(Class<T> type, long id) throws Exception {
    return super.loadInstance(datasource_.getQueryManager().getSelectQuery(type, id), type);
  }
  
//  @SuppressWarnings("unchecked")
  public void update(List<T> list) throws Exception {
    if(list == null) throw new Exception("The given beans null ") ;
    if(list.size() < 1) return;
    for(T bean : list){
      if(bean.getId() < 0) {
        throw new Exception("The given bean " + bean.getClass() + " doesn't have an id") ;
      }
    }
    Class clazz = list.get(0).getClass();
    execute(datasource_.getQueryManager().getUpdateQuery(clazz, -1), list);
  }    
  
  public void update(T bean) throws Exception {
    String query = datasource_.getQueryManager().getUpdateQuery(bean.getClass(), bean.getId());
    execute(query, bean);
  }
  
  public void save(List<T> beans) throws Exception {
  }
  
  
  public void save(T bean) throws Exception {
    if(bean.getId() == -1) bean.setId(datasource_.getIDGenerator().generateLongId(bean));
    execute(datasource_.getQueryManager().getInsertQuery(bean.getClass(), bean.getId()), bean);
  }
  
  public T remove(Class<T> type, long id) throws Exception {
    T value = load(type, id);
    if(value == null) return null;
    execute(datasource_.getQueryManager().getRemoveQuery(type, id), (T)null);
    return value;
  }

  public void remove(T bean) throws Exception {
    execute(datasource_.getQueryManager().getRemoveQuery(bean.getClass(), bean.getId()), (T)null); 
  }
}