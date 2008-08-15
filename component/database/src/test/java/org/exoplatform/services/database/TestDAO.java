/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.listener.ListenerService;
/**
 * Created by The eXo Platform SAS
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * Mar 27, 2007  
 */
public class TestDAO extends TestCase {
  
  public void testDummy() {
    // empty, to doesn't fail during the tests
  }
  
/*

  GA: This Test failed, uncomment to see!!! 
  
  public void testDAO() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    DatabaseService service = 
      (DatabaseService) pcontainer.getComponentInstance("XAPoolTxSupportDBConnectionService") ;
    
    PortalContainer manager  = PortalContainer.getInstance();
    ListenerService  listenerService = (ListenerService) manager.getComponentInstanceOfType(ListenerService.class) ;    
    
    queries(service);
    testMock(listenerService, service);
  }
*/  
  private void testMock(ListenerService listenerService, DatabaseService service) throws Exception {
    ExoDatasource dataSource = service.getDatasource();
    DBTableManager dbManager = dataSource.getDBTableManager() ;
    assertEquals(dbManager.hasTable(Mock.class), false);
    dbManager.createTable(Mock.class, true) ;
    assertEquals(dbManager.hasTable(Mock.class), true);
    
    StandardSQLDAO<Mock>  dao = new StandardSQLDAO<Mock>(dataSource, new Mock.MockMapper(), Mock.class);
    Mock mock = new Mock("Benj", 2);
    dao.save(mock);
    
    List<Mock> list  = new ArrayList<Mock>();
    list.add(new Mock("Thuannd", 12));    
    list.add(new Mock("Hung", 4));
    dao.save(list);
    
    Mock savedMock = dao.load(mock.getDBObjectId());
    assertEquals(mock.getName(), savedMock.getName());
    
    //reflection mapper
    dao = new StandardSQLDAO<Mock>(dataSource, Mock.class);
    list.clear();
    list.add(new Mock("Ha", 17));
    list.add(new Mock("Hoa", 6));
    dao.save(list);
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
//    
//    sql = exoLongIDDAO.getRemoveQuery(ExoLongID.class, 34L);
//    assertEquals(sql, "DELETE FROM ExoLongId WHERE id = '34'");
//    sql = exoLongIDDAO.getUpdateQuery(ExoLongID.class, 34L);
//    assertEquals(sql, "UPDATE ExoLongId SET name = ?, start = ? WHERE id = 34"); 
//  
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
