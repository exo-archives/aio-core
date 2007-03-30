/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import junit.framework.TestCase;

import org.exoplatform.container.PortalContainer;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * Mar 27, 2007  
 */
public class TestDAO extends TestCase {
  
  public void testDAO() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    DatabaseService service = 
      (DatabaseService) pcontainer.getComponentInstance("XAPoolTxSupportDBConnectionService") ;
    
    queries(service);
    testMock(service);
  }
  
  private void testMock(DatabaseService service) throws Exception {
    ExoDatasource dataSource = service.getDatasource();
    DBTableManager dbManager = dataSource.getDBTableManager() ;
    assertEquals(dbManager.hasTable(Mock.class), false);
    dbManager.createTable(Mock.class, true) ;
    assertEquals(dbManager.hasTable(Mock.class), true);
    
    StandardSQLDAO<Mock>  dao = new StandardSQLDAO<Mock>(dataSource, new Mock.MockMapper());
    Mock mock = new Mock("benj", 2);
    dao.save(mock);
    
    Mock savedMock = dao.load(Mock.class, mock.getId());
    assertEquals(mock.getName(), savedMock.getName());
  }
  
  private String printQueryResult(DatabaseService service) throws Exception {
    Connection conn = service.getConnection() ;
    Statement statement = conn.createStatement();
    String output = "\nQuery result: \n";    
    ResultSet rs = statement.executeQuery("SELECT * FROM ExoLongId");    
    while (rs.next()) {
      output += rs.getString(1) + "\n" + rs.getString(2) + "\n" + rs.getString(3) + "====\n";
    }
    
    return output;
  }
  
  private void queries(DatabaseService service) throws Exception {       
//    ExoLongIDDAO exoLongIDDAO = new ExoLongIDDAO(service.getDatasource());   
//    assertEquals(sql, "INSERT INTO ExoLongId(id, name, start) VALUES(34, ?, ?)");
//    System.out.println("\n=========> sql: " + sql +"\n");
    
//    sql = exoLongIDDAO.getRemoveQuery(ExoLongID.class, 34L);
//    assertEquals(sql, "DELETE FROM ExoLongId WHERE id = '34'");
//    sql = exoLongIDDAO.getUpdateQuery(ExoLongID.class, 34L);
//    assertEquals(sql, "UPDATE ExoLongId SET name = ?, start = ? WHERE id = 34"); 
  
//    Table table =  TestTable.class.getAnnotation(Table.class) ;
//    String sql = exoLongIDDAO.getInsertQuery(ExoLongID.class, 34L);
//    
//    Connection conn = service.getConnection() ;
//    PreparedStatement ps = conn.prepareStatement(sql) ; 
//   
//    ps.setString(1, "This is name");
//    ps.setLong(2, 55L);
//    ps.executeUpdate();
//    System.out.println(printQueryResult(service));
//    
//    try {
//      ps.setString(1, "This is name");
//      ps.setLong(2, 55L);
//      ps.executeUpdate();
//    } catch (SQLException ex) {   
//      System.err.println("\n==================> Error in insert: " + ex.getMessage() + "\n\n");
//    }
//    
//    String updateSQL = exoLongIDDAO.getUpdateQuery(ExoLongID.class, 34L);
//    ps = conn.prepareStatement(updateSQL) ; 
//    ps.setString(1, "This is updated name");
//    ps.setLong(2, 56L);
//    ps.executeUpdate();
//    System.out.println("After update: " + printQueryResult(service));
//    
//    sql = exoLongIDDAO.getRemoveQuery(ExoLongID.class, 34L);   
//    ps = conn.prepareStatement(sql) ;    
////    ps.setLong(1, 34L);
//    ps.executeUpdate();
//    System.out.println("After delete: " + printQueryResult(service));
  }  
}
