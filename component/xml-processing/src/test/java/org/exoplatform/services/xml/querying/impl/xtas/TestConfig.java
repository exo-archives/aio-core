package org.exoplatform.services.xml.querying.impl.xtas;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.*;
import org.exoplatform.services.xml.querying.impl.xtas.XMLConfig;
import org.xml.sax.InputSource;
//import org.exoplatform.services.xml.querying.impl.xtas.XMLConfig;

public class TestConfig extends TestCase 
{

    public TestConfig(String name)
    {
        super(name);
    }
    public static Test suite()
    {
        return new TestSuite(TestConfig.class);
    }

    public void testXmlConfig() 
    {


       try {

           XMLConfig conf = XMLConfig.getInstance();
//           System.out.println(conf.getResources().iterator().next().toString());
              

        } catch ( Exception e) {
            fail( "testXmlConfig() ERROR: "+e.toString());
        }

    }


}
