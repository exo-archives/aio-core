/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.Connection;

import org.exoplatform.services.transaction.TransactionService;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 * 
 * This service should provide a single interface to access the diffent datasource.
 */
public interface DatabaseService {
  
  /**
   * This method should return the default datasource of the application
   * @return
   * @throws Exception
   */
  public ExoDatasource getDatasource() throws Exception ;
  /**
   * This method should look up the datasouce by the datasource name and return. If the datasource is not
   * found then the method should return null
   * @param dsname
   * @return
   * @throws Exception
   */
  public ExoDatasource getDatasource(String dsname) throws Exception ;
  
  //TODO: This method should be removed and used the getDataSource method
  public Connection getConnection() throws Exception ;
  //TODO: This method should be removed and used the getDataSource method
  public Connection getConnection(String  dsName) throws Exception ;
  //TODO: This method should be removed and used the getDataSource method
  public void closeConnection(Connection conn) throws Exception ;
  
  /**
   * This method should return the transaction service
   * @return
   * @throws Exception
   */
  public TransactionService getTransactionService() throws Exception ;

}