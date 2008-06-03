/**
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

package org.exoplatform.services.xml.transform.impl.html;

import org.exoplatform.services.xml.transform.html.HTMLTransformerService;
import org.exoplatform.services.xml.transform.html.HTMLTransformer;
import javax.xml.transform.TransformerConfigurationException;
import org.exoplatform.services.xml.resolving.XMLResolvingService;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: TidyTransformerServiceImpl.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class TidyTransformerServiceImpl implements HTMLTransformerService {
  private XMLResolvingService resolvingService;

  public TidyTransformerServiceImpl(XMLResolvingService resolvingService) {
    this.resolvingService = resolvingService;
  }

  public HTMLTransformer getTransformer()
      throws TransformerConfigurationException {
    TidyTransformerImpl transf = new TidyTransformerImpl();
    transf.setResolvingService(resolvingService);
    return transf;
  }

}
