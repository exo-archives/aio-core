package org.exoplatform.services.xml.querying.impl.xtas.helper;

import java.io.InputStream;
import org.xml.sax.InputSource;
import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.XMLData;
import org.exoplatform.services.xml.querying.XMLFragmentData;
import org.exoplatform.services.xml.querying.XMLWellFormedData;
import org.exoplatform.services.xml.querying.helper.XMLDataManager;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormConverter;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormTreeFragment;
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.exoplatform.services.xml.querying.object.MarshallerCreateException;
import org.exoplatform.services.xml.querying.object.ObjectMappingException;
import org.exoplatform.services.xml.querying.object.ObjectMarshalException;
import org.w3c.dom.Node;



public class XMLDataManagerFacade implements XMLDataManager {

    public XMLFragmentData create(InputStream stream) throws UniFormTransformationException {
        UniFormTreeFragment tree = new UniFormTreeFragment();
        tree.init(stream);
        return tree;
    }

    public XMLWellFormedData create(InputSource src) throws UniFormTransformationException {
        WellFormedUniFormTree tree = new WellFormedUniFormTree();
        tree.init(src);
        return tree;
    }

    public XMLWellFormedData create(Node node) throws UniFormTransformationException {
        WellFormedUniFormTree tree = new WellFormedUniFormTree();
        tree.init(node);
        return tree;
    }

    public XMLWellFormedData create(Object obj) throws UniFormTransformationException, ObjectMarshalException,
                                                MarshallerCreateException, ObjectMappingException {
        WellFormedUniFormTree tree = new WellFormedUniFormTree();
        tree.init(obj);
        return tree;
    }

    public XMLWellFormedData create(Object mapping, Object obj) throws UniFormTransformationException, ObjectMarshalException,
                                                MarshallerCreateException, ObjectMappingException {
        WellFormedUniFormTree tree = new WellFormedUniFormTree();
        tree.init(mapping, obj);
        return tree;
    }

    public XMLFragmentData toFragment(XMLData tree) throws UniFormTransformationException {
        return UniFormConverter.toFragment((UniFormTree) tree);
    }

    public XMLWellFormedData toWellFormed(XMLData tree) throws UniFormTransformationException {
        return UniFormConverter.toWellForm((UniFormTree) tree);
    }

}
