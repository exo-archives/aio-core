/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
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
  
  private XADataSource xaDatasource_  ;
  private DBTableManager tableManager_ ;
  private IDGenerator  idGenerator_ ;
  private String databaseName_ ;
  private String databaseVersion_ ;
  private int dbType_ = STANDARD_DB_TYPE;
  
  public ExoDatasource(XADataSource ds) throws Exception {
    xaDatasource_ = ds ;
    DatabaseMetaData metaData = ds.getXAConnection().getConnection().getMetaData() ;
    databaseName_ = metaData.getDatabaseProductName() ;
    databaseVersion_ = metaData.getDatabaseProductVersion() ;
    
    String dbname = databaseName_.toLowerCase() ;
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
    } else {
      dbType_ = STANDARD_DB_TYPE ;
    }
    
    tableManager_ = DBTableManager.createDBTableManager(this) ;
    //TODO: Create IDGenrator and test
    idGenerator_ = new IDGenerator(this) ;
  }
  
  public XADataSource getDatasource() { return xaDatasource_ ;}

  public XAConnection getXAConnection() throws Exception {
    return  xaDatasource_.getXAConnection() ;
  }

  public Connection getConnection() throws Exception {
    return xaDatasource_.getXAConnection().getConnection() ;
  }
  
  public void closeConnection(Connection conn) throws Exception {
    conn.close() ;
  }
  
  public void commit(Connection conn) throws Exception {
    conn.commit() ;
  }

  public  DBTableManager getDBTableManager()  { return tableManager_ ; }
  
  public  IDGenerator  getIDGenerator() { return null ; }
  
  public int getDatabaseType()  { return dbType_ ; }
  
  public String getDatabaseName() { return databaseName_ ;}

  public String getDatabaseVersion() { return databaseVersion_ ; }
}