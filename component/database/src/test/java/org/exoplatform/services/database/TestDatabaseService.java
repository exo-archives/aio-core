/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.transaction.UserTransaction;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.table.ExoLongID;
import org.exoplatform.services.database.table.ExoLongIDDAO;
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
  
  public void testDatabaseService() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    DatabaseService service = 
      (DatabaseService) pcontainer.getComponentInstance("XAPoolTxSupportDBConnectionService") ;
    //assertConfiguration(service) ;
//    assertDBTableManager(service);
    assertIDGenerator(service);
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
    ResultSet rs = s.executeQuery("select name from test") ;   
    if(rs.next()) {     
      fail("Should not have any data in the test table") ;
    } else {
      System.err.println("Transaction work ok") ;
    }
  }
  
  private void assertDBTableManager(DatabaseService service)  throws Exception {
    System.err.println("\n\n===>ASERT DBTableManager\n") ;
    ExoDatasource  datasource = service.getDatasource() ;
    DBTableManager dbManager = datasource.getDBTableManager() ;
    assertEquals(dbManager.hasTable(TestTable.class), false);
    dbManager.createTable(TestTable.class, true) ;
    
    //Test meta data here
//    ResultSetMetaData metaData = datasource.g
    
    assertEquals(dbManager.hasTable(TestTable.class), true);
    dbManager.dropTable(TestTable.class);
    
    assertEquals(dbManager.hasTable(TestTable.class), false);
    
    //Test metadata here
  /*  Connection conn = service.getConnection() ;
    Statement s = conn.createStatement() ;
    Table table =  TestTable.class.getAnnotation(Table.class) ;
    ResultSet rs = s.executeQuery("SELECT * FROM " + table.name());
    ResultSetMetaData metaData = rs.getMetaData();
    for (int i = 1; i <= metaData.getColumnCount(); i++) {
      System.out.println("Information about column " + metaData.getColumnName(i) + ":\n" +
         "type: " + metaData.getColumnTypeName(i) + ", is nullable: " + metaData.isNullable(i) +
         "\n");      
    }*/
    System.err.println("\n\n<===ASSERT DBTableManager\n") ;
  }
 
  private void assertExoLongIDDAO(DatabaseService service)  throws Exception {
   /* Connection connection = service.getConnection();
    ExoLongIDDAO exoLongIDDAO = new ExoLongIDDAO(service.getDatasource());
    ExoLongID 
    exoLongIDDAO.save();*/
  }
  
  private void assertIDGenerator(DatabaseService service)  throws Exception {
    ExoDatasource  datasource = service.getDatasource() ;
//    DBTableManager dbManager = datasource.getDBTableManager() ;    
    IDGenerator idGenerator = new IDGenerator(datasource);
    
//    idGenerator.restartTracker();
    for(int i = 0; i < 10; i++) {
      System.out.println("\n=================> IDGenerator " + i + " : " + idGenerator.generateLongId(ExoLongID.class));
//      if (i == 5) idGenerator.restartTracker();        
    }
    
    idGenerator.restartTracker();
    System.out.println("\n=================> IDGenerator " + 10 + " : " + idGenerator.generateLongId(ExoLongID.class));
  }
  
}