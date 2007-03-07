/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization;

/**
 * Created by The eXo Platform SARL
 * Author : Thuannd
 *         nhudinhthuan@yahoo.com
 * Feb 16, 2006
 */
public interface CryptPassword {
  
  public byte[] encrypt(String password) throws Exception;
  
  public String decrypt(byte[] password) throws Exception;
  
}
