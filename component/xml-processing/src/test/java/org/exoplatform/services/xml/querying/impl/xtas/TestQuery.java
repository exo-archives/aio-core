package org.exoplatform.services.xml.querying.impl.xtas;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


import java.io.ByteArrayInputStream;
import java.io.File;

import org.exoplatform.services.xml.querying.QueryRunTimeException;
import org.exoplatform.services.xml.querying.impl.xtas.BaseStatement;
import org.exoplatform.services.xml.querying.impl.xtas.Query;
import org.exoplatform.services.xml.querying.impl.xtas.QueryType;
import org.exoplatform.services.xml.querying.impl.xtas.SimpleStatement;
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.XTASMLStatement;
import org.exoplatform.services.xml.querying.impl.xtas.resource.plugins.LocalFile;
import org.xml.sax.InputSource;
/*
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.BaseStatement;
import org.exoplatform.services.xml.querying.impl.xtas.SimpleStatement;
import org.exoplatform.services.xml.querying.impl.xtas.Query;
import org.exoplatform.services.xml.querying.impl.xtas.XTASMLStatement;
import org.exoplatform.services.xml.querying.impl.xtas.QueryType;
*/

/**
 * Test Case for Query related things
 * @version $Id: TestQuery.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
public class TestQuery extends TestCase
{

    public final ByteArrayInputStream source =
        new ByteArrayInputStream( "<root><child1>child1Value</child1><child2 attr1=\"child2Attr\"/></root>".getBytes());

    public TestQuery(String name)
    {
        super(name);
    }
    public static Test suite()
    {
        return new TestSuite(TestQuery.class);
    }


    public void testSerialize()
    {
       try {

            String f1 = "tmp/out1.xml";
            String f2 = "tmp/out2.xml";
            String _xml = "<test/>";

            BaseStatement qc = new SimpleStatement (); 
            qc.setDestinationId(f1);
            Query q = new Query ( qc );

            q.setInputStream( new ByteArrayInputStream(_xml.getBytes()) );
            q.execute();

            q.serialize();

            // And one more serialize f1->f2
            q.prepare( new SimpleStatement () );
            q.loadSource( new LocalFile( f1 ) );
            q.setDestination(  f2  );
            q.execute();
            q.serialize();

            assertEquals( "Serialize1 Not equal ", util.XML_DECLARATION+_xml, util.getFileContent(f1) );
            assertEquals( "Serialize2 Not equal ", util.XML_DECLARATION+_xml, util.getFileContent(f2) );

        } catch ( Exception e) {
//            e.printStackTrace();
            fail( "testSerialize() ERROR: "+e.toString());
        }

    }

    public void testCascadeSelect()
    {
       try {

            String f1 = "tmp/out1.xml";
            String f2 = "tmp/out2.xml";

            BaseStatement qc = new XTASMLStatement ("tmp/query1.xml");
            Query q = new Query ( qc );
            q.execute();
            q.serialize();


        } catch ( Exception e) {
//            e.printStackTrace();
            fail( "testCascadeSelect() ERROR: "+e.toString());
        }

    }


    public void testUpdate()
    {
       try {

          String updVal = "Updated value";
          BaseStatement qc = new SimpleStatement (QueryType.UPDATE, "//root/child1/node()", updVal);
          Query q = new Query ( qc );
//           WellFormedUniFormTree wfTree =  new WellFormedUniFormTree ();
//           wfTree.init( source );
//           q.setInput(wfTree);

          q.setInputStream( source );

          q.execute();
//          System.out.println(q.getResult().toString());

          // (Update without writting)

          // Get updated value...
          BaseStatement qc1 = new SimpleStatement (QueryType.SELECT, "//root/child1/node()");
//          Query q1 = q.createNext( qc1 );

//          Query q1 = new Query( qc1, q.getResult() );
//          q1.execute();
//          System.out.println(q1.getResult().toString());
//          assertEquals( "Update Not equal " , updVal, q1.getResult().toString() );

       } catch ( Exception e) {

            fail( "testUpdate() ERROR: "+e.toString());
        }
    }

    public void testDelete()
    {
       try {

          String f1 = "tmp/out1.xml";
          BaseStatement qc = new SimpleStatement (QueryType.DELETE, "//root/child1", (String)null, null, f1);

          Query q = new Query ( qc );
          q.setInputStream( source );
          q.execute();
          q.serialize();

          // Try to Get deleted value...
          BaseStatement qc1 = new SimpleStatement (QueryType.SELECT, "//root/child1", (String) null, f1, null);
          Query q1 = new Query ( qc1 );
          q1.execute();
//          System.out.println(q1.getResult().toString());

          assertEquals( "Bad delete " , "", q1.getResult().toString().trim());

       } catch ( Exception e) {

            e.printStackTrace(); 
            fail( "testDelete() ERROR: "+e.toString());
        }

    }

    public void testAppend()
    {

       try {

          String f1 = "tmp/out1.xml";
          String appVal = "<child3>NewChild</child3>";
          BaseStatement qc = new SimpleStatement (QueryType.APPEND, "//root/child1", appVal, null, f1);
          Query q = new Query ( qc );
          q.setInputStream(source);
          q.execute();
          q.serialize();

          // Get updated value...
          BaseStatement qc1 = new SimpleStatement (QueryType.SELECT, "//root/child3/node()", (String)null, f1, null);
          Query q1 = new Query ( qc1 );
          q1.execute();
          assertEquals( "Bad Append " , "NewChild", q1.getResult().toString());

       } catch ( Exception e) {

            fail( "testAppend() ERROR: "+e.toString());
        }

    }


    public void testPrepareNext()
    {

       try {

          WellFormedUniFormTree initTree = new WellFormedUniFormTree ();
          initTree.initRoot("root");
          Query q = new Query ();
          q.setInput(initTree);
          q.prepare( SimpleStatement.select("/"));
          q.execute();
          q.prepareNext( SimpleStatement.append("root/text()", null, "<branch/>"));
          q.execute();

//          assertEquals( "<root><branch/></root>", util.packXmlString(q.getResult().toString()));

       } catch ( Exception e) {

            fail( "testPrepareNext() ERROR: "+e.toString());
        }

    }

    public void testLoadFormLocalFile()
    {

       try {

           // File with external DTD...
           String f1 = "tmp/employees.xml";
           // File with internal DTD...
           String f2 = "tmp/employees1.xml";

          BaseStatement qc = new SimpleStatement (QueryType.SELECT, "/*", (String)null, f1, "tmp/employees-out.xml");
          Query q = new Query ( qc );
          q.execute();
          q.serialize();

//          assertEquals( "Bad Append " , "NewChild", q1.getResult().toString());

       } catch ( Exception e) {

            fail( "testLoadFormLocalFile() ERROR: "+e.toString());
        }

    }
/*
    public void testGetAttribute()
    {

       try {

          String f1 = "tmp/mapping1.xml";

          Statement qc = new SimpleStatement(QueryType.SELECT, "/mapping/class/map-to[@xml='bean']/@*", (String)null, f1, "tmp/mapping1-out.xml");
          Query q = new Query( qc );
          q.execute();
          q.serialize();

//          System.out.println(q.getResult());

// @todo Does not worked!
// Xalan bug 10616
// All have a copy-of inside an instruction (xsl:attribute, xsl:comment,
// xsl:processing-instruction) that is supposed to have text-only constructors.
// Xalan stringifies the whole node-set or RTF instead of taking only top-level
// text nodes and discarding the rest.


//          assertEquals( "@TODO :" , "bean", q.getResult().toString());

       } catch ( Exception e) {

            fail( "testGetAttribute() ERROR: "+e.toString());
        }

    }


    public void testSerializeComments()
    {


       try {

           // File with external DTD...
          String f1 = "tmp/employees1.xml";
          String f2 =  "tmp/employees-out.xml";
          String updStr = "<employee><firstname>Gennady</firstname></employee>";
          Statement qc = new SimpleStatement(QueryType.APPEND, "text()", updStr, f1, f2);
          Query q = new Query( qc );
          q.execute();
          q.serialize();

//          assertEquals( "Bad Append " , "NewChild", q1.getResult().toString());

       } catch ( Exception e) {

            fail( "testXMLwithDTDSerialize() ERROR: "+e.toString());
        }

    }
*/

    public void testXMLwithDTDSerialize()
    {

// It is does not work properly for now !!!
// @todo DTD/XSD supporting

       try {

           // File with external DTD...
          String f1 = "tmp/employees.xml";
          String f2 =  "tmp/employees-dtd.xml";
          String updStr = "<employee><firstname>Gennady</firstname></employee>";
          BaseStatement qc = new SimpleStatement (QueryType.APPEND, "text()", updStr, f1, f2);
          Query q = new Query ( qc );
          q.execute();
          q.serialize();

//          assertEquals( "Bad Append " , "NewChild", q1.getResult().toString());

       } catch ( Exception e) {

            fail( "testXMLwithDTDSerialize() ERROR: "+e.toString());
        }

    }

    public void testUnapropriateState()
    {
       try {

            String f1 = "tmp/out1.xml";
            String f2 = "tmp/out2.xml";
            String _xml = "<test/>";

            BaseStatement qc = new SimpleStatement (); 
            qc.setDestinationId(f1);
            Query q = new Query ( qc );
//            q.setInput( new WellFormedUniFormTree (_xml.getBytes()) );

//            WellFormedUniFormTree wfTree =  new WellFormedUniFormTree ();
//            wfTree.init( new ByteArrayInputStream(_xml.getBytes()) );
//            q.setInput(wfTree);

            q.setInputStream(new ByteArrayInputStream(_xml.getBytes()));
            q.execute();
            q.serialize();

            // And one more serialize f1->f2
            q.loadSource( new LocalFile( f1 ) );
            q.setDestination( f2 );
            q.execute();
            q.serialize();
        } catch ( QueryRunTimeException e) {
//            e.printStackTrace();
           return;

        } catch ( Exception e) {
            fail( "testUnapropriateState() ERROR: "+e.toString());
        }
        fail( "testUnapropriateState() ERROR: QueryRunTimeException(Bad state) must be trown!");

    }

    public void testMultiInstruction()
    {
       try {


            String f1 = "tmp/multiquery1.xml";

            BaseStatement qc = new XTASMLStatement (f1);

//System.out.println(qc);

//            qc.setDestinationId(f1);
            Query q = new Query ( qc );

//            q.setInputStream(new ByteArrayInputStream(_xml.getBytes()));
            q.execute();
//            q.serialize();

        } catch ( QueryRunTimeException e) {
//            e.printStackTrace();
           return;

        } catch ( Exception e) {
            fail( "testUnapropriateState() ERROR: "+e.toString());
        }
//        fail( "testUnapropriateState() ERROR: QueryRunTimeException(Bad state) must be trown!");

    }

}
