/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Mar 29, 2007  
 */
public interface DBObjectMapper<T extends DBObject> {
  
  public  void mapUpdate(T bean, PreparedStatement statement) throws Exception ;
  
  public String [][] toParameters(T bean) throws Exception ;
  
  public void mapResultSet(ResultSet res, T bean) throws Exception ;
  
}
