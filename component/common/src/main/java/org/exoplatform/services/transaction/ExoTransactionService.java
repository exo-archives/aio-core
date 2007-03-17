package org.exoplatform.services.transaction;

import javax.transaction.*;

/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

/**
 * Created by The eXo Platform SARL Author : Tuan Nguyen
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
