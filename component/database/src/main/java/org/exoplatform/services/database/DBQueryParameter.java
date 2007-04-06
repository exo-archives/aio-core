/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.util.Comparator;
/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Apr 2, 2007  
 */
public class DBQueryParameter {

  protected String name;
  protected String value;
  protected Operator operator;
  protected int order = WHERE;
  
  public final static int SELECT = 1, WHERE = 3, GROUP = 4, HAVING = 5, ORDER = 6;
  
  protected DBQueryParameter(){
  }
  
  public DBQueryParameter(String value) {
    this.value = value;
  }
  
  public DBQueryParameter(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public DBQueryParameter(String name, String value, Operator operator){
    this.name = name;
    this.value = value;
    this.operator = operator;
  }

  public void build(StringBuilder builder) {
    if(order == WHERE && builder.indexOf(" WHERE ") < 0) builder.append(" WHERE ");
    builder.append(' ').append(name).append(operator.toString()).append(value);
  }
  
  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  public Operator getOperator() { return operator; }

  public void setOperator(Operator operator) { this.operator = operator; }

  public String getValue() { return value; }

  public void setValue(String value) { this.value = value; }
  
  public final static Comparator<DBQueryParameter> PARAMETER_SORT = new Comparator<DBQueryParameter>(){
    public int compare(DBQueryParameter param1, DBQueryParameter param2) {
      return param1.order - param2.order;
    }
  };

  public static class Operator {

    public final static Operator AND = new Operator(" AND "); 
    public final static Operator OR = new Operator(" OR "); 
    public final static Operator LESS_THAN = new Operator(" < ");
    public final static Operator LESS_THAN_AND_EQUALS = new Operator(" <= ");
    public final static Operator GREATER_THAN = new Operator(" > ");
    public final static Operator GREATER_THAN_AND_EQUALS = new Operator(" >= "); 
    public final static Operator LIKE = new Operator(" LIKE "); 
    public final static Operator IN = new Operator(" IN "); 
    public final static Operator IS = new Operator(" IS "); 
    public final static Operator EQUALS = new Operator(" = ");

    private String value;

    public Operator(String value){this.value = value; }

    public String toString() { return value; }

    public String getValue() { return value; }

  }
}
