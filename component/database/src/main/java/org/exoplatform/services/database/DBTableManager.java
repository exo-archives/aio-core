/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
abstract public class DBTableManager {
  abstract public <T extends DBObject> void createTable(Class<T>  type,  boolean dropIfExist) throws Exception ;
  abstract public <T extends DBObject> void dropTable(Class<T>  type) throws Exception ;
  
  abstract public <T extends DBObject> boolean hasTable(Class<T> type)  throws Exception ;
  
  final static  public DBTableManager createDBTableManager(ExoDatasource datasource) {
    if(datasource.getDatabaseType() == ExoDatasource.HSQL_DB_TYPE) {
      return new StandardSQLTableManager(datasource)  ;
    } 
    return new StandardSQLTableManager(datasource) ;
  }
}