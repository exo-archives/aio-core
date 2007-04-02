/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.lang.reflect.Method;
import java.util.List;

import org.exoplatform.services.database.annotation.Query;
import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.annotation.TableField;
import org.exoplatform.services.database.annotation.Query.SQL;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Mar 30, 2007  
 */
public class QueryBuilder {
  
  private int databaseType = ExoDatasource.STANDARD_DB_TYPE;
  
  public QueryBuilder() {}
  
  public QueryBuilder(int dbType) { databaseType = dbType; }
  
  public <T extends DBObject> String createSelectQuery(Class<T> type, long id) throws Exception {
    Table table = type.getAnnotation(Table.class) ;
    TableField[]  fields =  table.field() ;
    
    StringBuilder query = new StringBuilder("SELECT ");
    for(int i = 0; i <  fields.length; i++) {
      TableField  field =  fields[i] ;
      query.append(field.name()) ;
      if (i !=  fields.length - 1) query.append(", ") ;      
    }
    query.append(" FROM ").append(table.name());
    if(id > -1) query.append(" WHERE id = ").append(id);
    return query.toString() ;
  }
  
  public <T extends DBObject> String createUpdateQuery(Class<T> type) throws Exception {
    Table table = type.getAnnotation(Table.class) ;
    TableField[]  fields =  table.field() ;
    
    StringBuilder query = new StringBuilder("UPDATE ").append(table.name()).append(" SET ");
    for(int i = 0; i <  fields.length; i++) {
      TableField  field =  fields[i] ;
      query.append(field.name()).append(" = '$").append(field.name()).append('\'') ;
      if (i !=  fields.length - 1) query.append(", ") ; else query.append(" WHERE id = $id");   
    }
    return query.toString() ;
  }
  
  public <T extends DBObject> String createInsertQuery(Class<T> type) throws Exception {
    Table table = type.getAnnotation(Table.class) ;
    TableField[]  fields =  table.field() ;
    
    StringBuilder query = new StringBuilder("INSERT INTO ").append(table.name()).append("(id, ");
    for(int i = 0; i <  fields.length; i++) {
      TableField  field =  fields[i] ;
      query.append(field.name()) ;
      if (i !=  fields.length - 1)  query.append(", ") ; else query.append(") VALUES($id, ");
    }
    
    for(int i = 0; i <  fields.length; i++) {
      query.append("'$").append(fields[i].name()).append('\'') ;
      if (i !=  fields.length - 1)  query.append(", "); else query.append(")");
    }
    return query.toString();
  }
  
  public <T extends DBObject> String createUpdateQuery(Class<T> type, long id) throws Exception {
    Table table = type.getAnnotation(Table.class) ;
    TableField[]  fields =  table.field() ;
    
    StringBuilder query = new StringBuilder("UPDATE ").append(table.name()).append(" SET ");
    for(int i = 0; i <  fields.length; i++) {
      TableField  field =  fields[i] ;
      query.append(field.name()).append(" = ?") ;
      if (i !=  fields.length - 1) query.append(", "); else query.append(" WHERE id = ").append(id); 
    }
    
    return query.toString() ;
  }
  
  public <T extends DBObject> String createInsertQuery(Class<T> clazz, long id) throws Exception {
    Table table = clazz.getAnnotation(Table.class);
    TableField[]  fields =  table.field() ;
    
    StringBuilder query = new StringBuilder("INSERT INTO ").append(table.name()).append("(id, ");
    for(int i = 0; i <  fields.length; i++) {
      TableField  field =  fields[i] ;
      query.append(field.name()) ;
      if(i != fields.length - 1) query.append(", "); 
    }
    query.append(") VALUES(").append(id).append(", ");
    
    for(int i = 0; i <  fields.length; i++) {
//      TableField  field =  fields[i] ;
      query.append("?") ;
//      if(i ==  field.length() - 1)  query.append(", ") ;
      if (i !=  fields.length - 1)  query.append(", ") ; else query.append(")");
    }
    return query.toString();
  }
  
  public <T extends DBObject> String createRemoveQuery(Class<T> type, long id) throws Exception {
    Table table = type.getAnnotation(Table.class) ;
    StringBuilder builder = new StringBuilder("DELETE FROM ");
    builder.append(table.name()).append(" WHERE id = ").append(id).toString();
    return builder.toString();
  }
  
