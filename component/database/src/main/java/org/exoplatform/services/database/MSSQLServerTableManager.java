/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import org.exoplatform.services.database.annotation.TableField;


/**
 * Created by The eXo Platform SARL
 * Author : Le Bien Thuy
 *          lebienthuy@exoplatform.com
 * Apr 4, 2006
 */
public class MSSQLServerTableManager extends StandardSQLTableManager {

  public MSSQLServerTableManager(ExoDatasource datasource) {
    super(datasource);
  }
  
  protected void appendDateField(TableField field, StringBuilder builder) {
    builder. append(field.name()).append(" DATETIME");
  }  
}