package org.exoplatform.services.xml.querying.impl.xtas.xml;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.InputStream;
import java.io.IOException;

/**
 * @version $Id: Utils.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
public class Utils {
    public static Document createDocument() throws ParserConfigurationException
    {
        DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
        dfactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
        return docBuilder.newDocument();
    }

    public static Document createDocument(InputStream is) throws IOException, SAXException, ParserConfigurationException
    {
        DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
        dfactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
        return docBuilder.parse( is );


    }

}
