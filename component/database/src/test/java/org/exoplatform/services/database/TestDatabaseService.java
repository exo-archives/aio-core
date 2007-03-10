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

  public void setUp() throws Exception {
  }

  public void testXAPoolStandardDBConnectionService() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    DatabaseService service_ = (DatabaseService) pcontainer.getComponentInstance("XAPoolStandardDBConnectionService") ;
    
    assertTrue(service_ != null) ;
    Connection conn = service_.getConnection() ;
    System.err.println("\n\nConnection type: " + conn + "\n\n");
    Statement s = conn.createStatement() ;
    s.addBatch("create table test (name varchar, data varchar)" ) ;
    s.addBatch("insert into test values('name1', 'value1')" ) ;
    s.executeBatch() ;
    s.close() ;
    conn.commit() ;
    service_.closeConnection(conn) ;
    
    conn =  service_.getConnection() ;
    s = conn.createStatement() ;
    
    ResultSet r = s.executeQuery("select name from test") ;
    if(r.next()) assertEquals("name1", r.getString("name")) ;
  }
  
  public void testXAPoolTxSupportDBConnectionService() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    DatabaseService service_ = (DatabaseService) pcontainer.getComponentInstance("XAPoolTxSupportDBConnectionService") ;
  
    TransactionService txservice = service_.getTransactionService() ;
    assertTrue(service_ != null) ;
    //TransactionManager tm = txservice.getTransactionManager() ;
    UserTransaction utx = txservice.getUserTransaction() ;
    Connection conn = service_.getConnection() ;
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
    service_.closeConnection(conn) ;
    conn =  service_.getConnection() ;
    s = conn.createStatement() ;
    ResultSet r = s.executeQuery("select name from test") ;
    if(r.next()) {
      fail("Should not have any data in the test table") ;
    } else {
      System.err.println("Transaction work ok") ;
    }
  }
}