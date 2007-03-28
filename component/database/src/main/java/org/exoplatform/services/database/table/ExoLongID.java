/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.table;

import org.exoplatform.services.database.DBObject;
import org.exoplatform.services.database.annotation.*;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * Mar 16, 2007  
 */
@Table(
  name = "ExoLongId" ,
  field = {
    @TableField(name = "name", type = "string", length = 500, unique = true, nullable = false),
    @TableField(name = "start", type = "long")
  }
)
public class ExoLongID extends DBObject {
  final static public long BLOCK_SIZE = 100 ;

  private String name ;
  //rename to currentBlockId
  private long   start ;
  
  public ExoLongID() { }

  public ExoLongID(String name,  long start) {
    this.name = name ;
    this.start =  start ;
  }
  
  public String getName()  { return name ; }
  public void setName(String name) { this.name = name ; }
  
  public long getStart()  { return start ; }
  public void setStart(long start) { this.start = start ; }
  public void setNextBlock() { this.start = this.start + BLOCK_SIZE ; }
}