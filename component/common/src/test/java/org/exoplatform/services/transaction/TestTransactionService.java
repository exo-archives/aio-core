/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.transaction;

import java.lang.reflect.Field;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SARL
 * Author : HoaPham
 *          phamvuxuanhoa@yahoo.com 
 * Jan 10, 2006
 */
public class TestTransactionService extends BasicTestCase {
  private TransactionService service_ ;
  
  public TestTransactionService(String name) {
    super(name) ;
  }
  
  public void setUp() throws Exception  {
    if(service_== null) {
      PortalContainer manager = PortalContainer.getInstance() ;
      service_ = (TransactionService)manager.getComponentInstanceOfType(TransactionService.class) ;
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
