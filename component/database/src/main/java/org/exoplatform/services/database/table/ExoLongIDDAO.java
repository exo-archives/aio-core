/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.database.StandardSQLDAO;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public  class ExoLongIDDAO extends StandardSQLDAO<ExoLongID>  {
  
  public ExoLongIDDAO(ExoDatasource ds) {
    super(ds) ;
  }
  
  public ExoLongID createInstance(Class<ExoLongID> type) throws Exception {
    return new ExoLongID() ;
  }
  
  protected void mapUpdate(ExoLongID bean, PreparedStatement  pstm) throws Exception {
    pstm.setString(1, bean.getName()) ;
    pstm.setLong(2, bean.getStart()) ;
  }
  
  protected  void mapResultSet(ResultSet res, ExoLongID bean) throws Exception {
    bean.setId(res.getLong(1)) ;
    bean.setName(res.getString(2)) ;
    bean.setStart(res.getLong(3)) ;
  }
}