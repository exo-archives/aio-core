/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.impl;

import java.util.Collection;
/**
 * May 28, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UserBackupData {
	private UserImpl  user ;
	private UserProfileImpl userProfile ;
	private Collection memberships ;
	
	public UserBackupData(UserImpl u, UserProfileImpl up, Collection mbs) {
		user = u ;
		userProfile = up ;
		memberships = mbs ;
	}
	
	public UserImpl getUser() { return user ; }
	
	public UserProfileImpl getUserProfile() { return userProfile ; }
	
	public Collection getMemberships() { return memberships ;}
}