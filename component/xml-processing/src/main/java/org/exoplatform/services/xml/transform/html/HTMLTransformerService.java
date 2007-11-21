/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform.html;
import javax.xml.transform.TransformerConfigurationException;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: HTMLTransformerService.java 5799 2006-05-28 17:55:42Z geaz $
 */

public interface HTMLTransformerService {
    HTMLTransformer getTransformer() throws TransformerConfigurationException;
}
