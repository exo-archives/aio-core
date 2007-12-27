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
package org.exoplatform.services.security.sso.impl;

import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.services.security.sso.SSOAuthentication;
import org.exoplatform.services.security.sso.SSOAuthenticationConfig;

/**
 * Created by The eXo Platform SAS
 * Author : Fran�ois MORON
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
