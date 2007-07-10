/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.enhydra.jdbc.standard.StandardXADataSource;
import org.enhydra.jdbc.pool.StandardXAPoolDataSource ;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.PropertiesParam;
import org.exoplatform.services.database.DatabaseService;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.transaction.TransactionService;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public class XAPoolTxSupportDatabaseService implements DatabaseService {
  private HashMap<String, ExoDatasource> datasources_ ;
  private ExoDatasource defaultDS_ ;
  private TransactionService txService_ ;
  
  public XAPoolTxSupportDatabaseService(InitParams params, 
                                        TransactionService txService) throws Exception {
    datasources_ = new HashMap<String, ExoDatasource>(5) ;
    txService_ =  txService ;
    Iterator i = params.getPropertiesParamIterator() ;
    while(i.hasNext()) {
      PropertiesParam param = (PropertiesParam)i.next() ;
      String name = param.getName() ;
      ExoDatasource ds = new ExoDatasource(createDatasource(param.getProperties())) ;
      datasources_.put(name, ds) ;
      if(defaultDS_ == null)  defaultDS_ = ds ;
    }
  }
  
  
  public ExoDatasource getDatasource() throws Exception {  return defaultDS_ ; }
  
  public ExoDatasource getDatasource(String dsName) throws Exception {  
    return datasources_.get(dsName) ; 
  }
  
  
  public Connection getConnection() throws Exception {
    return   defaultDS_.getConnection() ;
  }
  
  public Connection getConnection(String dsName) throws Exception {
    ExoDatasource ds = datasources_.get(dsName) ;
    return  ds.getConnection();
  }
  
  public void closeConnection(Connection conn) throws Exception {
    conn.close() ;
  }
  
  public TransactionService getTransactionService() throws Exception { return txService_ ; }
  
  
  private DataSource createDatasource(Map<String,String> props) throws Exception {
    StandardXADataSource ds = new StandardXADataSource();
    ds.setDriverName(props.get("connection.driver")) ;
    ds.setUrl(props.get("connection.url")) ;
    ds.setUser(props.get("connection.login")) ;
    ds.setPassword(props.get("connection.password")) ;
    //ds.setMinCon(Integer.parseInt(props.get("connection.min-size"))) ;
    //ds.setMaxCon(Integer.parseInt(props.get("connection.max-size"))) ;
    ds.setTransactionManager(txService_.getTransactionManager()) ;
    
    StandardXAPoolDataSource pool = new StandardXAPoolDataSource(3);
    pool.setMinSize(Integer.parseInt(props.get("connection.min-size"))) ;
    pool.setMaxSize(Integer.parseInt(props.get("connection.max-size"))) ;
    //pool.setUser(props.get("connection.login")) ;
    //pool.setPassword(props.get("connection.password")) ;
    pool.setDataSource(ds) ;
    return pool ;
  }
}
