/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.exoplatform.services.database.annotation.Table;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 25, 2004
 * @version $Id: ObjectQuery.java 6006 2006-06-06 10:01:27Z thangvn $
 */
public class DBObjectQuery  {
  
  private static SimpleDateFormat ft_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") ;
  private Class<? extends DBObject> type_ ;
  private String orderBy_ ;
  private String groupBy_ ;
  private List<Parameter>  parameters_ ;  
  private List<Parameter> selectParameter_ ;
  
  public DBObjectQuery(Class<? extends DBObject> type) {
    type_ = type ;
    parameters_ = new ArrayList<Parameter>(3) ;
    selectParameter_ = new ArrayList<Parameter>(10) ;
  }
  
  public DBObjectQuery addEQ(String field, Object value) {
    if(value != null) {
      parameters_.add(new Parameter(" = ", field, value)) ;
    }
    return this ;
  }
  
  public DBObjectQuery addGT(String field, Object value) {
    if(value != null) {
      parameters_.add(new Parameter(" > ", field, value)) ;
    }
    return this ;
  }
  
  public DBObjectQuery addLT(String field, Object value) {
    if(value != null) {
      parameters_.add(new Parameter(" < ", field, value)) ;
    }
    return this ;
  }
  
  public DBObjectQuery addLIKE(String field, String value) {
    if(value != null && value.length() > 0)  {
      parameters_.add(new Parameter(" like ", field, optimizeInputString(value))) ;
    }
    return this ;
  }
  
  public String optimizeInputString(String value){
    value = value.replace('*', '%');
    value = value.replaceAll("'", "&#39;");
    value = value.replaceAll("<", "&#60;");
    value = value.replaceAll(">", "&#62;");
    return value;
  }
  
  public DBObjectQuery addSUM(String field) {
    selectParameter_.add(new Parameter("sum", field)) ;
    return this ;
  }
  
  public DBObjectQuery addSelect(String field) {
    selectParameter_.add(new Parameter("fieldselect", field)) ;
    return this ;
  }
  
  public DBObjectQuery addSelectCount(String type) {
    selectParameter_.add(new Parameter("countselect", type)) ;
    return this ;
  }
  
  public DBObjectQuery addSelectMaxMin(String op, String field) {
    selectParameter_.add(new Parameter(op, field)) ;
    return this ;
  }
  
  public DBObjectQuery setGroupBy(String field) {
    groupBy_ = " GROUP BY " + field ;
    return this ;
  }
  
  public DBObjectQuery setAscOrderBy(String field) {
    orderBy_ = " ORDER BY " + field + " ASC";
    return this ;
  }
  
  public DBObjectQuery setDescOrderBy(String field) {
    orderBy_ = " ORDER BY " + field + " DESC";
    return this ;
  }
  
  public String toQuery() {
    StringBuilder b = new StringBuilder(" SELECT * ") ;
    Table table = type_.getAnnotation(Table.class) ;   
    b.append(" FROM ").append(table.name()) ;
    if(parameters_.size() > 0) {
      b.append(" WHERE ") ;
      for(int i = 0; i < parameters_.size(); i ++) {
        if(i > 0) b.append(" AND ") ;
        Parameter p = parameters_.get(i) ;
        if(p.value_ instanceof String) {
          b.append(" ").append(p.field_).append(p.op_).append("'").append(p.value_).append("'") ;
        } else if(p.value_ instanceof Date) {
          String value = ft_.format((Date) p.value_) ;
          b.append(" ").append(p.field_).append(p.op_).append("'").append(value).append("'") ;
        } else {
          b.append(" ").append(p.field_).append(p.op_).append(p.value_);
        }
      }
    }
    if(orderBy_ != null )   b.append(orderBy_ );
    return b.toString() ;
  }
  
  public String getHibernateGroupByQuery() {
    StringBuilder b = new StringBuilder("SELECT ") ;
    if(selectParameter_.size() > 0){
      for(int i = 0; i < selectParameter_.size(); i++){
        Parameter p = selectParameter_.get(i) ;
        if(p.op_.equals("fieldselect")){
          b.append(p.field_) ;
        }else if(p.op_.equals("countselect")){
          b.append("COUNT");
          if (p.field_ != "" || p.field_.length() > 0){
            b.append("(").append(p.field_).append(" )");
          }else{
            b.append("(*)");
          }          
        }else {
          b.append(p.op_).append("(").append(p.field_).append(") ");
        }        
        if(i < selectParameter_.size() - 1 ) b.append(" , ") ;       
      }
    }
    Table table = type_.getAnnotation(Table.class) ; 
    b.append(" FROM ").append(table.name()) ;
    if(parameters_.size() > 0) {
      b.append(" WHERE ") ;
      for(int i = 0; i < parameters_.size(); i ++) {
        if(i > 0) b.append(" AND ") ;
        Parameter p = parameters_.get(i) ;
        if(p.value_ instanceof String) {
          b.append(p.field_).append(p.op_).append("'").append(p.value_).append("'") ;
        } else if(p.value_ instanceof Date) {
          String value = ft_.format((Date) p.value_) ;
          b.append(' ').append(p.field_).append(p.op_).append("'").append(value).append("'") ;
        } else if(p.op_.equals("max") || p.op_.equals("min")){
          b.append(p.op_).append("(").append(p.field_).append(") ");
        } else{
          b.append(' ').append(p.field_).append(p.op_).append(p.value_);
        }
      }
    }
    if(groupBy_ != null )   b.append(groupBy_ );
    if(orderBy_ != null )   b.append(orderBy_ );
    return b.toString() ;
  }
  public String getHibernateCountQuery() {
    StringBuffer b = new StringBuffer() ;
    Table table = type_.getAnnotation(Table.class) ; 
    b.append("SELECT COUNT(*) FROM  ").append(table.name()) ;
    if(parameters_.size() > 0) {
      b.append(" WHERE ") ;
      for(int i = 0; i < parameters_.size(); i ++) {
        if(i > 0) b.append(" AND ") ;
        Parameter p = parameters_.get(i) ;
        if(p.value_ instanceof String) {
          b.append(' ').append(p.field_).append(p.op_).append("'").append(p.value_).append("'") ;
        } else if(p.value_ instanceof Date) {
          String value = ft_.format((Date) p.value_) ;
          b.append(' ').append(p.field_).append(p.op_).append("'").append(value).append("'") ;
        } else {
          b.append(' ').append(p.field_).append(p.op_).append(p.value_);
        }
      }
    }
    return b.toString() ;
  }
  
  static class Parameter {
    
    String op_ ;
    String field_ ;
    String label_ ;
    Object value_ ;
    
    Parameter(String op, String field , Object value) {
      op_ = op ;
      field_ = field ;
      value_ = value ;
    }
    Parameter(String op, String field) {
      op_ = op ;
      field_ = field ;      
    }    
  }
}
