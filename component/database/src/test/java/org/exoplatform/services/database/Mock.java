/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;

import org.exoplatform.services.database.annotation.*;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * Mar 16, 2007  
 */
@Table(
    name = "MockTable" ,
    field = {
        @TableField(name = "name", type = "string", length = 500, unique = true, nullable = false),
        @TableField(name = "status", type = "int", defaultValue= "0"),
        @TableField(name = "start", type = "date"),
        @TableField(name = "pass", type = "boolean", defaultValue="false")
    }
)
public class Mock extends DBObject {

  private String name ;
  private int status;
  private Calendar  start = Calendar.getInstance();
  private boolean pass = false;

  public Mock() { }

  public Mock(String name, int status) {
    this.name = name ;
    this.status = status;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public boolean isPass() { return pass;}
  public void setPass(boolean pass) { this.pass = pass; }

  public Calendar getStart() { return start; }
  public void setStart(Calendar start) { this.start = start; }

  public int getStatus() { return status; }
  public void setStatus(int status) { this.status = status; }


  static public class MockMapper implements Mapper<Mock> {

    public String mapSQL(Mock bean, String template) throws Exception {
      return null;
    }

    public void mapResultSet(ResultSet res, Mock bean) throws Exception {
      bean.setName(res.getString("name"));
      bean.setPass(res.getBoolean("pass"));
      Calendar calendar = Calendar.getInstance();
      res.getDate("start", calendar);
      bean.setStart(calendar);
      bean.setStatus(res.getInt("status"));
    }

    public void mapUpdate(Mock bean, PreparedStatement statement) throws Exception {
      statement.setString(1, bean.getName());
      statement.setInt(2, bean.getStatus());
      statement.setDate(3, new java.sql.Date(bean.getStart().getTimeInMillis()));
      statement.setBoolean(4, bean.isPass());
    }
  }

  /**
   * Query(
   *   name = "GetUserByUserName"
   *   oracle = "select * from user where username= N'?' and fname = $fname" +
   *            ".............................................................." +
   *            ".............................................................." ,
   *   standard="......................................................." ,
   * )
   * 
   */
  public  void getUserByName(String s) {
    HashMap<String, String>  values = new HashMap<String, String>();
    values.put("username" , s) ;
    values.put("fname" , s) ;
    //Object[]  params = {
    //  {"username", "value"},
    //  {"username", "value"},
    //  {"username", "value"}
    //}
    //String query =  createQuery("GetUserByUserName", params) ;
  }


}