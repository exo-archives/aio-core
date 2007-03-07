/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.querying;

import java.io.InputStream;
import org.exoplatform.services.xml.querying.helper.SimpleStatementHelper;
import org.exoplatform.services.xml.querying.helper.XMLDataManager;



/**
 * Created by The eXo Platform SARL        .
 *
 * XML query entry point
* @author Gennady Azarenkov
 * @version $Id: XMLQueryingService.java 5799 2006-05-28 17:55:42Z geaz $
 */

public interface XMLQueryingService
{
  /**
   * Creates an empty query object
   * See xtas.sourceforge.net for details
   * @return xtas.Query object
   */
   XMLQuery createQuery();

   XMLQuery createQuery(Statement statement) throws InvalidSourceException;

   XMLQuery createQuery(Statement statement, InputStream inputStream) throws InvalidSourceException;

   SimpleStatementHelper createStatementHelper();

   XMLDataManager createXMLDataManager();

}
