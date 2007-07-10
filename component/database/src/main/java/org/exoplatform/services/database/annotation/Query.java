/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Mar 30, 2007  
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Query {  
  String name() ;
  String standardSQL();
  String mysqlSQL() default "";
  String mssqlSQL() default "";
  String oracleSQL() default "";
  String postgresSQL() default "";
  String hsqlSQL() default "";
  String derbySQL() default "";
  String sysbaseSQL() default "";
  String db2SQL() default "";
}
