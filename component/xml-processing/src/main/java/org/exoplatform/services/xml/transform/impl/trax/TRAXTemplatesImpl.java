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

package org.exoplatform.services.xml.transform.impl.trax;

import java.util.Properties;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;

import org.exoplatform.services.xml.resolving.XMLResolvingService;
import org.exoplatform.services.xml.transform.trax.TRAXTemplates;
import org.exoplatform.services.xml.transform.trax.TRAXTransformer;

/**
 * Created by The eXo Platform SAS . Wrapper for Trax Transformer.
 * 
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: TRAXTemplatesImpl.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class TRAXTemplatesImpl implements TRAXTemplates {
  private Templates           templates;

  private XMLResolvingService resolvingService;

  public TRAXTemplatesImpl(Templates templates) {
    this.templates = templates;
  }

  public Properties getOutputProperties() {
    return templates.getOutputProperties();
  }

  public TRAXTransformer newTransformer() throws TransformerConfigurationException {
    TRAXTransformerImpl transf = new TRAXTransformerImpl(this.templates);
    transf.setResolvingService(resolvingService);
    return transf;
  }

  public void setResolvingService(XMLResolvingService r) {
    resolvingService = r;
  }
}
