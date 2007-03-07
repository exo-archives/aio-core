/***************************************************************************
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform.trax;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import org.exoplatform.services.xml.transform.NotSupportedIOTypeException;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: TRAXTransformerService.java 5799 2006-05-28 17:55:42Z geaz $
 */
public interface TRAXTransformerService {
    TRAXTransformer getTransformer() throws
            TransformerConfigurationException;

    TRAXTransformer getTransformer(Source source) throws
            TransformerConfigurationException;

    TRAXTemplates getTemplates(Source source) throws
            TransformerException,NotSupportedIOTypeException;
}
