/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public interface HibernateService  {
  public Configuration getHibernateConfiguration() ; 
  public Session openSession();
  public Session openNewSession();
  public void closeSession(Session session) ;
  /**Close the session that assign to the current thread */
  public void closeSession() ;
  SessionFactory getSessionFactory(); 
  
  public Object findOne(Session session, String query, String id) throws Exception ;
  public Object findExactOne(Session session, String query, String id) throws Exception ;
  public Object findOne(Class clazz, java.io.Serializable id) throws Exception ;
  public Object findOne(ObjectQuery q) throws Exception ;
  public Object create(Object obj) throws Exception  ;
  public Object update(Object obj) throws Exception  ;
  public Object save(Object obj) throws Exception ;
  public Object remove(Object obj) throws Exception  ;
  public Object remove(Class clazz , Serializable id) throws Exception  ;
  public Object remove(Session session ,Class clazz , Serializable id) throws Exception  ;
}
