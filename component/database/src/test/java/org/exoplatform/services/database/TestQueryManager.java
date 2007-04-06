/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Mar 30, 2007  
 */
public class TestQueryManager extends BasicTestCase {

  QueryBuilder manager_ ;

  public TestQueryManager(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    manager_ = new QueryBuilder();
  }

  public void testPaser() throws Exception {
    String template = "select name from $table where id = $id and name like '&yahoo'";
    String[][] parameters = {{"table", "student"}, {"id", "12345"}};
    String pamameterSql = manager_.mapDataToSql(template, parameters);
    
   /* String [] array = {"table", "student"};
    String arraySql = manager_.mapDataToSql(template, array);
    array = new String[]{"id", "12345"};
    arraySql = manager_.mapDataToSql(arraySql, array);
    
    assertEquals(pamameterSql, arraySql);
    
    Map<String, String> map  = new HashMap<String, String>();
    map.put("table", "student");
    map.put("id", "12345");
    String mapSql = manager_.mapDataToSql(template, map);
    assertEquals(mapSql, arraySql);
    
    String beanSql = manager_.mapDataToSql(template, new Table());
    assertEquals(beanSql, arraySql);*/
   
    System.out.println("\n\n\n "+pamameterSql+"\n\n");
  }
  
//  private class Table {
//    private String table = "student";
//    private String id = "12345";
//  }

  static public class Student {

    String name, value;
    public Student(String n, String v){
      name = n;
      value = v;
    }

    public String getName() { return name; }

    public void setValue(String value) { this.value = value; }
  }

}
