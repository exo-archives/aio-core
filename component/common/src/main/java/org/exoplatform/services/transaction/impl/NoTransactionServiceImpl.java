/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.transaction.impl;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.transaction.TransactionPlugin;
import org.exoplatform.services.transaction.ExoTransactionService;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public class NoTransactionServiceImpl implements ExoTransactionService { 
  
  Log log_ ;
  
  public NoTransactionServiceImpl(LogService logService) {
    log_ = logService.getLog(getClass());
  }
  
  public void setTransactionPlugin(TransactionPlugin plugin) {
    throw new RuntimeException("This method is not supported") ;
  }

  public UserTransaction getUserTransaction() throws Exception {
    throw new RuntimeException("This method is not supported") ;
  }

  public TransactionManager getTransactionManager() throws Exception {
    throw new RuntimeException("This method is not supported") ;
  }

  
  public void begin() throws NotSupportedException, SystemException {
    log_.warn("warn:  Transaction is not supported");
  }

  public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
    log_.warn("warn:  Transaction is not supported");
  }

  public int getStatus() throws SystemException {
    return 0;
  }

  public Transaction getTransaction() throws SystemException {
    throw new RuntimeException("This method is not supported") ;
  }

  public void resume(Transaction t) throws InvalidTransactionException, IllegalStateException, SystemException {
    throw new RuntimeException("This method is not supported") ;
  }

  public void rollback() throws IllegalStateException, SecurityException, SystemException {
    log_.warn("warn:  Transaction is not supported");
  }

  public void setRollbackOnly() throws IllegalStateException, SystemException {
    log_.warn("warn:  Transaction is not supported");
  }

  public void setTransactionTimeout(int time) throws SystemException {
    log_.warn("warn:  Transaction is not supported");
  }

  public Transaction suspend() throws SystemException {
    throw new RuntimeException("This method is not supported") ;
  }
}
