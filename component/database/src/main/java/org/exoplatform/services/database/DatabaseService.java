/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.Connection;

import javax.sql.DataSource;

import org.exoplatform.services.transaction.TransactionService;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public interface DatabaseService {
  
  public DataSource getDatasource() throws Exception ;
  public Connection getConnection() throws Exception ;
  public void closeConnection(Connection conn) throws Exception ;
  public boolean isTransactionSupport() ;
  public TransactionService getTransactionService() throws Exception ;

}