package org.exoplatform.services.xml.querying.impl.xtas;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


import java.io.*;
import org.exoplatform.services.xml.querying.impl.xtas.BaseStatement;
import org.exoplatform.services.xml.querying.impl.xtas.QueryType;
import org.exoplatform.services.xml.querying.impl.xtas.SimpleStatement;
import org.exoplatform.services.xml.querying.impl.xtas.XTASMLStatement;

//import org.exoplatform.services.xml.querying.impl.xtas.BaseStatement;
//import org.exoplatform.services.xml.querying.impl.xtas.XTASMLStatement;
//import org.exoplatform.services.xml.querying.impl.xtas.SimpleStatement;
//import org.exoplatform.services.xml.querying.impl.xtas.QueryType;

/**
 * Test Case for QueryContent related things
 * @version $Id: TestQueryContent.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
public class TestQueryContent extends TestCase 
{
    protected BaseStatement qc = null;

    public TestQueryContent(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(TestQueryContent.class);
    }

    public void testLocalFileContent()
    {

        String f1 = "tmp/query.xml";

        try {

            qc = new XTASMLStatement (f1);
            // Just see how it works :)
            assertNotNull( qc );
//            assertEquals( "Local file content Not equal ", util.getFileContent(f1), qc.getStatementAsString() );

        } catch ( Exception e) {
            fail( "Could not create QueryContent: "+e.toString());
        }

    }


    public void testUrlContent()
    {

    }

    public void testSimpleStatement()
    {

        BaseStatement qc = null;

        try {

//            qc = new SimpleStatement("select", "//security-identity/*");
            qc = SimpleStatement.select("//security-identity/*", "tmp/ejb-jar.xml");

            assertEquals( QueryType.SELECT, qc.getInstructions()[0].getType() );

            assertEquals( true , qc.getInstructions()[0].isAtXml() );
            assertEquals( false , qc.getInstructions()[0].isAtResource() );

//            System.out.println(qc);

            qc = null;
            String f1 = "tmp/created-xml.xml";
            qc = new SimpleStatement ("create", "tmp/created-xml.xml", "<test/>", null, null);

//            System.out.println(qc);


            assertNotNull( qc.toString() );
            assertEquals( QueryType.CREATE, qc.getInstructions()[0].getType() );
            assertEquals( qc.toString()+" isAtXml(): ", false , qc.getInstructions()[0].isAtXml() );
            assertEquals( qc.toString()+" isAtResource(): ", true , qc.getInstructions()[0].isAtResource() );


        } catch ( Exception e) {
            fail( "Could not create Query Statement: "+e.toString()+"("+qc+")");
        }



        try {

            // Must not be created! Bad query type
            qc = new SimpleStatement ("bad", "//security-identity/*");
//            assertNull( qc.toString() );

            fail( "Query Statement created ! " +qc.toString());

        } catch ( Exception e) {
//            fail( "Could not create Query Statement: "+e.toString());
        }

        try {

            qc = new SimpleStatement ("create", null, null);
//            assertNull( qc.toString() );

//            System.out.println(qc.getNewValue().toString().length());
//            System.out.println(qc.getNewValue().isEmpty());

            fail( "Query Statement created ! " +qc.toString());

        } catch ( Exception e) {
//            fail( "Could not create Query Statement: "+e.toString());
        }


    }


}
