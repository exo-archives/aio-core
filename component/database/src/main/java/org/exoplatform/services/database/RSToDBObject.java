/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.ResultSet;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Mar 29, 2007  
 */
public class RSToDBObject {

  void toObject(ResultSet resultSet, DBObject bean) throws Exception {
//    ResultSetMetaData rsmd = resultSet.getMetaData();
//    int numberOfColumns = rsmd.getColumnCount();
//    for(int i=1; i<=numberOfColumns; i++){
//      String name  = rsmd.getColumnName(i);
//      try{
//        RSField field = RSField.valueOf(name.toUpperCase());
//        bean.setField(field, getValue(rsmd.getColumnType(i), resultSet, name));
//      }catch (Exception e) {        
//        LogData.setMessage(e.toString()+" : "+bean +" : "+name);  
//        continue;
//      }  
//    }
  }
}
