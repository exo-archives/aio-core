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

package org.exoplatform.services.xml.resolving;

import org.xml.sax.EntityResolver;

/**
 * Created by The eXo Platform SAS        .
 *
 * XML resolving service - an abstract EntityResolver creator.
* @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: XMLResolvingService.java 5799 2006-05-28 17:55:42Z geaz $
 */

public interface XMLResolvingService {
  /**
   * Returns a pre-created EntityResolver
   * @return EntityResolver object
   */
   EntityResolver getEntityResolver();

}
