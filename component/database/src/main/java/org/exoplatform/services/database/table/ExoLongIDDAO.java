/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.exoplatform.services.database.DAO;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.database.Mapper;
import org.exoplatform.services.database.annotation.Table;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
@SuppressWarnings("unused")
public  class ExoLongIDDAO extends DAO<ExoLongID>  {
  
  public ExoLongIDDAO(ExoDatasource datasource) {
    super(datasource, new ExoLongIDMapper()) ;
  }
  
  public List<ExoLongID> loadObjectByName(String name) throws Exception {
    Table table = ExoLongID.class.getAnnotation(Table.class) ;   
    StringBuilder builder = new StringBuilder("SELECT NAME, START FROM ");
    builder.append(table.name()).append(" WHERE name = '").append(name).append('\'');
    return loadByQuery(builder.toString()) ;
  }

  static public class ExoLongIDMapper implements Mapper<ExoLongID> {

    public String[][] toParameters(ExoLongID bean) throws Exception {
      return null;
    }

    public void mapUpdate(ExoLongID bean, PreparedStatement pstm) throws Exception {
      pstm.setString(1, bean.getName()) ;
      pstm.setLong(2, bean.getCurrentBlockId()) ;
    }

    public void mapResultSet(ResultSet res, ExoLongID bean) throws Exception {  
      bean.setName(res.getString(1));
      bean.setCurrentBlockId(res.getLong(2));
    } 
  }
  
  public ExoLongID createInstance() throws Exception { return new ExoLongID(); }

  public ExoLongID load(long id) throws Exception { return null; }
  
  public List<ExoLongID> loadAll() throws Exception { return null; }

  public ExoLongID remove(long id) throws Exception { return null; }

  public void remove(ExoLongID bean) throws Exception {}

  public void save(ExoLongID bean) throws Exception {}

  public void save(List<ExoLongID> beans) throws Exception {}

  public void update(ExoLongID bean) throws Exception {}

  public void update(List<ExoLongID> beans) throws Exception { }
  
}