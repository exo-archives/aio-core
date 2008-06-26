package org.exoplatform.services.transaction;

import javax.transaction.*;

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

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Apr 4, 2006
 */
public interface ExoTransactionService {
  public UserTransaction getUserTransaction() throws Exception;

  public TransactionManager getTransactionManager() throws Exception;

  public void begin() throws NotSupportedException, SystemException;

  public void commit() throws RollbackException, HeuristicMixedException,
                      HeuristicRollbackException, SecurityException, IllegalStateException,
                      SystemException;

  public int getStatus() throws SystemException;

  public Transaction getTransaction() throws SystemException;

  public void resume(Transaction t) throws InvalidTransactionException, IllegalStateException,
                                   SystemException;

  public void rollback() throws IllegalStateException, SecurityException, SystemException;

  public void setRollbackOnly() throws IllegalStateException, SystemException;

  public void setTransactionTimeout(int time) throws SystemException;

  public Transaction suspend() throws SystemException;
}
