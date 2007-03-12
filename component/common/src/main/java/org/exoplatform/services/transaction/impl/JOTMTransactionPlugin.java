/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.transaction.impl;

import javax.naming.Context;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.services.naming.NamingService;
import org.exoplatform.services.transaction.TransactionPlugin;
import org.objectweb.jotm.Jotm;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Apr 4, 2006
 */
public class JOTMTransactionPlugin  extends BaseComponentPlugin implements TransactionPlugin {
  
  private Jotm jotm_ ;
  
  private NamingService service_;
  
  public JOTMTransactionPlugin(NamingService service) throws Exception  {
    jotm_ = new  Jotm(true, false) ;
    service_ = service;
    Context context = service.getContext() ;
    service.createSubcontext("java:comp/jotm", true) ;
    context.rebind("java:comp/jotm/UserTransaction", getUserTransaction()) ;
    context.rebind("java:comp/jotm/TransactionManager", getTransactionManager()) ;    
  }
  
  public UserTransaction getUserTransaction() throws Exception {
    return new UserTransactionWrapper(jotm_.getUserTransaction()) ;
  }

  public TransactionManager getTransactionManager() throws Exception {
    return  jotm_.getTransactionManager() ;
  }

  public void start() throws Exception { }

  public void stop() throws Exception {
    Context context = service_.getContext() ;
    context.unbind("java:comp/jotm/UserTransaction");
    context.unbind("java:comp/jotm/TransactionManager");
    jotm_.stop() ;
  }

  public void begin() throws NotSupportedException, SystemException {
  }
  
  public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
    jotm_.getTransactionManager().commit() ;   
  }
  
  public int getStatus() throws SystemException {
    return jotm_.getTransactionManager().getStatus();
  }
  
  public Transaction getTransaction() throws SystemException {
    return jotm_.getTransactionManager().getTransaction();
  }
  
  public void resume(Transaction t) throws InvalidTransactionException, IllegalStateException, SystemException {
    jotm_.getTransactionManager().resume(t) ;    
  }
  
  public void rollback() throws IllegalStateException, SecurityException, SystemException {
    jotm_.getTransactionManager().rollback() ;
  }
  
  public void setRollbackOnly() throws IllegalStateException, SystemException {
    jotm_.getTransactionManager().setRollbackOnly() ;
  }
  
  public void setTransactionTimeout(int time) throws SystemException {
    jotm_.getTransactionManager().setTransactionTimeout(time) ; 
  }
  
  public Transaction suspend() throws SystemException {
    return jotm_.getTransactionManager().suspend() ;
  }
}
