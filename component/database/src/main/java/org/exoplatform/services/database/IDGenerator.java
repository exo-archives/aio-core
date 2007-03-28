/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.table.ExoLongID;
import org.exoplatform.services.database.table.ExoLongIDDAO;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public class IDGenerator {
  private HashMap<Class, IDTracker> idTrackers_  ;
  private ExoLongIDDAO dao_ ;
  
  public IDGenerator(ExoDatasource datasource) throws Exception {
    idTrackers_  = new HashMap<Class, IDTracker>();
    dao_  = new ExoLongIDDAO(datasource) ;
   
    //TODO: check  and create ExoLongID  table if it is not existed   
    DBTableManager tableManager = datasource.getDBTableManager();
    if (!tableManager.hasTable(ExoLongID.class)) {
      tableManager.createTable(ExoLongID.class, true);
    }
    
  }
  
  public <T extends DBObject> long generateLongId(T bean) throws Exception {
    return generateLongId(bean.getClass()) ;
  }

  //Lazy loading
  synchronized  public <T extends DBObject> long generateLongId(Class<T> type) throws Exception {
    IDTracker idTracker =  idTrackers_.get(type) ;
    if(idTracker == null) {
      Table table = ExoLongID.class.getAnnotation(Table.class) ;    
      String loadQuery =  
        "SELECT * FROM " + table.name() + " WHERE name = '" +  type.getName()  + "'" ;
      System.out.println("\n=======> loadQuery: " + loadQuery + "\n");
      List<ExoLongID> list = dao_.load(ExoLongID.class, loadQuery) ;
      ExoLongID idObject ;
      if (list.size() == 0) {
        idObject = new ExoLongID(type.getClass().getName(), 100) ;
        //save
        PortalContainer pcontainer = PortalContainer.getInstance() ;
        DatabaseService service = 
          (DatabaseService) pcontainer.getComponentInstance("XAPoolTxSupportDBConnectionService") ;
        Connection conn = service.getConnection() ;        
        ExoLongIDDAO exoLongIDDAO = new ExoLongIDDAO(service.getDatasource());  
        String sql = exoLongIDDAO.getInsertQuery(ExoLongID.class, 1L);
        PreparedStatement ps = conn.prepareStatement(sql) ;       
        ps.setString(1, ExoLongID.class.getName());
        ps.setLong(2, 0L);
        ps.execute();        
//        System.out.println(printQueryResult(service));
//      } else if(list.size() == 1) {
        } else if(list.size() > 0) {
        idObject = list.get(0);
      } else {
        throw new Exception("") ;
      }
      idTracker = new IDTracker(idObject) ;
      idTrackers_.put(type, idTracker) ;
    }
    
    long id = idTracker.currentId_++ ;
    if(id > idTracker.dbobject.getCurrentBlockId() + ExoLongID.BLOCK_SIZE) {
      idTracker.dbobject.setNextBlock() ;
      //save idTracker.dbobject
      
    }
    return id ;    
  }
  
  static private class IDTracker {
    ExoLongID dbobject ;
    long currentId_ ;
    
    IDTracker(ExoLongID dbobject) {
      this.dbobject = dbobject ;
      currentId_ = dbobject.getCurrentBlockId() ;
    }
  }
}
