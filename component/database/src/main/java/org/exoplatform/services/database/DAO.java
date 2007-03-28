/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public abstract  class DAO<T extends DBObject> {
  protected ExoDatasource datasource_ ;
  
  public DAO(ExoDatasource ds) {
    datasource_ = ds ;
  }
  
  public ExoDatasource getExoDatasource() { return datasource_ ; }
  
  abstract public T load(Class<T> type, long id) throws Exception ;
  abstract public T  update(T bean) throws Exception ;
  abstract public T save(T bean,  long id) throws Exception ;
  abstract public T remove(T bean, long id) throws Exception ;
  
  abstract protected void mapUpdate(T bean, PreparedStatement  statement) throws Exception ;  
  abstract protected void mapResultSet(ResultSet res, T bean) throws Exception ;
  
  abstract public T createInstance(Class<T> type) throws Exception ;
}