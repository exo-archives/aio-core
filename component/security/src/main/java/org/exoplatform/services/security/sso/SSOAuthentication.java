/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.security.sso;

/**
 * Created by The eXo Platform SARL
 * Author : François MORON
 *          francois.moron@exoplatform.com
 * Dec 21, 2005
 */
 public interface SSOAuthentication  {
	 
	 abstract public String getProxyTicket(String userName, String urlOfTargetService) throws Exception ;
	 
}
