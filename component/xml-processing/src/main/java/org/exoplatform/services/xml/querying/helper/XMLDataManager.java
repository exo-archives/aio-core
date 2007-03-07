package org.exoplatform.services.xml.querying.helper;

import java.io.InputStream;
import org.xml.sax.InputSource;
import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.XMLData;
import org.exoplatform.services.xml.querying.XMLFragmentData;
import org.exoplatform.services.xml.querying.XMLWellFormedData;
import org.exoplatform.services.xml.querying.object.MarshallerCreateException;
import org.exoplatform.services.xml.querying.object.ObjectMappingException;
import org.exoplatform.services.xml.querying.object.ObjectMarshalException;
import org.w3c.dom.Node;


public interface XMLDataManager {

    XMLFragmentData create(InputStream stream) throws UniFormTransformationException;

    XMLWellFormedData create(InputSource src) throws UniFormTransformationException;

    XMLWellFormedData create(Node node) throws UniFormTransformationException;

    XMLWellFormedData create(Object obj) throws UniFormTransformationException, ObjectMarshalException,
                                                MarshallerCreateException, ObjectMappingException;

    XMLWellFormedData create(Object mapping, Object obj) throws UniFormTransformationException, ObjectMarshalException,
                                                MarshallerCreateException, ObjectMappingException;

    XMLFragmentData toFragment(XMLData tree) throws UniFormTransformationException;

    XMLWellFormedData toWellFormed(XMLData tree) throws UniFormTransformationException;

}
