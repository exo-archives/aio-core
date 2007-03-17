/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.transaction.UserTransaction;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.transaction.TransactionService;
import org.exoplatform.test.BasicTestCase;
/*
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestDatabaseService.java 5332 2006-04-29 18:32:44Z geaz $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestDatabaseService extends BasicTestCase {
  
  public void testXAPoolTxSupportDBConnectionService() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    DatabaseService service = 
      (DatabaseService) pcontainer.getComponentInstance("XAPoolTxSupportDBConnectionService") ;
    assertConfiguration(service) ;
  }
  
  private void assertConfiguration(DatabaseService service)  throws Exception {
    TransactionService txservice = service.getTransactionService() ;
    assertTrue(service != null) ;
    //TransactionManager tm = txservice.getTransactionManager() ;
    UserTransaction utx = txservice.getUserTransaction() ;
    Connection conn = service.getConnection() ;
    Statement s = null ;
    utx.begin() ;
    try {
      System.err.println("\n\nConnection type: " + conn + "\n\n");
      s = conn.createStatement() ;
      s.addBatch("create table test (name varchar, data varchar)" ) ;
      s.addBatch("insert into test values('name1', 'value1')" ) ;
      s.executeBatch() ;
      s.close() ;
      //Call conn.commit()  will cause an exception since the connection is now part of
      //a global transaction.  You should call utx.commit()  here
      conn.commit() ;
      utx.commit() ;
    } catch(Exception ex) {
      System.err.println("ERROR: " + ex.getMessage()) ;
      utx.rollback() ;
    }
    //tm.rollback() ;
    service.closeConnection(conn) ;
    conn =  service.getConnection() ;
    s = conn.createStatement() ;
    ResultSet r = s.executeQuery("select name from test") ;
    if(r.next()) {
      fail("Should not have any data in the test table") ;
    } else {
      System.err.println("Transaction work ok") ;
    }
  }
  
  private void assertDBTableManager(DatabaseService service)  throws Exception {
    
  }
 
  private void assertIDGenerator(DatabaseService service)  throws Exception {
    
  }
  
}