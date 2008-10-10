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

import org.exoplatform.commons.utils.MimeTypeResolver;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.document.DocumentReader;
import org.exoplatform.services.document.DocumentReaderService;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SAS Author : Sergey Karpenko
 * <sergey.karpenko@exoplatform.com.ua>
 * 
 * @version $Id: $
 */

public class TestHtmlDocumentReader extends BasicTestCase {
  DocumentReaderService service_;

  public void setUp() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance();
    service_ = (DocumentReaderService) pcontainer.getComponentInstanceOfType(DocumentReaderService.class);
  }

  public void testGetContentAsString() throws Exception {
    InputStream is = TestHtmlDocumentReader.class.getResourceAsStream("/test.html");
    MimeTypeResolver mimetypeResolver = new MimeTypeResolver();
    String mimeType = mimetypeResolver.getMimeType("test.html");

    DocumentReader dr = service_.getDocumentReader(mimeType);
    String text = dr.getContentAsText(is);
    // TODO text is too huge, need small test file
  }
}
