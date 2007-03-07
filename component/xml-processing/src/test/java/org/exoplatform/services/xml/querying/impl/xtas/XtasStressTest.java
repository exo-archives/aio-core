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
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;

//import xtas.*;
//import xtas.test.*;

/**
 * Created by the Exo Development team.
 */
public class XtasStressTest extends TestCase {

   public void setUp() throws Exception 
   {
   }

   public void testLoading() throws Exception 
   {
       long t; 
       int numT = 100;
       try {

          PortalContainer manager = PortalContainer.getInstance();
          XMLQueryingService service = (XMLQueryingService) manager.
              getComponentInstanceOfType(XMLQueryingService.class);
          Query query = (Query)service.createQuery();

          Statement qc = service.createStatementHelper().select("/", "tmp/employees.xml");
          query.prepare(qc);
          query.execute();

          t = System.currentTimeMillis();
          for(int i=0;i<numT;i++)
          { 
             WellFormedUniFormTree tree = new WellFormedUniFormTree();
             tree.init(new InputSource(new ByteArrayInputStream(("<employee id=\"N"+i+"\"><firstname>Firstname"+i+"</firstname><lastname>Lastname"+i+"</lastname></employee>").getBytes())));
             qc = service.createStatementHelper().append("employees/*[last()]",tree);

             query.prepareNext(qc);
             query.execute();
          }
          System.out.println("Avg INSERT time: "+(System.currentTimeMillis() - t)/numT+" ms.");              
          t = System.currentTimeMillis();

          for(int i=0;i<numT;i++)
          { 
             WellFormedUniFormTree tree = new WellFormedUniFormTree();
             tree.init(new InputSource(new ByteArrayInputStream(("<firstname>Firstname"+(i*10)+"</firstname>").getBytes())));
             qc = service.createStatementHelper().update("employees/employee/firstname[text()='Firstname"+i+"']", "tmp/employees.xml", "tmp/employees1.xml",tree);
                                                               
             query.prepareNext(qc);
             query.execute();
          }
         System.out.println("Avg UPDATE time: "+(System.currentTimeMillis() - t)/numT+" ms.");              

         query.serialize();
         t = System.currentTimeMillis();

          for(int i=0;i<numT;i++)
          { 

             qc = service.createStatementHelper().delete("employees/employee/firstname[text()='Firstname"+i+"']");

             query.prepareNext(qc);
             query.execute();
          }
         System.out.println("Avg DELETE time: "+(System.currentTimeMillis() - t)/numT+" ms.");              
         System.out.println(""+numT+" times on 30 to "+(3*numT)+" (entity) nodes tree ");              

//          query.serialize();

       } catch ( Exception e) {

            fail( "testSelect() ERROR: "+e.toString());
       }

   }

}
