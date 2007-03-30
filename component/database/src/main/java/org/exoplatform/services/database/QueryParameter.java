/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Mar 28, 2007  
 */
public class QueryParameter {
  
  private String name;
  private Object value;
  
  private Operator operator = Operator.EQUALS;
  private Operator combineOperator = Operator.AND;
  
  public  QueryParameter(String name, Object value){
    this.name  = name;
    this.value = value;
  }
  
  public  QueryParameter(String name, Object value, Operator operator){
    this(name, value);
    this.operator = operator;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Object getValue() { return value; }
  public void setValue(Object value) { this.value = value; }
  
  public Operator getCombineOperator() { return combineOperator; }
  public void setCombineOperator(Operator combineOperator) { 
    this.combineOperator = combineOperator;
  }

  public Operator getOparator() { return operator; }
  public void setOparator(Operator oparator) { this.operator = oparator; }
 
  public static enum Operator  {
    OR, AND, IS, LIKE , IN, EQUALS, LESS_THAN , GREATER_THAN
  }
}
