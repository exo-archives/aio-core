/***************************************************************************
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform.impl.html;

import org.exoplatform.services.xml.transform.html.HTMLTransformerService;
import org.exoplatform.services.xml.transform.html.HTMLTransformer;
import javax.xml.transform.TransformerConfigurationException;
import org.exoplatform.services.xml.resolving.XMLResolvingService;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: TidyTransformerServiceImpl.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class TidyTransformerServiceImpl implements HTMLTransformerService
{
    private XMLResolvingService resolvingService;

    public TidyTransformerServiceImpl(XMLResolvingService resolvingService){
        this.resolvingService = resolvingService;
    }

    public HTMLTransformer getTransformer() throws
            TransformerConfigurationException {
        TidyTransformerImpl transf = new TidyTransformerImpl();
        transf.resolvingService = resolvingService;
        return transf;
    }

}
