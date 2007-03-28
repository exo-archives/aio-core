/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import org.exoplatform.services.database.DBObject;
import org.exoplatform.services.database.annotation.*;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * Mar 16, 2007  
 */
@Table(
  name = "TestTable" ,
  field = {
    @TableField(name = "name", type = "string", length = 500, unique = true, nullable = false),
    @TableField(name = "status", type ="string", length = 500, nullable = false),
    @TableField(name = "start", type = "long"),
    @TableField(name = "intField", type = "int"),
    @TableField(name = "floatField", type = "float"),
    @TableField(name = "doubleField", type = "double"),
    @TableField(name = "booleanField", type = "boolean"),
    @TableField(name = "dateField", type = "date"),
    @TableField(name = "binaryField", type = "binary", nullable = false)
  }
)
public class TestTable extends DBObject {
  private String nameField ;
  private String statusStringField;
  private long   startField ;  

  public TestTable() { }

  public TestTable(String name, long start, String status) {
    this.nameField = name ;
    this.startField =  start ;
    this.statusStringField = status;
  }
  
  public String getStatus() { return statusStringField; }
  public void setStatus(String str) { this.statusStringField = str; }
  
  public String getNameField()  { return nameField ; }
  public void setNameField(String name) { this.nameField = name ; }
  
  public long getStartField()  { return startField ; }
  public void setStartField(long start) { this.startField = start ; }
}