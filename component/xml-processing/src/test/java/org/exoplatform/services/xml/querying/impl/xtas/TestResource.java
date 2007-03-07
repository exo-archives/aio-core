package org.exoplatform.services.xml.querying.impl.xtas;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


import java.io.*;
import org.exoplatform.services.xml.querying.impl.xtas.BaseStatement;
import org.exoplatform.services.xml.querying.impl.xtas.Query;
import org.exoplatform.services.xml.querying.impl.xtas.SimpleStatement;
import org.exoplatform.services.xml.querying.impl.xtas.XTASMLStatement;
import org.xml.sax.InputSource;
/*
import org.exoplatform.services.xml.querying.impl.xtas.BaseStatement;
import org.exoplatform.services.xml.querying.impl.xtas.SimpleStatement;
import org.exoplatform.services.xml.querying.impl.xtas.Query;
import org.exoplatform.services.xml.querying.impl.xtas.XTASMLStatement;
import org.exoplatform.services.xml.querying.impl.xtas.resource.ResourceResolver;
import org.exoplatform.services.xml.querying.impl.xtas.resource.Resource;
*/

public class TestResource extends TestCase 
{
   

    public TestResource(String name)
    {
        super(name);
    }
    public static Test suite()
    {
        return new TestSuite(TestResource.class);
    }

    public void testLocalFile() 
    {

       BaseStatement stat = null;
       String f1 = "tmp/new-res.xml";
       String updStr = "<employee><firstname>Gennady</firstname></employee>";

       try {


           System.setProperty("xtas.xsl", "tmp/xtas-debug.xsl");

           stat = new SimpleStatement ("create", f1, updStr, null, f1);
           Query q = new Query ( stat );
           q.execute();
           q.serialize();

           String str = util.getFileContent(f1);

           stat = null;
           stat = new SimpleStatement ("drop", f1, "");
           q = new Query ( stat );
           q.execute();

           assertEquals( "", util.XML_DECLARATION+updStr, str );

           System.setProperty("xtas.xsl", "");

        } catch ( Exception e) {
//            e.printStackTrace();
            fail( "testLocalFile() ERROR: "+e.toString() + " "+stat);
        }

    }

    public void testLocalFile1() 
    {

       BaseStatement stat = null;
       String f1 = "tmp/employees.xml";

       try {

           stat = SimpleStatement.select( "/" , f1, "tmp/employees-out.xml");
           Query q = new Query ( stat );
           q.execute();
//           q.serialize();
//           q.serialize();

//           System.out.println(q1.getResult());

//           assertEquals( "", true, q.getResult().isEmpty() );


        } catch ( Exception e) {
//            e.printStackTrace();
            fail( "testLocalFile() ERROR: "+e.toString() + " "+stat);
        }
    }

    public void testLocalFile2() 
    {

       BaseStatement stat = null;
       String f1 = "tmp/new-res.xml";
       String updStr = "<employee><firstname>Gennady</firstname></employee>";

       try {

//           stat =  SimpleStatement.create( f1, updStr );
           String s = "<query><create resource=\"tmp/new-res.xml\">"+
                      updStr+"</create></query>";
           stat = new XTASMLStatement ( s.getBytes() );
           Query q = new Query ( stat );
           q.execute();
           q.serialize();

           String str = util.getFileContent(f1);

           stat = null;
           stat = new SimpleStatement ("drop", f1, "");
           q = new Query ( stat );
           q.execute();

           assertEquals( "", util.XML_DECLARATION+updStr, str );



        } catch ( Exception e) {
//            e.printStackTrace();
            fail( "testLocalFile() ERROR: "+e.toString() + " "+stat);
        }

    }

/*
    public void testDescription() 
    {


       try {

//          System.out.println(ResourceResolver.getInstance().getResource("servlet://test.xml")); 

          Resource res = ResourceResolver.getInstance().getResource("test.xml");
          assertEquals( "", res.getClass().getName(), "xtas.resource.plugins.LocalFile" );

          res = ResourceResolver.getInstance().getResource("servlet://test.xml");
          assertEquals( "", res.getClass().getName(), "xtas.resource.plugins.ServletResource" );

//          System.out.println(); 

        } catch ( Exception e) {
//            e.printStackTrace();
            fail( "testDescription() ERROR: "+e.toString());
        }

    }
*/
}
