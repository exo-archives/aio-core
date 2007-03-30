/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.util.HashMap;
import java.util.List;

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
  private ExoLongIDDAO dao_;
  
  public IDGenerator(ExoDatasource datasource) throws Exception {
    idTrackers_  = new HashMap<Class, IDTracker>();
   
    //TODO: check  and create ExoLongID  table if it is not existed   
    DBTableManager tableManager = datasource.getDBTableManager();
    if (!tableManager.hasTable(ExoLongID.class)) {
      tableManager.createTable(ExoLongID.class, true);
    }
    dao_ = new ExoLongIDDAO(datasource); 
  }
  
  public <T extends DBObject> long generateLongId(T bean) throws Exception {
    return generateLongId(bean.getClass()) ;
  }

  //Lazy loading
  synchronized  public <T extends DBObject> long generateLongId(Class<T> type) throws Exception {
    IDTracker idTracker =  idTrackers_.get(type) ;  
    
    if(idTracker == null) {
      List<ExoLongID> list = dao_.loadObjectByName(type.getName()) ;
      System.out.println("\n=======> list.size() " + list.size());
      ExoLongID idObject ;  
      long currentId = 0 ;
      if (list.size() == 0) {       
        idObject = new ExoLongID(type.getName(), ExoLongID.BLOCK_SIZE) ;
        dao_.save(idObject);       
      } else if(list.size() == 1) {
        idObject = list.get(0);
        currentId = idObject.getCurrentBlockId() ;
        idTracker.blockTracker.setNextBlock() ;   
        dao_.update(idTracker.blockTracker);
      } else {
        throw new Exception("") ;
      }
      idTracker = new IDTracker(idObject, currentId) ;
      idTrackers_.put(type, idTracker) ;
      
      System.out.println("+++>>" + dao_.load(ExoLongID.class, 1))  ;
    }
    
    long generatedId = ++idTracker.currentId_ ;
    if(generatedId > idTracker.blockTracker.getCurrentBlockId() + ExoLongID.BLOCK_SIZE) {
      idTracker.blockTracker.setNextBlock() ;   
      dao_.update(idTracker.blockTracker);
    }
    return generatedId ;
  }
  
  //for testing
  public void restartTracker() { idTrackers_.clear() ; }
  
  static private class IDTracker {
    ExoLongID blockTracker ;
    long currentId_ ;
    
    IDTracker(ExoLongID dbobject, long currentId) {
      this.blockTracker = dbobject ;
      currentId_ = currentId ;     
    }
  }
}
