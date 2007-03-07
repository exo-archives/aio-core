/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.xml.querying.impl.xtas;

import junit.framework.TestCase;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.xml.querying.Statement;
import org.exoplatform.services.xml.querying.XMLQueryingService;
import org.exoplatform.services.xml.querying.impl.xtas.Query;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormConverter;
import org.exoplatform.services.xml.querying.impl.xtas.UniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.util.Iterator;

/**
 * Created by the Exo Development team.
 */
public class TestXtasImpl extends TestCase {

   public void setUp() throws Exception 
   {
   }

   public void testSelect() throws Exception 
   {
       try {

          PortalContainer manager = PortalContainer.getInstance();
          XMLQueryingService service = (XMLQueryingService) manager.getComponentInstanceOfType(XMLQueryingService.class);
          Query query = (Query)service.createQuery();

          Statement qc = service.createStatementHelper().select("employees/employee[firstname='Gennady']", "tmp/employees.xml", "tmp/employees-out.xml");
          query.prepare(qc);
          query.execute();
          query.serialize();

          qc = service.createStatementHelper().select("employees/employee[firstname='Gennady']/lastname/text()", "tmp/employees.xml");
          query.prepare(qc);
          query.execute();

          assertEquals( "Azarenkov", query.getResult().toString());

       } catch ( Exception e) {

            fail( "testSelect() ERROR: "+e.toString());
        }

   }


   public void testDelete() throws Exception 
   {
       try {

          PortalContainer manager = PortalContainer.getInstance();
          XMLQueryingService service = (XMLQueryingService) manager.getComponentInstanceOfType(XMLQueryingService.class);
          Query query = (Query)service.createQuery();

          Statement qc = service.createStatementHelper().delete("employees/employee[firstname='Gennady']", "tmp/employees.xml", "tmp/employees-del.xml");
          query.prepare(qc);
          query.execute();
          query.serialize();
 
          qc = service.createStatementHelper().select("count(employee[firstname='Gennady']/lastname/text())");
          query.prepareNext(qc);
          query.execute();

          assertEquals( "0", query.getResult().toString());

       } catch ( Exception e) {

            fail( "testDelete() ERROR: "+e.toString());
       }

   }

   public void testUpdate() throws Exception 
   {
       try {

          PortalContainer manager = PortalContainer.getInstance();
          XMLQueryingService service = (XMLQueryingService) manager.
              getComponentInstanceOfType(XMLQueryingService.class);
          Query query = (Query)service.createQuery();

          WellFormedUniFormTree tree = new WellFormedUniFormTree();
          tree.init(new InputSource(new ByteArrayInputStream("<lastname>Azarenkov1</lastname>".getBytes())));
          Statement qc = service.createStatementHelper().update("employees/employee[firstname='Gennady']/lastname", "tmp/employees.xml", tree);
          query.prepare(qc);
          query.execute();
 
          qc = service.createStatementHelper().select("employees/employee[firstname='Gennady']/lastname/text()");
          query.prepareNext(qc);
          query.execute();

          assertEquals( "Azarenkov1", query.getResult().toString());


       } catch ( Exception e) {

            fail( "testUpdate() ERROR: "+e.toString());
       }

   }

   public void testAppend() throws Exception 
   {
       try {

          PortalContainer manager = PortalContainer.getInstance();
          XMLQueryingService service = (XMLQueryingService) manager.
              getComponentInstanceOfType(XMLQueryingService.class);
          Query query = (Query)service.createQuery();

          WellFormedUniFormTree tree = new WellFormedUniFormTree();
          tree.init(new InputSource(new ByteArrayInputStream("<employee id=\"N4543\"><firstname>Bill</firstname><lastname>Gates</lastname></employee>".getBytes())));
          Statement qc = service.createStatementHelper().append("employees/*[last()]", "tmp/employees.xml", tree);
          query.prepare(qc);
          query.execute();

          qc = service.createStatementHelper().select("employees/employee[lastname='Gates']/firstname/text()");
          query.prepareNext(qc);
          query.execute();

          assertEquals( "Bill", query.getResult().toString());


       } catch ( Exception e) {

            fail( "testAppend() ERROR: "+e.toString());
       }

   }

   public void testCreateDrop() 
    {

       Statement stat = null;
       String f1 = "tmp/new-res.xml";
       String updStr = "<employee><firstname>Gennady</firstname></employee>";

       try {

          PortalContainer manager = PortalContainer.getInstance();
          XMLQueryingService service = (XMLQueryingService) manager.
              getComponentInstanceOfType(XMLQueryingService.class);
          Query query = (Query)service.createQuery();

          WellFormedUniFormTree tree = new WellFormedUniFormTree();
          tree.init(new InputSource(new ByteArrayInputStream(updStr.getBytes())));

          stat = service.createStatementHelper().create(f1, tree);
          query.prepare(stat);
          query.execute();
          query.serialize();

          String str = util.getFileContent(f1);

          query.prepare(service.createStatementHelper().drop(f1));
          query.execute();

          assertEquals( "", util.XML_DECLARATION+updStr, str );

        } catch ( Exception e) {
//            e.printStackTrace();
            fail( "testCreateDrop() ERROR: "+e.toString() + " "+stat);
        }

    }

   public void testObjectMarshalling() 
    {

       try {

            PortalContainer manager = PortalContainer.getInstance();
            XMLQueryingService service = (XMLQueryingService) manager.
                getComponentInstanceOfType(XMLQueryingService.class);
            Query query = (Query)service.createQuery();

            WellFormedUniFormTree tree = new WellFormedUniFormTree();
            tree.init(new City("UK","London","55555"));

            query.prepare(service.createStatementHelper().select("/",null,"tmp/addresses-out.xml")); 
            query.setInput(tree); 
            query.execute();
            query.serialize();

            query.prepare(service.createStatementHelper().select("/","tmp/addresses-out.xml")); 
            query.execute();
            Iterator iter = UniFormConverter.toFragment((UniFormTree)query.getResult()).
                            getAsCollection(City.class).iterator();
            City city = (City)iter.next();

            assertEquals( "London", city.getName() );
            assertEquals( "UK", city.getCountry() );


        } catch ( Exception e) {
//            e.printStackTrace();
            fail( "testObjectMarshalling() ERROR: "+e.toString());
        }

    }

/*
   public void testJdbcResource() 
    {

       try {

          PortalContainer manager = PortalContainer.getInstance();
          XMLQueryingService service = (XMLQueryingService) manager.getService(XMLQueryingServiceImpl.class);
          Query query = (Query)service.createQuery();

          // Load the HSQL Database Engine JDBC driver
          String driverClass = "org.hsqldb.jdbcDriver";

          connURL = "jdbc:hsqldb:./tmp/test?user=sa;rootTable=exo_user";

          query.prepare(SimpleStatementHelper..select("tables/exo_user", connURL);
          query.execute();

        } catch ( Exception e) {
//            e.printStackTrace();
            fail( "testObjectMarshalling() ERROR: "+e.toString());
        }


    }
*/

}
