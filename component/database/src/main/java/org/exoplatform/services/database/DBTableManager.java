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
 * 
 * The DBTableManager is an interface to help the developer to check if a table is existed or not 
 * and create/drop a table. We can implement more one more DBTableManager according to the 
 * database type and versio
 */
abstract public class DBTableManager {
  /**
   * This method should:
   * 1. Extract the table information such table name  and table fields from the class T, the information
   *    are annotated in the class T
   * 2. Generate the sql statement and create the table
   * @param <T>
   * @param type
   * @param dropIfExist
   * @throws Exception
   */
  abstract public <T extends DBObject> void createTable(Class<T>  type,  boolean dropIfExist) throws Exception ;
  /**
   * This method should:
   * 1. Extract the table information from the class T, the information are annotated in the class T 
   * 2. Generate the sql statement and drop the table
   * @param <T>
   * @param type
   * @throws Exception
   */
  abstract public <T extends DBObject> void dropTable(Class<T>  type) throws Exception ;
  /**
   * This method should:
   * 1. Extract the table information from the class T, the information are annotated in the class T 
   * 2. Check to see if the table is existed in the database system
   * @param <T>
   * @param type
   * @return
   * @throws Exception
   */
  abstract public <T extends DBObject> boolean hasTable(Class<T> type)  throws Exception ;
  /**
   * This method should check the database type and version  and create a corresponded DBTableManager
   * @param datasource
   * @return
   */
  final static  public DBTableManager createDBTableManager(ExoDatasource datasource) {
    if(datasource.getDatabaseType() == ExoDatasource.HSQL_DB_TYPE) {
      return new StandardSQLTableManager(datasource)  ;
    } else if (ExoDatasource.ORACLE_DB_TYPE == datasource.getDatabaseType()) {
      return new OracleTableManager(datasource);
    } else if(ExoDatasource.SQL_SERVER_TYPE == datasource.getDatabaseType()) {
      return new MSSQLServerTableManager(datasource);
    }
    return new StandardSQLTableManager(datasource) ;
  }
}