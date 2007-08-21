/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SAS         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.table;

import org.exoplatform.services.database.DBObject;
import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.annotation.TableField;

/**
 * Created by The eXo Platform SAS
 * Mar 16, 2007  
 */
@Table(
    name = "EXO_LONG_ID" ,
    field = {
        @TableField(name = "EXO_NAME", type = "string", length = 500, unique = true, nullable = false),
        @TableField(name = "EXO_START", type = "long")
    }
)
public class ExoLongID extends DBObject {

  final static public long BLOCK_SIZE = 3 ;

  private String name ;
  private long  currentBlockId ;

  public ExoLongID() { }

  public ExoLongID(String name,  long start) {
    this.name = name ;
    this.currentBlockId =  start ;
  }

  public String getName()  { return name ; }
  public void setName(String name) { this.name = name ; }

  public long getCurrentBlockId()  { return currentBlockId ; }
  public void setCurrentBlockId(long start) { this.currentBlockId = start ; }

  public void setNextBlock() { 
    this.currentBlockId = this.currentBlockId + BLOCK_SIZE ;
  }
 
}