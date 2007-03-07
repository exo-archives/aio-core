/***************************************************************************
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform.trax;

import java.util.Properties;
import javax.xml.transform.TransformerConfigurationException;


/**
 * Created by The eXo Platform SARL        .
 *
 * An object that implements this interface is the runtime
 *   representation of processed transformation instructions
 * Analog of javax.xml.transform.Templates
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
