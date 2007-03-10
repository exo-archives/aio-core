/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.XAConnection;

import org.enhydra.jdbc.standard.StandardXADataSource;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.database.DatabaseService;
import org.exoplatform.services.transaction.TransactionService;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public class XAPoolTxSupportDatabaseService implements DatabaseService {
  private StandardXADataSource datasource_ ;
  private TransactionService txService_ ;
  
  public XAPoolTxSupportDatabaseService(InitParams params, TransactionService txService) throws Exception {
    Map<String,String> props = params.getPropertiesParam("connection.config").getProperties();
    datasource_ = new StandardXADataSource();
    datasource_.setDriverName(props.get("connection.driver")) ;
    datasource_.setUrl(props.get("connection.url")) ;
    datasource_.setUser(props.get("connection.login")) ;
    datasource_.setPassword(props.get("connection.password")) ;
    datasource_.setMinCon(Integer.parseInt(props.get("connection.min-size"))) ;
    datasource_.setMaxCon(Integer.parseInt(props.get("connection.max-size"))) ;
    txService_ =  txService ;
    datasource_.setTransactionManager(txService_.getTransactionManager()) ;
  }
  
  
  public DataSource getDatasource() throws Exception { return datasource_ ; }
  
  public Connection getConnection() throws Exception {
    XAConnection xaconn = datasource_.getXAConnection() ;
    Connection conn = xaconn.getConnection();
    return conn ;
  }
  
  public void closeConnection(Connection conn) throws Exception {
    conn.close() ;
  }
  
  public boolean isTransactionSupport() {  return true; }
  
  public TransactionService getTransactionService() throws Exception { return txService_ ; }
}
