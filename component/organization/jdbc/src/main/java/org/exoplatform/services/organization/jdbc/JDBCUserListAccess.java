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
package org.exoplatform.services.organization.jdbc;

import java.sql.Connection;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.database.DAO;
import org.exoplatform.services.organization.User;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a>
 * @version $Id: JDBCUserListAccess.java 111 2008-11-11 11:11:11Z $
 */
public abstract class JDBCUserListAccess implements ListAccess<User> {

  /**
   * The DAO.
   */
  protected DAO    dao;

  /**
   * The find query string.
   */
  protected String findQuery;

  /**
   * The count query string.
   */
  protected String countQuery;

  /**
   * JDBCUserListAccess constructor.
   * 
   * @param dao
   *          The DAO
   * @param findQuery
   *          Find query string
   * @param countQuery
   *          Count query string
   */
  public JDBCUserListAccess(DAO dao, String findQuery, String countQuery) {
    this.dao = dao;
    this.findQuery = findQuery;
    this.countQuery = countQuery;
  }

  /**
   * {@inheritDoc}
   */
  public int getSize() throws Exception {
    Connection connection = null;
    try {
      connection = dao.getExoDatasource().getConnection();
      return getSize(connection);
    } finally {
      dao.getExoDatasource().closeConnection(connection);
    }
  }

  /**
   * {@inheritDoc}
   */
  public User[] load(int index, int length) throws Exception, IllegalArgumentException {
    Connection connection = null;
    try {
      connection = dao.getExoDatasource().getConnection();
      return load(connection, index, length);
    } finally {
      dao.getExoDatasource().closeConnection(connection);
    }
  }

  protected abstract User[] load(Connection connection, int index, int length) throws Exception;

  protected abstract int getSize(Connection connection) throws Exception;

}
