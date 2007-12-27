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
 * Created by The eXo Platform SAS
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
