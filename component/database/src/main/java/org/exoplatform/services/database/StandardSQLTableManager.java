/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.sql.Connection;
import java.sql.SQLException;
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
    
    StringBuilder builder = new StringBuilder(1000) ;
    builder.append("CREATE TABLE ").append(table.name()).append(" (") ;
    builder.  append("id BIGINT PRIMARY KEY, ");
    TableField[]  fields = table.field() ; 
    for(int i = 0; i <  fields.length; i++) {
      TableField field = fields[i] ;
      String  fieldType = field.type() ;
      if("string".equals(fieldType)) {
        appendStringField(field, builder);
      } else if("int".equals(fieldType)) {
        appendIntegerField(field, builder);
      } else if("long".equals(fieldType)) {
        appendLongField(field, builder);
      } else if("float".equals(fieldType)) {
        appendFloatField(field, builder);
      } else if("double".equals(fieldType)) {
        appendDoubleField(field, builder);
      } else if("boolean".equals(fieldType)) {
        appendBooleanField(field, builder);
      } else if("date".equals(fieldType)) {
        appendDateField(field, builder);
      } else if("binary".equals(fieldType)) {
        appendBinaryField(field, builder);
      }
      if(i !=  fields.length - 1) builder.append(", ");
    }
    builder.append(")") ;
    // print  out  the  sql string 
    Connection connection = xaDatasource_.getXAConnection().getConnection() ;
    Statement statement = connection.createStatement();
    
    if(dropIfExist) statement.execute("DROP TABLE IF EXISTS " + table.name());
    System.out.println("QUERY: \n  " + builder + "\n");
    statement.execute(builder.toString()) ;
    
    statement.close() ;
    connection.commit() ;
    connection.close() ;
  }
  
  public <T extends DBObject> void dropTable(Class<T>  type) throws Exception {
    Table table = type.getAnnotation(Table.class);
    if (table == null) {
      throw new Exception("Can not find the annotation for class " + type.getClass().getName());
    }
    Connection conn = xaDatasource_.getXAConnection().getConnection();
    Statement s = conn.createStatement();
    s.execute("DROP TABLE " + table.name());
    s.close();
    conn.commit();
    conn.close();    
  }
  
  public <T extends DBObject> boolean hasTable(Class<T> type)  throws Exception {
    Table table = type.getAnnotation(Table.class);
    if (table == null) {
      throw new Exception("Can not find the annotation for class " + type.getClass().getName());
    }
    Connection connection = xaDatasource_.getXAConnection().getConnection();
    Statement statement = connection.createStatement();
    try {
      if(statement.execute("SELECT 1 FROM " + table.name()) == true) return true;      
    } catch (SQLException ex) {
      return false;      
    } finally {
      statement.close();
      connection.close();
    }
    return false;
  }
  
  protected void appendStringField(TableField field, StringBuilder builder) throws Exception {   
    if(field.length() < 1) {
      throw new Exception("You forget to specify  the length for field " + field.name() + " , type " + field.type()) ;
    } 
    builder.append(field.name()).append(" ").append("VARCHAR(" + field.length() + ")");
    if(!field.nullable())  builder.append(" NOT NULL ") ;
  }
  
  protected void appendIntegerField(TableField field, StringBuilder builder) {
    builder.append(field.name()).append(" INTEGER");
  }
  
  protected void appendLongField(TableField field, StringBuilder builder) {
    builder. append(field.name()).append(" BIGINT");
  }
  
  protected void appendFloatField(TableField field, StringBuilder builder) {
    builder. append(field.name()).append(" REAL");
  }
  
  protected void appendDoubleField(TableField field, StringBuilder builder) {
    builder. append(field.name()).append(" DOUBLE");
  }
  
  protected void appendBooleanField(TableField field, StringBuilder builder) {
    builder. append(field.name()).append(" BIT");
  }
  
  protected void appendDateField(TableField field, StringBuilder builder) {
    builder. append(field.name()).append(" DATE");
  }
  
  protected void appendBinaryField(TableField field, StringBuilder builder) {
    builder. append(field.name()).append(" VARBINARY");
  }
  
}