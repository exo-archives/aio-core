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
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * Created by The eXo Platform SAS
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
