/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.database;

import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SAS
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
