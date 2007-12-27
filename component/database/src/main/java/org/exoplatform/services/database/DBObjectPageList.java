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

import java.util.List;

import org.exoplatform.commons.utils.PageList;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 21, 2004
 * @version $Id: DBObjectPageList.java 5332 2006-04-29 18:32:44Z geaz $
 */
public class DBObjectPageList extends PageList {
 
  private String findQuery_ ;
  private String countQuery_ ;
  private HibernateService service_ ;
  
  public DBObjectPageList(HibernateService service, Class objectType) throws Exception {
    super(20) ;
    service_ = service ;
    findQuery_ = "from o in class " + objectType.getName() ;
    countQuery_ = "select count(o) from " + objectType.getName()  + " o" ;
    Session session = service_.openSession() ;
    List l  = session.createQuery(countQuery_).list() ;
    Number count = (Number) l.get(0) ;
    setAvailablePage(count.intValue()) ;    
  }
  
  public DBObjectPageList(HibernateService service, ObjectQuery  oq) throws Exception {
    super(20) ;
    service_ = service ;
    findQuery_ = oq.getHibernateQuery() ;
    countQuery_ = oq.getHibernateCountQuery() ;
    Session session = service_.openSession() ;
    List l  = session.createQuery(countQuery_).list() ;
    Number count = (Number) l.get(0) ;
    setAvailablePage(count.intValue()) ;
  }
  
  public DBObjectPageList(HibernateService service, int pageSize,
                          String query, String countQuery) throws Exception {
    super(pageSize) ;
    service_ = service ;
    findQuery_ =  query ;
    countQuery_ =  countQuery ;
    Session session = service_.openSession() ;
    List l  = session.createQuery(countQuery_).list() ;
    Number count = (Number) l.get(0) ;
    setAvailablePage(count.intValue()) ;
  }
  
  @SuppressWarnings("unused")
  protected void populateCurrentPage(int page) throws Exception  {
    Session session = service_.openSession() ;
    Query query = session.createQuery(findQuery_);
    int from = getFrom() ;
    query.setFirstResult(from);
    query.setMaxResults(getTo()- from) ;
    currentListPage_ = query.list() ;
  }
  
  public List getAll() throws Exception  { 
    Session session = service_.openSession() ;
    Query query = session.createQuery(findQuery_);
    return query.list() ;
  }
}