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

import org.exoplatform.services.transaction.TransactionPlugin;
import org.exoplatform.services.transaction.ExoTransactionService;
import org.picocontainer.Startable;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public class ExoTransactionServiceImpl  implements ExoTransactionService,  Startable {
  
  private TransactionPlugin transactionPlugin_ ;
  
  public ExoTransactionServiceImpl() {
  }
  
  public void setTransactionPlugin(JOTMTransactionPlugin plugin) {
    transactionPlugin_ = plugin ;
  }

  public UserTransaction getUserTransaction() throws Exception {   
    return transactionPlugin_.getUserTransaction() ;
  }

  public TransactionManager getTransactionManager() throws Exception {
    return transactionPlugin_.getTransactionManager() ;
  }

  
  public void begin() throws NotSupportedException, SystemException {
    transactionPlugin_.begin() ;
  }

  public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
    transactionPlugin_.commit();  
  }

  public int getStatus() throws SystemException {
    return transactionPlugin_.getStatus();
  }

  public Transaction getTransaction() throws SystemException {
    return transactionPlugin_.getTransaction();
  }

  public void resume(Transaction t) throws InvalidTransactionException, IllegalStateException, SystemException {
    transactionPlugin_.resume(t);    
  }

  public void rollback() throws IllegalStateException, SecurityException, SystemException {
    transactionPlugin_.rollback();    
  }

  public void setRollbackOnly() throws IllegalStateException, SystemException {
    transactionPlugin_.setRollbackOnly();
  }

  public void setTransactionTimeout(int time) throws SystemException {
    transactionPlugin_.setTransactionTimeout(time);
  }

  public Transaction suspend() throws SystemException {
    return transactionPlugin_.suspend();
  }
  
  public void start() {  }

  public void stop()  {
    try {
      transactionPlugin_.stop() ;
    } catch(Exception ex) {
      
    }
  }
}
