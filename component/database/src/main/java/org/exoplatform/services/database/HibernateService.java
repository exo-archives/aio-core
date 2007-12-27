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

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by The eXo Platform SAS        .
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
