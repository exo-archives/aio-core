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

package org.exoplatform.services.database.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.exoplatform.services.log.Log;

import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.naming.InitialContextInitializer;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady
 *         Azarenkov</a>
 * @version $Id: DBSchemaCreator.java 13053 2007-03-01 06:44:00Z tuan08 $
 */

public class DBSchemaCreator {

  static public String               SQL_DELIMITER_COMMENT_PREFIX = "/*$DELIMITER:";

  static public String               SQL_DELIMITER                = ";";

  static private String              SQL_ALREADYEXISTS            = ".*((already exist)|(duplicate key)| (already used)|(ORA-00955))+.*";

  private final Pattern              pattern;

  private static Log                 log                          = ExoLogger.getLogger("database.DBSchemaCreator");

  private List<CreateDBSchemaPlugin> createDBSchemaPlugins        = new ArrayList<CreateDBSchemaPlugin>();

  @SuppressWarnings("unused")
  public DBSchemaCreator(InitialContextInitializer contextInit) {
    pattern = Pattern.compile(SQL_ALREADYEXISTS, Pattern.CASE_INSENSITIVE);
  }

  // for testing only
  private DBSchemaCreator(String dsName, String script) throws SQLException, NamingException {
    pattern = Pattern.compile(SQL_ALREADYEXISTS, Pattern.CASE_INSENSITIVE);
    createTables(dsName, script);
  }

  public void createTables(String dsName, String script) throws NamingException, SQLException {
    InitialContext context = new InitialContext();
    DataSource ds = (DataSource) context.lookup(dsName);
    Connection conn = ds.getConnection();
    String sql = "";
    try {
      String[] scripts = null;
      if (script.startsWith(SQL_DELIMITER_COMMENT_PREFIX)) {
        // read custom prefix
        try {
          String s = script.substring(SQL_DELIMITER_COMMENT_PREFIX.length());
          int endOfDelimIndex = s.indexOf("*/");
          String delim = s.substring(0, endOfDelimIndex).trim();
          s = s.substring(endOfDelimIndex + 2).trim();
          scripts = s.split(delim);
        } catch (IndexOutOfBoundsException e) {
          log.warn("Error of parse SQL-script file. Invalid DELIMITER configuration. Valid format is '"
                       + SQL_DELIMITER_COMMENT_PREFIX
                       + "XXX*/' at begin of the SQL-script file, where XXX - DELIMITER string."
                       + " Spaces will be trimed. ",
                   e);
          log.info("Using DELIMITER:[" + SQL_DELIMITER + "]");
          scripts = script.split(SQL_DELIMITER);
        }
      } else {
        scripts = script.split(SQL_DELIMITER);
      }

      for (String scr : scripts) {
        String s = cleanWhitespaces(scr.trim());
        if (s.length() < 1)
          continue;
        sql = s;
        if (log.isDebugEnabled())
          log.debug("Execute script: \n[" + sql + "]");

        try {
          conn.setAutoCommit(false);
          conn.createStatement().executeUpdate(sql);
          conn.commit();
        } catch (SQLException e) {
          conn.rollback();
          // already exists check
          Matcher aeMatcher = pattern.matcher(e.getMessage().trim());
          if (!aeMatcher.matches())
            throw e;
          if (log.isDebugEnabled())
            log.debug(e.getMessage());
        }

      }
      log.info("DB schema of DataSource: '" + dsName + "' created succesfully. context " + context);
    } catch (SQLException e) {
      SQLException next = e.getNextException();
      String errorTrace = "";
      while (next != null) {
        errorTrace += next.getMessage() + "; ";
        next = e.getNextException();
      }
      Throwable cause = e.getCause();
      log.error("Could not create db schema of DataSource: '" + dsName + "'. Reason: "
          + e.getMessage() + "; " + errorTrace
          + (cause != null ? " (Cause: " + cause.getMessage() + ")" : "") + ". Last command: "
          + sql);
      e.printStackTrace();
    } finally {
      conn.close();
    }

  }

  public void addPlugin(ComponentPlugin plugin) {
    if (plugin instanceof CreateDBSchemaPlugin) {
      CreateDBSchemaPlugin csplugin = (CreateDBSchemaPlugin) plugin;
      try {
        createTables(csplugin.getDataSource(), csplugin.getScript());
        createDBSchemaPlugins.add(csplugin);
      } catch (NamingException e) {
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @SuppressWarnings("unused")
  public ComponentPlugin removePlugin(String name) {
    return null;
  }

  public Collection getPlugins() {
    return createDBSchemaPlugins;
  }

  // for testing
  public static DBSchemaCreator initialize(String dsName, String script) throws SQLException,
                                                                        NamingException {
    return new DBSchemaCreator(dsName, script);
  }

  static public String cleanWhitespaces(String string) {
    if (string == null || string.length() < 1)
      return string;
    char[] cc = string.toCharArray();
    for (int ci = cc.length - 1; ci > 0; ci--) {
      if (Character.isWhitespace(cc[ci]))
        cc[ci] = ' ';
    }
    return new String(cc);
  }

}
