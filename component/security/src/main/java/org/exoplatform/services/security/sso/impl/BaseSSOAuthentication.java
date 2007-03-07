/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.security.sso.impl;

import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.services.security.sso.SSOAuthentication;
import org.exoplatform.services.security.sso.SSOAuthenticationConfig;

/**
 * Created by The eXo Platform SARL
 * Author : François MORON
 *          francois.moron@exoplatform.com
 * Dec 21, 2005
 */
abstract public class BaseSSOAuthentication extends BaseComponentPlugin implements SSOAuthentication {

	SSOAuthenticationConfig SSOAuthenticationConfig_ ;
	
	public BaseSSOAuthentication() {
	}
	
	public SSOAuthenticationConfig getSSOAuthenticationConfig() {
		return SSOAuthenticationConfig_ ;
	}

	public void setSSOAuthenticationConfig(SSOAuthenticationConfig config) {
		SSOAuthenticationConfig_ = config ;
	}
}
