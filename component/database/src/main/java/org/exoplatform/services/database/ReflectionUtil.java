/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Mar 29, 2007  
 */
public class ReflectionUtil {
  
  public final static void setValue(Object bean, Field field, Object value) throws Exception {
//    Class clazz = bean.getClass();
//    Method method = getMethod("set", field, clazz);
//    if(method != null) return method.invoke(bean, new Object[]{});
//    method = getMethod("is", field, clazz);
//    if(method != null) return method.invoke(bean, new Object[]{});
//    field.setAccessible(true);
//    return field.get(bean);
  }
  
  public final static Object getValue(Object bean, Field field) throws Exception {
    Class clazz = bean.getClass();
    Method method = getMethod("get", field, clazz);
    if(method != null) return method.invoke(bean, new Object[]{});
    method = getMethod("is", field, clazz);
    if(method != null) return method.invoke(bean, new Object[]{});
    field.setAccessible(true);
    return field.get(bean);
  }

  public final static Method getMethod(String prefix, Field field, Class clazz) throws Exception {
    StringBuilder name = new StringBuilder(field.getName());
    name.setCharAt(0, Character.toUpperCase(name.charAt(0)));
    name.insert(0, prefix);
    try{
      Method method = clazz.getDeclaredMethod(name.toString(), new Class[]{});
      return method; 
    }catch (Exception e) {
    }
    return null;
  }
  
  public final static List<Method> getMethod(Class clazz, String name) throws Exception {
    Method [] methods = clazz.getDeclaredMethods();
    List<Method> list = new ArrayList<Method>();
    for(Method method : methods){
      if(method.getName().equals(name)) list.add(method);
    }
    return list;
  }
}
