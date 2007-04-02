/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.annotation.TableField;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Apr 2, 2007  
 */
public class ReflectionMapper<T extends DBObject> implements DBObjectMapper<T> {

  public String[][] toParameters(T bean) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    getParameters(bean, bean.getClass(), map);
    
    String [][]  parameters = new String[map.size()][2];
    Iterator<String> iter = map.keySet().iterator();
    int i = 0;
    while(iter.hasNext()) {
      parameters[i][0] = iter.next();
      parameters[i][1] = map.get(parameters[i][0]);
      i++;
    }
    return parameters;
  }
  
  private void getParameters(Object bean, Class clazz, Map<String, String> map) throws Exception {
    Field [] fields = clazz.getDeclaredFields();   
    for(int i = 0; i < fields.length; i++) {
      Object value = ReflectionUtil.getValue(bean, fields[i]);
      if(value == null) value = new String();
      if(value instanceof Calendar) {
        value =  new java.sql.Date(( (Calendar)value).getTimeInMillis());
      } else if(value instanceof Date) {
        value =  new java.sql.Date(( (Date)value).getTime());
      }
      String name = fields[i].getName();
      int k = name.length() - 1;
      while(k > -1){
        if(Character.isLetterOrDigit(name.charAt(k)) ) break;
        k--;
      }
      if(k < name.length() - 1) name = name.substring(0, k+1);
      map.put(name, value.toString());
    }
    if(clazz == DBObject.class) return;
    Class superClazz = clazz.getSuperclass();
    getParameters(superClazz.cast(bean), superClazz, map);
  }
  
  
  public void mapResultSet(ResultSet resultSet, T bean) throws Exception {
    Class<? extends DBObject> clazz = bean.getClass();   
    Table table = clazz.getAnnotation(Table.class);
    TableField[] tableFields =  table.field() ;
    
    ResultSetMetaData rsmd = resultSet.getMetaData();
    int numberOfColumns = rsmd.getColumnCount();
    
    for(int i=1; i<=numberOfColumns; i++) {
      String name  = rsmd.getColumnName(i);
      TableField tableField = searchTableField(tableFields, name);
      if(tableField == null) continue;
      String fieldName = tableField.field().length() == 0 ? tableField.name() : tableField.field();
      Field field = getField(clazz, fieldName);
      if(field == null) continue;
      ReflectionUtil.setValue(bean, field, getValue(rsmd.getColumnType(i), resultSet, name));
    }
  }
  
  public void mapUpdate(T bean, PreparedStatement statement) throws Exception {
    Class<? extends DBObject> clazz = bean.getClass();
    Table table = clazz.getAnnotation(Table.class);
    TableField[] tableFields =  table.field() ;
    
    int i = 1;
    for(TableField tableField : tableFields) {
      String fieldName = tableField.field().length() == 0 ? tableField.name() : tableField.field();
      Field field = getField(clazz, fieldName);
      if(field == null) continue;
      statement.setObject(i, ReflectionUtil.getValue(bean, field));
      i++;
    }
  }
  
  private Field getField(Class clazz, String name) throws Exception {
    Field field = clazz.getDeclaredField(name);
    if(field != null) return field;
    if(clazz == DBObject.class) return null;
    return getField(clazz.getSuperclass(), name);
  }
  
  //TODO will review 
  private Object getValue(int type, ResultSet resultSet, String name) throws Exception {
    switch(type){
      case Types.CLOB :
        return loadClob(resultSet, name);
      case Types.BLOB :
        return loadBlob(resultSet, name);
      case Types.BINARY :
        return loadBinary(resultSet, name);
      default :
        return resultSet.getObject(name.toUpperCase());
    }
  }
  
  private synchronized byte [] loadBinary(ResultSet resultSet, String name) throws Exception {
    InputStream input = resultSet.getBinaryStream(name);
    if(input == null) return null;
    ByteArrayOutputStream output = loadInputStream(input);
    return output.toByteArray();
  }

  private synchronized byte [] loadBlob(ResultSet resultSet, String name) throws Exception {
    Blob clob = resultSet.getBlob(name);  
    if(clob == null) return null;
    ByteArrayOutputStream output = loadInputStream(clob.getBinaryStream());
    return output.toByteArray();
  }
  
  private synchronized String loadClob(ResultSet resultSet, String name) throws Exception {
    Clob clob = resultSet.getClob(name);  
    if(clob == null) return null;
    Reader input = clob.getCharacterStream();    
    if(input == null) return  null;
    LineNumberReader lineReader = new LineNumberReader(input); 
    StringBuilder builder = new StringBuilder(); 
    String line;
    while((line = lineReader.readLine()) != null){
      builder.append(line);
    }    
    return builder.toString();
  }
  
  private ByteArrayOutputStream loadInputStream(InputStream input) throws Exception {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] data  = new byte[1024];      
    int available = -1;
    while( (available = input.read(data)) > -1){
      output.write(data, 0, available);
    }   
    return output;
  }
  
  private TableField searchTableField(TableField[] tableFields, String name) {
    for(TableField tableField : tableFields) {
      if(tableField.name().equals(name) && tableField.field().length() > 0) return tableField;
    }
    for(TableField field : tableFields) {
      if(field.name().equals(name) && field.field().length() < 1) return field;
    }
    return null;  
  }

}
