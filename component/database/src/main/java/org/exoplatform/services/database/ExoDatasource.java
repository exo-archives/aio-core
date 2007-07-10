/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.sql.DataSource;

import org.exoplatform.services.database.table.IDGenerator;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public class ExoDatasource {
  
  final public static int STANDARD_DB_TYPE  = 0;
  final public static int HSQL_DB_TYPE  = 1;
  final public static int MYSQL_DB_TYPE  = 2;
  final public static int DB2_DB_TYPE  = 3;
  final public static int DERBY_DB_TYPE  = 4;
  final public static int ORACLE_DB_TYPE  = 5;
  final public static int SQL_SERVER_TYPE = 6;
  //TODO need remove
  static int totalGetConnect = 0;
//  static int totalCommit = 0;
//  static int totalCloseConnect = 0;
  final public static int MSSQL_DB_TYPE  = 6;
  final public static int SYSBASE_DB_TYPE  = 7;
  final public static int POSTGRES_DB_TYPE  = 8;
  
  private DataSource xaDatasource_  ;
  private DBTableManager tableManager_ ;
  private IDGenerator  idGenerator_ ;
  private QueryBuilder queryManager_;
  private String databaseName_ ;
  private String databaseVersion_ ;
  private int dbType_ = STANDARD_DB_TYPE;
  Connection conn ;
  
  public ExoDatasource(DataSource ds) throws Exception {
    xaDatasource_ = ds ;
    DatabaseMetaData metaData = ds.getConnection().getMetaData() ;
    databaseName_ = metaData.getDatabaseProductName() ;
    databaseVersion_ = metaData.getDatabaseProductVersion() ;
  
     
    String dbname = databaseName_.toLowerCase() ;
    System.out.println("\n\n\n\n------->DB Name: " + dbname + "\n\n\n\n");
    if(dbname.indexOf("oracle") >= 0) {
      dbType_ = ORACLE_DB_TYPE ;
    } else if(dbname.indexOf("hsql") >= 0) {
      dbType_ = HSQL_DB_TYPE ;
    } else if(dbname.indexOf("mysql") >= 0) {
      dbType_ = MYSQL_DB_TYPE ;
    } else if(dbname.indexOf("derby") >= 0) {
      dbType_ = DERBY_DB_TYPE ;
    } else if(dbname.indexOf("db2") >= 0) {
      dbType_ = DB2_DB_TYPE ;
    }else if(dbname.indexOf("server") >= 0) {
      dbType_ = SQL_SERVER_TYPE;
    } else {
      dbType_ = STANDARD_DB_TYPE ;
    }
    
    tableManager_ = DBTableManager.createDBTableManager(this) ;
    idGenerator_ = new IDGenerator(this) ;
    queryManager_ = new QueryBuilder(dbType_);
  }
  
  public DataSource getDatasource() { return xaDatasource_ ;}

  public Connection getConnection() throws Exception {
    return xaDatasource_.getConnection() ;
  }
  
  public void closeConnection(Connection conn) throws Exception {
//    long startGet = System.currentTimeMillis();
    conn.close() ;
//    totalCloseConnect += System.currentTimeMillis() - startGet;
//    System.out.println(" \n\n\n == > total time to Close connection "+totalCloseConnect+"\n\n");
  }
  
  public void commit(Connection conn) throws Exception {
//    long startGet = System.currentTimeMillis();
    conn.setAutoCommit(false);
    conn.commit() ;
//    totalCommit += System.currentTimeMillis() - startGet;
//    System.out.println(" \n\n\n == > total time to Commit "+totalCommit+"\n\n");
  }

  public  DBTableManager getDBTableManager()  { return tableManager_ ; }
  
  public  IDGenerator  getIDGenerator() { return idGenerator_ ; }
  
  public int getDatabaseType()  { return dbType_ ; }
  
  public String getDatabaseName() { return databaseName_ ;}

  public String getDatabaseVersion() { return databaseVersion_ ; }

  public QueryBuilder getQueryBuilder() { return queryManager_; }
}