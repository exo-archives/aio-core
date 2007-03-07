package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


import org.xml.sax.InputSource;
import java.io.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.xml.sax.InputSource;

import org.exoplatform.services.xml.querying.impl.xtas.UniFormConverter;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormTreeFragment;
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.xml.Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
/*
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormTreeFragment;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormConverter;
*/


public class TestUniFormTree extends TestCase {

    public final String str = "<root><child1>child1Value</child1><child2 attr1=\"child2Attr\"/></root>";
    public final ByteArrayInputStream source =
        new ByteArrayInputStream( str.getBytes());

    public TestUniFormTree(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(TestUniFormTree.class);
    }

    public void testNewDefault()
    {
       try {

           WellFormedUniFormTree elem = new WellFormedUniFormTree ();
           assertEquals( "", "", elem.toString() );

        } catch ( Exception e ) {
            e.printStackTrace();
            fail( " testNewFromStream() failed ! "+e.toString());

        }
    }


    public void testNewFromStream()
    {
       try {

           UniFormTreeFragment elem = new UniFormTreeFragment ();
           elem.init( source );

           assertEquals( "", util.packXmlString(str), util.packXmlString(elem.toString()) );

        } catch ( Exception e ) {
            e.printStackTrace();
            fail( " testNewFromStream() failed ! "+e.toString());

        }
    }

    public void testToWellForm()
    {
       try {

           UniFormTreeFragment elem = new UniFormTreeFragment ();
           elem.init( source );
           java.util.Properties attrs = new java.util.Properties();
           attrs.setProperty("xtas:xpath", "tables/exo_user");
           attrs.setProperty("xtas:resource", "?rootTable=EXO_USER");

           WellFormedUniFormTree t = UniFormConverter.toWellForm(elem, "users", "xmlns:xtas=\"http://xtas.sourceforge.net\"", attrs);
//System.out.println(t);
//           assertEquals( "", util.packXmlString(str), util.packXmlString(elem.toString()) );

        } catch ( Exception e ) {
            e.printStackTrace();
            fail( " testNewFromStream() failed ! "+e.toString());

        }
    }

    public void testAsNodeList()
    {
       try {

          String str1 = "<child1>child1Value</child1><child2 attr1=\"child2Attr\"/>";

           UniFormTreeFragment elem = new UniFormTreeFragment ();
           elem.init( new ByteArrayInputStream( str1.getBytes()) );
           NodeList nl = elem.getAsNodeList();
           assertTrue("Num of children !=2", nl.getLength()==2 );

        } catch ( Exception e ) {
            e.printStackTrace();
            fail( " testNewFromStream() failed ! "+e.toString());

        }
    }

    public void testNewFromDocument()
    {
       try {

          DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
          dfactory.setNamespaceAware(true);
          DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
          Document doc =  docBuilder.parse( source );
           
           WellFormedUniFormTree elem = new WellFormedUniFormTree ();
           elem.init( doc );
           elem.getAsDOM();

//           System.out.println( elem.getAsDOM().getDocumentElement().getFirstChild() );

           assertEquals( "", util.packXmlString(str), util.packXmlString(elem.toString()) );

        } catch ( Exception e ) {
            e.printStackTrace();
            fail( " testNewFromDocument() failed ! "+e.toString());

        }
    }


    public void testNewFromLocalFile()
    {
       try {

           // File with external DTD...
           String f1 = "tmp/employees.xml";
           // File with internal DTD...
           String f2 = "tmp/employees1.xml";

//           UniFormTree elem = new UniFormTreeFragment();

           // Well Formed !!!!!!!
           WellFormedUniFormTree elem = new WellFormedUniFormTree ();
           elem.init(  new InputSource(f1) );
//           elem.init( new FileInputStream(f1) );

//           UniFormTree elem1 = new WellFormedUniFormTree( new FileInputStream(f2) );


//           System.out.println(elem.toString());
//           assertEquals( "", util.packXmlString(str), util.packXmlString(elem.getAsString()) );

        } catch ( Exception e ) {
            fail( " testNewFromLocalFile() failed ! "+e.toString());

        }
    }


    // Mutable Tree is DEPRECATED!!
    public void testSimpleAdd()
    {
       try {

           WellFormedUniFormTree tree = new WellFormedUniFormTree ();

           Document doc = Utils.createDocument(); 
           Element root = doc.createElement("root");
           doc.appendChild( root );
         
           doc.getDocumentElement().setAttribute("attr", "val");
           Element item =  doc.createElement("node1");
           doc.getDocumentElement().appendChild( item );
           item.appendChild( doc.createTextNode("val1") );
           Element item1 =  doc.createElement("node2");
           item.appendChild( item1 );
           item1.appendChild( doc.createTextNode("val2") );  

           tree.init(doc);

           String testStr = "<root attr=\"val\"><node1>val1<node2>val2</node2></node1></root>";

           assertEquals( "", util.packXmlString(testStr), util.packXmlString(tree.toString()) );

        } catch ( Exception e ) {
            e.printStackTrace();
            fail( " testSimpleAdd() failed ! "+e.toString());

        }
    }


}
