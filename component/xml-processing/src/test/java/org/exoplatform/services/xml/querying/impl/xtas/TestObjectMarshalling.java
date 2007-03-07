package org.exoplatform.services.xml.querying.impl.xtas;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.xml.sax.InputSource;
import java.io.*;
import java.util.Iterator;
import java.util.ArrayList;
import org.exoplatform.services.xml.querying.impl.xtas.BaseStatement;
import org.exoplatform.services.xml.querying.impl.xtas.Query;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormTreeFragment;
import org.exoplatform.services.xml.querying.impl.xtas.XTASMLStatement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
/*
import org.exoplatform.services.xml.querying.impl.xtas.BaseStatement;
import org.exoplatform.services.xml.querying.impl.xtas.XTASMLStatement;
import org.exoplatform.services.xml.querying.impl.xtas.Query;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormTreeFragment;
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormConverter;
*/
/**
 * Test Case for Object Marshalling related things
 * @version $Id: TestObjectMarshalling.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
public class TestObjectMarshalling extends TestCase {

    public TestObjectMarshalling(String name)
    {
        super(name);
    }
    public static Test suite()
    {
        return new TestSuite(TestObjectMarshalling.class);
    }


    public void testSimpleBeanWMapping()
    {
        ArrayList list = new ArrayList();
/*
        try {

            BaseStatement qc = new XTASMLStatement ("tmp/test-bean-query.xml");
            Query q = new Query (qc);
            q.execute();

//            InputSource mapping = new InputSource(new FileReader("tmp/mapping1.xml"));

//            UniFormTreeFragment ro = UniFormConverter.toFragment(q.getResult());            

            UniFormTreeFragment ro = (UniFormTreeFragment)q.getResult();            

//System.out.println("RO -> "+ro.getAsCollection( "tmp/mapping1.xml" ));

            Iterator i = ro.getAsCollection( "tmp/mapping1.xml" ).iterator();

            while(i.hasNext()) {

              TmpBean tb = (TmpBean)i.next();
              list.add(tb);

           }

        } catch ( Exception e ) {
            e.printStackTrace();
            fail( " testSimpleBean() failed ! "+e.toString());

        }


        this.assertEquals("Bean(0).property not equal","Test String#1",((TmpBean)list.get(0)).getProperty());
        this.assertEquals("Bean(0).e1 not equal",new Integer(1),((TmpBean)list.get(0)).getE1());
        this.assertNull("Bean(0).f2 not null",((TmpBean)list.get(0)).getF2());

        this.assertEquals("Bean(1).property not equal",((TmpBean)list.get(1)).getProperty(),"Test String#2");
        this.assertEquals("Bean(1).e1 not equal",((TmpBean)list.get(1)).getE1(),new Integer(2));
        this.assertEquals("Bean(1).f2 not equal",((TmpBean)list.get(1)).getF2(),new Float(2.22f));

        this.assertNull("Bean(4).property not null",((TmpBean)list.get(4)).getProperty());
*/

    }



    public void testSimpleBeanWOMapping()
    {
        ArrayList list = new ArrayList();
        try {

            BaseStatement qc = new XTASMLStatement ("tmp/test-bean-query.xml");
            Query q = new Query (qc);
            q.execute();

//            UniFormTree ro = q.getResult();            


            UniFormTreeFragment ro = (UniFormTreeFragment)q.getResult();            

//System.out.println("RO -> "+ro);


            Iterator i = ro.getAsCollection( TmpBean.class ).iterator();

            while(i.hasNext()) {

              TmpBean tb = (TmpBean)i.next();
              list.add(tb);

           }

        } catch ( Exception e ) {
//            e.printStackTrace();
            fail( " testSimpleBeanWOMapping() failed ! "+e.toString());

        }

        assertEquals("Bean(0).property not equal",((TmpBean)list.get(0)).getProperty(),"Test String#1");
        assertEquals("Bean(0).e1 not equal",((TmpBean)list.get(0)).getE1(),new Integer(1));
        assertNull("Bean(0).f2 not null",((TmpBean)list.get(0)).getF2());

        assertEquals("Bean(1).property not equal",((TmpBean)list.get(1)).getProperty(),"Test String#2");
        assertEquals("Bean(1).e1 not equal",((TmpBean)list.get(1)).getE1(),new Integer(2));
        assertEquals("Bean(1).f2 not equal",((TmpBean)list.get(1)).getF2(),new Float(2.22f));

        assertNull("Bean(4).property not null",((TmpBean)list.get(4)).getProperty());

    }

    public void testSimpleBeanWOMapping1()
    {

        ArrayList list = new ArrayList();
        try {

            BaseStatement qc = new XTASMLStatement ("tmp/test-addresses.xml");
            Query q = new Query (qc);
            q.execute();

//            UniFormTree ro = q.getResult();            
            UniFormTreeFragment ro = (UniFormTreeFragment)q.getResult();            

            Addresses a = (Addresses) ro.getAsCollection( Addresses.class ).iterator().next(); 

            // Mapped to Array of Objects !!!
            Address[] a1 = a.getAddresses();
//            System.out.println(a1+" "+a.getCountry());

//            for(int i=0;i<a1.length;i++)
//              System.out.println(a1[i].getStreet1()+" "+a1[i].getZip());

           assertEquals("","Ukraine",a.getCountry());
           assertEquals("","Kiev",a1[0].getCity());
           assertEquals("","Cherkassy",a1[1].getCity());


        } catch ( Exception e ) {
//            e.printStackTrace();
            fail( " testSimpleBeanWOMapping1() failed ! "+e.toString());

        }

    }


    public void testMarshallingWOMapping()
    {
/*
        try {

             String testStr = "<test-bean><property>Test String</property><e1>1</e1><f2>0.1</f2></test-bean>";
            TmpBean tb = new TmpBean("Test String", new Integer(1), new Float(0.1f));

            WellFormedUniFormTree ro =  new WellFormedUniFormTree ();

//            UniFormTreeFragment ro = new UniFormTreeFragment();            

            ro.init( tb ); 
            assertEquals(testStr, util.packXmlString(ro.toString()));

//            System.out.println( ro );

        } catch ( Exception e ) {
//            e.printStackTrace();
            fail( " testMarshallingWOMapping() failed ! "+e.toString());

        }
  */

    }


    public void testMarshallingWMapping()
    {
/*
        try {

             String testStr = "<bean property=\"Test String\"><e1>1</e1><f2>0.1</f2></bean>";
            TmpBean tb = new TmpBean("Test String", new Integer(1), new Float(0.1f));

//            UniFormTree ro =  new UniFormTree( tb );

            WellFormedUniFormTree ro =  new WellFormedUniFormTree ();
            ro.init( "tmp/mapping1.xml", tb ); 

            assertEquals(testStr, util.packXmlString(ro.toString()));

//            System.out.println( ro );

        } catch ( Exception e ) {
//            e.printStackTrace();
            fail( " testMarshallingWMapping() failed ! "+e.toString());

        }
*/

    }


    public void testMarshallingWMapping1()
    {
/*
        try {

            EmployeeBean eb = new EmployeeBean();
            eb.setId("N1"); 
            eb.setFirstname("One"); 
            eb.setLastname("Man"); 

            WellFormedUniFormTree ro =  new WellFormedUniFormTree ();
            ro.init( "tmp/employees-map.xml", eb ); 


            EmployeeBean eb1 = (EmployeeBean) UniFormConverter.toFragment(ro).getAsCollection(EmployeeBean.class).iterator().next();

            assertEquals("",eb.getId(),eb1.getId());
           assertEquals("",eb.getFirstname(), eb1.getFirstname());
           assertEquals("",eb.getLastname(), eb1.getLastname());

//            System.out.println( UniFormConverter.toFragment(ro) );

//            assertEquals(testStr, util.packXmlString(ro.toString()));


        } catch ( Exception e ) {
//            e.printStackTrace();
            fail( " testMarshallingWMapping() failed ! "+e.toString());

        }
*/

    }


}
