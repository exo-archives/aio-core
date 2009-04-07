/**
 * 
 */
/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
package org.exoplatform.services.organization.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.organization.User;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a>
 * @version $Id: SimpliHibernateUserListAccess.java 111 2008-11-11 11:11:11Z $
 */
public class SimpleHibernateUserListAccess extends HibernateUserListAccess {

  /**
   * SimpliHibernateUserListAccess constructor.
   * 
   * @param service
   *          The Hibernate Service.
   * @param findQuery
   *          Find query string
   * @param countQuery
   *          Count query string
   */
  public SimpleHibernateUserListAccess(HibernateService service, String findQuery, String countQuery) {
    super(service, findQuery, countQuery);
  }

  /**
   * {@inheritDoc}
   */
  protected int getSize(Session session) throws Exception {
    List l = session.createQuery(countQuery).list();
    Number count = (Number) l.get(0);

    return count.intValue();
  }

  /**
   * {@inheritDoc}
   */
  protected User[] load(Session session, int index, int length) throws Exception {
    if (index < 0)
      throw new IllegalArgumentException("Illegal index: index must be a positive number");

    if (length < 0)
      throw new IllegalArgumentException("Illegal length: length must be a positive number");

    User[] users = new User[length];

    Query query = session.createQuery(findQuery);
    Iterator<Object> results = query.iterate();

    for (int p = 0, counter = 0; counter < length; p++) {
      if (!results.hasNext())
        throw new IllegalArgumentException("Illegal index or length: sum of the index and the length cannot be greater than the list size");

      Object result = results.next();

      if (p >= index) {
        users[counter++] = (User) result;
      }
    }

    return users;
  }

}
