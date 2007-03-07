/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.querying.impl.xtas.helper;

import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.exoplatform.services.xml.querying.Statement;
import org.exoplatform.services.xml.querying.XMLData;
import org.exoplatform.services.xml.querying.XMLWellFormedData;
import org.exoplatform.services.xml.querying.helper.SimpleStatementHelper;
import org.exoplatform.services.xml.querying.impl.xtas.QueryType;
import org.exoplatform.services.xml.querying.impl.xtas.SimpleStatement;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormTreeFragment;
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;



/**
 * Created by The eXo Platform SARL        .
 *
 * Just set of static helpers to prepare xtas's SimpleStatement objects
 *
* @author Gennady Azarenkov
 * @version $Id: SimpleStatementHelperImpl.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class SimpleStatementHelperImpl implements SimpleStatementHelper
{
  /**
   * Select statement helper
   * @param match - XPath expression
   * @return Statement object
   */
   public Statement select(String match) throws InvalidStatementException
   {
      return select(match, null, null);
   }

  /**
   * Select statement helper
   * @param match - XPath expression
   * @param resource - XTAS resource identifier (will be used for both source and destination)
   * @return Statement object
   */
    public Statement select(String match, String resource) throws InvalidStatementException
   {
      return select(match, resource, resource);
   }

  /**
   * Select statement helper
   * @param match - XPath expression
   * @param source - XTAS resource identifier for query source
   * @param destination - XTAS resource identifier for query destination
   * @return Statement object
   */
   public Statement select(String match, String source, String destination) throws InvalidStatementException
   {
      return new SimpleStatement( QueryType.SELECT, match, (UniFormTreeFragment)null, source, destination);
   }

  /**
   * Delete statement helper
   * @param match - XPath expression
   * @return Statement object
   */
   public Statement delete(String match) throws InvalidStatementException
   {
       return delete(match, null, null);
   }

  /**
   * Delete statement helper
   * @param match - XPath expression
   * @param resource - XTAS resource identifier (will be used for both source and destination)
   * @return Statement object
   */
   public Statement delete(String match, String resource) throws InvalidStatementException
   {
       return delete(match, resource, resource);
   }

  /**
   * Delete statement helper
   * @param match - XPath expression
   * @param source - XTAS resource identifier for query source
   * @param destination - XTAS resource identifier for query destination
   * @return Statement object
   */
   public Statement delete(String match, String source, String destination) throws InvalidStatementException
   {
        return new SimpleStatement( QueryType.DELETE, match, (UniFormTreeFragment)null, source, destination);
   }

  /**
   * Append statement helper
   * @param match - XPath expression
   * @param value - new node(set) value
   * @return Statement object
   */
   public Statement append(String match, XMLData value) throws InvalidStatementException
   {
        return append(match, null, null, (UniFormTree)value);
   }

  /**
   * Append statement helper
   * @param match - XPath expression
   * @param resource - XTAS resource identifier (will be used for both source and destination)
   * @param value - new node(set) value
   * @return Statement object
   */
   public Statement append(String match, String resource, XMLData value) throws InvalidStatementException
   {
        return append(match, resource, resource, (UniFormTree)value);
   }

  /**
   * Append statement helper
   * @param match - XPath expression
   * @param source - XTAS resource identifier for query source
   * @param destination - XTAS resource identifier for query destination
   * @param value - new node(set) value
   * @return Statement object
   */
   public Statement append(String match, String source, String destination, XMLData value) throws InvalidStatementException
   {
       return new SimpleStatement( QueryType.APPEND, match, (UniFormTree)value, source, destination);
   }

  /**
   * Update statement helper
   * @param match - XPath expression
   * @param value - new node(set) value
   * @return Statement object
   */
   public Statement update(String match, XMLData value) throws InvalidStatementException
   {
       return update(match, null, null, (UniFormTree)value);
   }

  /**
   * Update statement helper
   * @param match - XPath expression
   * @param resource - XTAS resource identifier (will be used for both source and destination)
   * @param value - new node(set) value
   * @return Statement object
   */
   public Statement update(String match, String resource, XMLData value) throws InvalidStatementException
   {
       return update(match, resource, resource, (UniFormTree)value);
   }

  /**
   * Update statement helper
   * @param match - XPath expression
   * @param source - XTAS resource identifier for query source
   * @param destination - XTAS resource identifier for query destination
   * @param value - new node(set) value
   * @return Statement object
   */
   public Statement update(String match, String source, String destination, XMLData value) throws InvalidStatementException
   {
       return new SimpleStatement( QueryType.UPDATE, match, (UniFormTree)value, source, destination);
   }

  /**
   * Create Resource statement helper
   * @param resource - XTAS resource identifier
   * @param initTree - initial node
   * @return Statement object
   */
   public Statement create(String resource, XMLWellFormedData initTree) throws InvalidStatementException
   {
       return new SimpleStatement( QueryType.CREATE, resource, (WellFormedUniFormTree)initTree, null, resource);
   }

  /**
   * Drop Resource statement helper
   * @param resource - XTAS resource identifier
   * @return Statement object
   */
   public Statement drop(String resource) throws InvalidStatementException
   {
       return new SimpleStatement( QueryType.DROP, resource, (UniFormTreeFragment)null, null, resource);
   }

}
