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

package org.exoplatform.services.xml.transform.trax;

import java.util.Properties;
import javax.xml.transform.TransformerConfigurationException;

/**
 * Created by The eXo Platform SAS .
 * 
 * An object that implements this interface is the runtime representation of
 * processed transformation instructions Analog of javax.xml.transform.Templates
 * 
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: TRAXTemplates.java 5799 2006-05-28 17:55:42Z geaz $
 * 
 * @see javax.xml.transform.Templates
 */
public interface TRAXTemplates {

  /**
   * @see javax.xml.transform.Templates#getOutputProperties()
   */
  Properties getOutputProperties();

  TRAXTransformer newTransformer() throws TransformerConfigurationException;

}
