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
 * Created by The eXo Platform SAS
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
    service.createSubcontext("jotm", true) ;
    context.rebind("jotm/UserTransaction", getUserTransaction()) ;
    context.rebind("jotm/TransactionManager", getTransactionManager()) ;    
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
