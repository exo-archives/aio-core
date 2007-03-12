/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.transaction.impl;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * Created by The eXo Platform SARL
 * Author : Thuannd
 *          nhudinhthuan@yahoo.com
 * Apr 12, 2006
 */
public class UserTransactionWrapper implements UserTransaction {
  private  UserTransaction real_ ;
  
  public UserTransactionWrapper(UserTransaction  real) {
    real_ =  real ;
  }
  
  public void begin() throws NotSupportedException, SystemException {
    System.err.println("[UserTransactionWrapper] begin") ;
    real_.begin() ;
  }

  public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
    real_.commit() ;
  }

  public int getStatus() throws SystemException {
    return real_.getStatus() ;
  }

  public void rollback() throws IllegalStateException, SecurityException, SystemException {
    real_.rollback() ;    
  }

  public void setRollbackOnly() throws IllegalStateException, SystemException {
    real_.setRollbackOnly() ;
  }

  public void setTransactionTimeout(int t) throws SystemException {
    real_.setTransactionTimeout(t) ;
  }

}
