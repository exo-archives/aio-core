/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 25, 2004
 * @version $Id: ObjectQuery.java 6006 2006-06-06 10:01:27Z thangvn $
 */
public class ObjectQuery {
  private static SimpleDateFormat ft_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") ;
  private Class type_ ;
  private String orderBy_ ;
  private String groupBy_ ;
  private List  parameters_ ;  
  private List selectParameter_ ;
  
  public ObjectQuery(Class type) {
    type_ = type ;
    parameters_ = new ArrayList(3) ;
    selectParameter_ = new ArrayList(10) ;
  }
  
  public ObjectQuery addEQ(String field, Object value) {
    if(value != null) {
      parameters_.add(new Parameter(" = ", field, value)) ;
    }
    return this ;
  }
  
  public ObjectQuery addGT(String field, Object value) {
    if(value != null) {
      parameters_.add(new Parameter(" > ", field, value)) ;
    }
    return this ;
  }
  
  public ObjectQuery addLT(String field, Object value) {
    if(value != null) {
      parameters_.add(new Parameter(" < ", field, value)) ;
    }
    return this ;
  }
  
  public ObjectQuery addLIKE(String field, String value) {
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
  public ObjectQuery addSUM(String field) {
    selectParameter_.add(new Parameter("sum", field)) ;
    return this ;
  }
  public ObjectQuery addSelect(String field) {
    selectParameter_.add(new Parameter("fieldselect", field)) ;
    return this ;
  }
  public ObjectQuery addSelectCount(String type) {
    selectParameter_.add(new Parameter("countselect", type)) ;
    return this ;
  }
  public ObjectQuery addSelectMaxMin(String op, String field) {
    selectParameter_.add(new Parameter(op, field)) ;
    return this ;
  }
  
  public ObjectQuery setGroupBy(String field) {
    groupBy_ = " group by o." + field ;
    return this ;
  }
  
  public ObjectQuery setAscOrderBy(String field) {
    orderBy_ = " order by o." + field + " asc";
    return this ;
  }
  
  public ObjectQuery setDescOrderBy(String field) {
    orderBy_ = " order by o." + field + " desc";
    return this ;
  }
  
  public String getHibernateQuery() {
    StringBuffer b = new StringBuffer() ;
    b.append("from o in class ").append(type_.getName()) ;
    if(parameters_.size() > 0) {
      b.append(" where ") ;
      for(int i = 0; i < parameters_.size(); i ++) {
        if(i > 0) b.append(" and ") ;
        Parameter p = (Parameter) parameters_.get(i) ;
        if(p.value_ instanceof String) {
          b.append(" o.").append(p.field_).append(p.op_).append("'").append(p.value_).append("'") ;
        } else if(p.value_ instanceof Date) {
          String value = ft_.format((Date) p.value_) ;
          b.append(" o.").append(p.field_).append(p.op_).append("'").append(value).append("'") ;
        } else {
          b.append(" o.").append(p.field_).append(p.op_).append(p.value_);
        }
      }
    }
    if(orderBy_ != null )   b.append(orderBy_ );
    return b.toString() ;
  }
  
  public String getHibernateGroupByQuery() {
    StringBuffer b = new StringBuffer() ;
    b.append("select ") ;
    if(selectParameter_.size() > 0){
      for(int i = 0; i < selectParameter_.size(); i++){
        Parameter p = (Parameter)selectParameter_.get(i) ;
        if(p.op_.equals("fieldselect")){
          b.append("o.").append(p.field_) ;
        }else if(p.op_.equals("countselect")){
          b.append("count");
          if (p.field_ != "" || p.field_.length() > 0){
            b.append("(").append(p.field_).append(" o)");
          }else{
            b.append("(o)");
          }          
        }else {
          b.append(p.op_).append("(").append("o.").append(p.field_).append(") ");
        }        
        if(i < selectParameter_.size() - 1 ) b.append(" , ") ;       
      }
    }
    b.append(" from o in class ").append(type_.getName()) ;
    if(parameters_.size() > 0) {
      b.append(" where ") ;
      for(int i = 0; i < parameters_.size(); i ++) {
        if(i > 0) b.append(" and ") ;
        Parameter p = (Parameter) parameters_.get(i) ;
        if(p.value_ instanceof String) {
          b.append(" o.").append(p.field_).append(p.op_).append("'").append(p.value_).append("'") ;
        } else if(p.value_ instanceof Date) {
          String value = ft_.format((Date) p.value_) ;
          b.append(" o.").append(p.field_).append(p.op_).append("'").append(value).append("'") ;
        } else if(p.op_.equals("max") || p.op_.equals("min")){
          b.append(p.op_).append("(").append("o.").append(p.field_).append(") ");
        } else{
          b.append(" o.").append(p.field_).append(p.op_).append(p.value_);
        }
      }
    }
    if(groupBy_ != null )   b.append(groupBy_ );
    if(orderBy_ != null )   b.append(orderBy_ );
    return b.toString() ;
  }
  public String getHibernateCountQuery() {
    StringBuffer b = new StringBuffer() ;
    b.append("select count(o) from o in class ").append(type_.getName()) ;
    if(parameters_.size() > 0) {
      b.append(" where ") ;
      for(int i = 0; i < parameters_.size(); i ++) {
        if(i > 0) b.append(" and ") ;
        Parameter p = (Parameter) parameters_.get(i) ;
        if(p.value_ instanceof String) {
          b.append(" o.").append(p.field_).append(p.op_).append("'").append(p.value_).append("'") ;
        } else if(p.value_ instanceof Date) {
          String value = ft_.format((Date) p.value_) ;
          b.append(" o.").append(p.field_).append(p.op_).append("'").append(value).append("'") ;
        } else {
          b.append(" o.").append(p.field_).append(p.op_).append(p.value_);
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
