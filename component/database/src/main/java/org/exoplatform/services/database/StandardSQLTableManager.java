/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.XADataSource;

import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.annotation.TableField;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public class StandardSQLTableManager extends DBTableManager {
  private XADataSource xaDatasource_ ;
  
  public StandardSQLTableManager(ExoDatasource  datasource)  {
    xaDatasource_ = datasource.getDatasource() ;
  }
  
  public <T extends DBObject> void createTable(Class<T>  type,  boolean dropIfExist) throws Exception {
    Table table =  type.getAnnotation(Table.class) ;
    if(table == null) {
      throw new Exception("Cannot find the annotation for class " + type.getClass().getName()) ;
    }
    
    StringBuilder b = new StringBuilder(1000) ;
    b.append("CREATE TABLE ").append(table.name()).append(" (") ;
    b.  append("id BIGINT PRIMARY KEY, ");
    TableField[]  fields = table.field() ; 
    for(int i = 0; i <  fields.length; i++) {
      TableField field = fields[i] ;
      String  fieldType = field.type() ;
      if("string".equals(fieldType)) {
        appendStringField(field, b);
      } else if("int".equals(fieldType)) {
        appendIntegerField(field, b);
      } else if("long".equals(fieldType)) {
        appendLongField(field, b);
      } else if("float".equals(fieldType)) {
        appendFloatField(field, b);
      } else if("double".equals(fieldType)) {
        appendDoubleField(field, b);
      } else if("boolean".equals(fieldType)) {
        appendBooleanField(field, b);
      } else if("date".equals(fieldType)) {
        appendDateField(field, b);
      } else if("binary".equals(fieldType)) {
        appendBinaryField(field, b);
      }
      if(i !=  field.length() - 1) b.append(", ");
    }
    b.append(")") ;
    
    Connection conn = xaDatasource_.getXAConnection().getConnection() ;
    Statement s = conn.createStatement() ;
    s.execute(b.toString()) ;
    s.close() ;
    conn.commit() ;
  }
  
  public <T extends DBObject> void dropTable(Class<T>  type) throws Exception {
    
  }
  
  public <T extends DBObject> boolean hasTable(Class<T> type)  throws Exception {
    return false ;
  }
  
  protected void appendStringField(TableField field, StringBuilder b) {
    String  nullable = "" ;
    if(!field.nullable())  nullable = " NOT NULL " ;
    b. append(field.name()).append(" ").append("VARCHAR(" + field.length() + ")").append(nullable);
  }
  
  protected void appendIntegerField(TableField field, StringBuilder b) {
    b. append(field.name()).append(" INTEGER");
  }
  
  protected void appendLongField(TableField field, StringBuilder b) {
    b. append(field.name()).append(" BIGINT");
  }
  
  protected void appendFloatField(TableField field, StringBuilder b) {
    b. append(field.name()).append(" REAL");
  }
  
  protected void appendDoubleField(TableField field, StringBuilder b) {
    b. append(field.name()).append(" DOUBLE");
  }
  
  protected void appendBooleanField(TableField field, StringBuilder b) {
    b. append(field.name()).append(" BIT");
  }
  
  protected void appendDateField(TableField field, StringBuilder b) {
    b. append(field.name()).append(" DATE");
  }
  
  protected void appendBinaryField(TableField field, StringBuilder b) {
    b. append(field.name()).append(" VARBINARY");
  }
}