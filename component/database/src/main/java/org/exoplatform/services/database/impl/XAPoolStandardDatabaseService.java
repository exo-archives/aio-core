/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.enhydra.jdbc.pool.StandardPoolDataSource;
import org.enhydra.jdbc.standard.StandardConnectionPoolDataSource;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.database.DatabaseService;
import org.exoplatform.services.transaction.TransactionService;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public class XAPoolStandardDatabaseService implements DatabaseService {
  private StandardPoolDataSource datasource_ ;
  private boolean autocommit_ = false ;
  
  public XAPoolStandardDatabaseService(InitParams params) throws Exception {
    Map<String,String> props = params.getPropertiesParam("connection.config").getProperties();
    
    StandardConnectionPoolDataSource connect = new StandardConnectionPoolDataSource();
    String login =  props.get("connection.login") ;
    String password = props.get("connection.password") ;
    connect.setUrl(props.get("connection.url"));
    connect.setDriverName(props.get("connection.driver"));
    connect.setUser(login);
    connect.setPassword(password);
    
    // second, we create the pool of connection with the previous object
    datasource_ = new StandardPoolDataSource(connect);
    datasource_.setMinSize(Integer.parseInt(props.get("connection.min-size")));
    datasource_.setMaxSize(Integer.parseInt(props.get("connection.max-size")));
    
  }
  

  public DataSource getDatasource() throws Exception {
    return datasource_  ;
  }
  
  public Connection getConnection() throws Exception {
    Connection conn = datasource_.getConnection()  ;
    conn.setAutoCommit(autocommit_) ;
    return conn ;
  }
  
  public void closeConnection(Connection conn) throws Exception {
    conn.close() ;
  }
  
  public boolean isTransactionSupport() {
    return false;
  }
  
  public TransactionService getTransactionService() throws Exception {
    return null ;
  }
}