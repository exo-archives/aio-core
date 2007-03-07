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
 public class SSOAuthenticationConfig  {
	 private String authenticationName ;
	 private String serverUrl ;
	 private String serverPort ;
	 private String applicationPath ;
	 
	 public void setAuthenticationName(String name) {
		 authenticationName = name ;
	 }
	 
	 public String getAuthenticationName() {
		 return authenticationName ;
	 }
	 
	 public void setServerUrl(String url) {
		 serverUrl = url ;
	 }

	 public String getServerUrl() {
		 return serverUrl ;
	 }
	 
	 public void setServerPort(String port) {
		 serverPort = port ;
	 }
	 
	 public String getServerPort() {
		 return serverPort ;
	 }

	 public void setApplicationPath(String path) {
		 applicationPath = path ;
	 }
	 
	 public String getApplicationPath() {
		 return applicationPath ;
	 }
}
