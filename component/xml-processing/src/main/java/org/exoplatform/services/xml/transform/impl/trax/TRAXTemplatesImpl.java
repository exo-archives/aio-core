/***************************************************************************
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform.impl.trax;

import java.util.Properties;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.URIResolver;
import javax.xml.transform.Source;
import javax.xml.transform.Result;

import javax.xml.transform.Transformer;
import javax.xml.transform.Templates;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import org.exoplatform.services.xml.transform.trax.TRAXTemplates;
import org.exoplatform.services.xml.transform.trax.TRAXTransformer;
import org.exoplatform.services.xml.transform.impl.trax.TRAXTransformerImpl;
import org.exoplatform.services.xml.resolving.XMLResolvingService;


//import org.exoplatform.services.xml.transform.TransformRules;

/**
 * Created by The eXo Platform SARL        .
 *
 * Wrapper for Trax Transformer
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: TRAXTemplatesImpl.java 5799 2006-05-28 17:55:42Z geaz $
 *
 */

public class TRAXTemplatesImpl implements TRAXTemplates {
    private Templates templates;
    XMLResolvingService resolvingService;

    public TRAXTemplatesImpl(Templates templates) {
        this.templates = templates;
    }

    public Properties getOutputProperties(){
        return templates.getOutputProperties();
    }

    public TRAXTransformer newTransformer() throws TransformerConfigurationException{
        TRAXTransformerImpl transf = new TRAXTransformerImpl(this.templates);
        transf.resolvingService = resolvingService;
        return transf;
    }
}
