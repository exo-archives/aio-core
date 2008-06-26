/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.transaction;

import java.lang.reflect.Field;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SAS
 * Author : HoaPham
 *          phamvuxuanhoa@yahoo.com 
 * Jan 10, 2006
 */
public class TestTransactionService extends BasicTestCase {
  private ExoTransactionService service_ ;
  
  public TestTransactionService(String name) {
    super(name) ;
  }
  
  public void setUp() throws Exception  {
    if(service_== null) {
      PortalContainer manager = PortalContainer.getInstance() ;
      service_ = (ExoTransactionService)manager.getComponentInstanceOfType(ExoTransactionService.class) ;
    }        
  }
  
  public void tearDown() throws Exception {
    
  }
  
  public void testNamingService() throws Exception { 
    System.err.println("User Transaction: " + service_.getUserTransaction() ) ;
    System.err.println("Transaction Manager: " + service_.getTransactionManager()) ;
    
    
    UserTransaction utc = service_.getUserTransaction() ;
    System.out.println();
    //a simple transaction
    try {
      System.out.println("a simple transaction which is committed:");
      System.out.println("\t- initial status : "+getStatusName(utc.getStatus()));
      utc.begin();
      System.out.println("\t- after begin status : "+getStatusName(utc.getStatus()));
      utc.commit();
      System.out.println("\t- after commit status : "+getStatusName(utc.getStatus()));            
    } catch (Exception e) {
      System.out.println("Exception of type :"+e.getClass().getName()+" has been thrown");
      System.out.println("Exception message :"+e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
  
  private String getStatusName(int status)  {
    String statusName = null;
    try {
      Field[] flds = Status.class.getDeclaredFields();
      for (int i=0; i<flds.length; i++) {
        if (flds[i].getInt(null) == status)
          statusName = flds[i].getName();
      }
    } catch (Exception e) {
      statusName = "invalid status value!";
    }
    return statusName;
  }
}
