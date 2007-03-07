/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.InputStream;
import org.exoplatform.services.xml.querying.InvalidSourceException;
import org.exoplatform.services.xml.querying.Statement;
import org.exoplatform.services.xml.querying.XMLQuery;
import org.exoplatform.services.xml.querying.XMLQueryingService;
import org.exoplatform.services.xml.querying.helper.SimpleStatementHelper;
import org.exoplatform.services.xml.querying.helper.XMLDataManager;
import org.exoplatform.services.xml.querying.impl.xtas.helper.SimpleStatementHelperImpl;
import org.exoplatform.services.xml.querying.impl.xtas.helper.XMLDataManagerFacade;



/**
 * Created by The eXo Platform SARL        .
 *
 * Xtas query entry point implementation
* @author Gennady Azarenkov
 * @version $Id: XMLQueryingServiceImpl.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class XMLQueryingServiceImpl implements XMLQueryingService
{
  /**
   * Creates an empty query object
   * Typical lifecycle: prepare()->[setInput()]->execute()->getResult()|serialize()
   * See xtas.sourceforge.net for details
   * @return xtas.Query object
   */
   public XMLQuery createQuery()
   {
       return new Query();
   }

   public XMLQuery createQuery(Statement statement) throws InvalidSourceException
   {
       return new Query(statement);
   }

   public XMLQuery createQuery(Statement statement, InputStream inputStream) throws InvalidSourceException
   {
       return new Query(statement, inputStream);
   }

   public SimpleStatementHelper createStatementHelper()
   {
       return new SimpleStatementHelperImpl();
   }

   public XMLDataManager createXMLDataManager() {

       return new XMLDataManagerFacade();
   }

}
