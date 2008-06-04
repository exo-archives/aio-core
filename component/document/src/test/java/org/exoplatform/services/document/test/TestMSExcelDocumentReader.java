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

/**
 * Created by The eXo Platform SAS
 * Author : Sergey Karpenko <sergey.karpenko@exoplatform.com.ua>
 * @version $Id: $
 */

public class TestMSExcelDocumentReader extends BasicTestCase {
  DocumentReaderService service_;

  public void setUp() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    service_ =
      (DocumentReaderService) pcontainer.getComponentInstanceOfType(DocumentReaderService.class) ;
  }

  public void testGetContentAsString() throws Exception {
    InputStream is = TestMSExcelDocumentReader.class.getResourceAsStream("/test.xls");
    String text = service_.getDocumentReader("application/excel").getContentAsText(is);
    String etalon = 
      "Ronaldo Eric Cantona Kaka Ronaldonho ID Group Functionality Executor Begin End Tested "
      +"XNNL XNNL Xay dung vung quan li nguyen lieu NamPH 2005-02-02 00:00:00.000+0200 2005-10-02 00:00:00.000+0300 Tested "
      +"XNNL XNNL XNNL_HAVEST NamPH 1223554.0 2005-10-01 00:00:00.000+0300 Tested "
      +"XNNL XNNL XNNL_PIECE_OF_GROUND NamPH 2005-10-12 00:00:00.000+0300 2005-10-02 00:00:00.000+0300 Tested "
      +"XNNL XNNL XNNL_76 NamPH TRUE 1984-12-10 00:00:00.000+0200 No "
      +"XNNL XNNL XNNL_CREATE_REAP NamPH none 2005-10-03 00:00:00.000+0300 No "
      +"XNNL XNNL XNNL_SCALE NamPH 1984-12-10 00:00:00.000+0200 2005-10-05 00:00:00.000+0300 Tested "
      +"XNNL XNNL LASUCO_PROJECT NamPH 2005-10-05 00:00:00.000+0300 2005-10-06 00:00:00.000+0300 No "
      +"XNNL XNNL LASUCO_PROJECT NamPH Tested "
      +"XNNL XNNL XNNL_BRANCH NamPH 2005-12-12 00:00:00.000+0200 2005-06-10 00:00:00.000+0300 Tested "
      +"XNNL XNNL XNNL_SUGAR_RACE NamPH 2005-05-09 00:00:00.000+0300 2005-06-10 00:00:00.000+0300 No "
      +"XNNL XNNL F_XNNL_DISTRI NamPH 2005-05-09 00:00:00.000+0300 2005-06-10 00:00:00.000+0300 Tested "
      +"XNNL XNNL XNNL_LASUCO_USER NamPH 2005-09-09 00:00:00.000+0300 2005-06-10 00:00:00.000+0300 No ";
    assertEquals("Wrong string returned",etalon ,text );
    
  }
}
