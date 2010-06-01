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

package org.exoplatform.services.document.test;

import java.io.InputStream;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.document.DocumentReaderService;
import org.exoplatform.test.BasicTestCase;

public class TestBigXLSDocumentReader extends BasicTestCase {
  DocumentReaderService service_;

  public void setUp() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance();
    service_ = (DocumentReaderService) pcontainer.getComponentInstanceOfType(DocumentReaderService.class);
  }
  
  /**
   * JCR-1398 Lucene threads blocked when uploading big text files in WebDAV
   * Uploading a big XLS file causes DocumentReader hangsup. 
   * @throws Exception
   */
  public void testBigXLSFile() throws Exception {
     InputStream is = TestBigXLSDocumentReader.class.getResourceAsStream("/testBigXLS.xls");
     String text = service_.getDocumentReader("application/excel").getContentAsText(is);
     assertNotNull(text);
     if(text.equals("")){
        fail("Must not be empty String");
     }
  }
}
