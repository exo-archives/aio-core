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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.exoplatform.services.database.DAO;
import org.exoplatform.services.database.DBObject;
import org.exoplatform.services.organization.User;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a>
 * @version $Id: SimpleJDBCUserListAccess.java 111 2008-11-11 11:11:11Z $
 */
public class SimpleJDBCUserListAccess extends JDBCUserListAccess {

  public SimpleJDBCUserListAccess(DAO dao, String findQuery, String countQuery) {
    super(dao, findQuery, countQuery);
  }

  /**
   * {@inheritDoc}
   */
  protected User[] load(Connection connection, int index, int length) throws Exception {
    if (index < 0)
      throw new IllegalArgumentException("Illegal index: index must be a positive number");

    if (length < 0)
      throw new IllegalArgumentException("Illegal length: length must be a positive number");

    User[] users = new User[length];

    Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                     ResultSet.CONCUR_READ_ONLY);
    ResultSet resultSet = statement.executeQuery(findQuery);

    for (int p = 0, counter = 0; counter < length; p++) {
      if (resultSet.isAfterLast())
        throw new IllegalArgumentException("Illegal index or length: sum of the index and the length cannot be greater than the list size");

      resultSet.next();

      DBObject bean = dao.createInstance();
      dao.getDBObjectMapper().mapResultSet(resultSet, bean);

      if (p >= index) {
        users[counter++] = (User) bean;
      }
    }

    resultSet.close();
    statement.close();

    return users;
  }

  @Override
  protected int getSize(Connection connection) throws Exception {
    Object retObj = dao.loadDBField(countQuery);

    if (retObj instanceof Integer) {
      return ((Integer) retObj).intValue();
    } else if (retObj instanceof BigDecimal) {
      return ((BigDecimal) retObj).intValue();
    } else {
      return ((Long) retObj).intValue();
    }
  }

}