  public <T extends DAO> String getQuery(Class<T> clazz, String name) throws Exception {
    Query query = clazz.getAnnotation(Query.class);
    String value = null;
    if(query != null && query.name().equals(name)) value = getQuery(query);
    if(value != null) return value;
    List<Method> list = ReflectionUtil.getMethod(clazz, name);
    for(Method method : list) {
      query = method.getAnnotation(Query.class);
      if(query != null && query.name().equals(name)) value = getQuery(query);
      if(value != null) return value;
    }
    return null;
  }
  
  private String getQuery(Query query) {
    SQL [] queries = query.queries();
    for(SQL sql : queries){
      if(sql.DBType() == databaseType) return sql.value();
    }
    return null;
  }
  
  public String mapDataToSql(String template, String[][] parameters) throws Exception {
    StringBuilder builder = new StringBuilder();
    int i = 0;
    int start  = 0;
    while(i < template.length()) {
      if(template.charAt(i) != '$') {
        i++;
        continue;
      }
      
      if(i > 0 && template.charAt(i-1) == '\\') {
        builder.append(template.subSequence(start, i-1)); 
        start = i; 
        i++;
        continue;
      }
      
      if(i == template.length() - 1) break;
      
      int j = i + 1;
      while(j < template.length()) {
        if(Character.isWhitespace(template.charAt(j))) break;
        if(template.charAt(j) == '\'' && template.charAt(j-1) != '\\') break;
        if(template.charAt(j) == ',' && template.charAt(j-1) != '\\') break;
        j++;
      }
      String name = template.substring(i+1, j);
      start = replace(template, builder, parameters, name, start, i); 
      i++;
    }
    if(start > 0 && start < template.length()) {
      builder.append(template.subSequence(start, template.length())); 
    }
    if(builder.length() < 1) return template.toString(); 
    return builder.toString();
  }
  
  private int replace(String template, StringBuilder builder,
                      String [][] parameters, String name, int start, int current) throws Exception {
    for(int k = 0; k < parameters.length; k++){
      if(!parameters[k][0].equals(name)) continue;
      builder.append(template.subSequence(start, current)).append(parameters[k][1]); 
      return current + 1 + name.length();
    }
    return start;
  }
  
  /*  will support 
     if(object instanceof String[]) {
        start = replace(template, builder, (String[])object, name, start, i);
      } else if(object instanceof String [][]) {
        start = replace(template, builder, (String [][])object, name, start, i);
      } else if(object instanceof Map) {
        start = replace(template, builder, Map.class.cast(object), name, start, i);
      } else {
      }
    private int replace(String template, StringBuilder builder,
                      String [] params, String name, int start, int current) throws Exception {
    if(params.length != 2) throw new Exception("Parameter is incorrect!");
    if(!params[0].equals(name)) return start;
    builder.append(template.subSequence(start, current)).append(params[1]); 
    return current + 1 + name.length();
  }
  
  private int replace(String template, StringBuilder builder,
                      Map map, String name, int start, int current) throws Exception {
    if(!map.containsKey(name)) return start;
    Object value = map.get(name);
    if(value == null) value = new String();
    builder.append(template.subSequence(start, current)).append(value.toString()); 
    return current + 1 + name.length();
  }
  
  private int replace(String template, StringBuilder builder,
                      Object object, String name, int start, int current) throws Exception {
    Field field = null;
    try {
      field = object.getClass().getDeclaredField(name);
    } catch (Exception e) {
    }
    if(field == null) return start;
    Object value = ReflectionUtil.getValue(object, field);
    if(value == null) value = new String();
    builder.append(template.subSequence(start, current)).append(value);
    return current + 1 + name.length();
  }*/

  public String encode(CharSequence seq) {
    if(seq.length() < 1) return seq.toString();
    StringBuilder builder = new StringBuilder();
    int i = 0;
    int start  = 0;
    while(i < seq.length()) {
      if(seq.charAt(i) == '\'') {
        builder.append(seq.subSequence(start, i)).append("''");
        start = i+1;
      }
      i++;
    }
    if(start > 0 && start < seq.length()) {
      builder.append(seq.subSequence(start, seq.length())); 
    }
    if(builder.length() < 1) return seq.toString(); 
    return builder.toString();
  }
  
}
