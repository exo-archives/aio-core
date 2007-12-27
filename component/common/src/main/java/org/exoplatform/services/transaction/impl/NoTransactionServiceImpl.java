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

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.transaction.TransactionPlugin;
import org.exoplatform.services.transaction.ExoTransactionService;
/**
 * Created by The eXo Platform SAS
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
