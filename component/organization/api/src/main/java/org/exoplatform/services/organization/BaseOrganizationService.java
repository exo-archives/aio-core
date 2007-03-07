/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.container.component.ComponentPlugin;
import org.picocontainer.Startable;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 13, 2005
 */
abstract public class BaseOrganizationService implements OrganizationService, Startable {
  protected UserHandler userDAO_ ;
  protected UserProfileHandler userProfileDAO_ ;
  protected GroupHandler groupDAO_ ;
  protected MembershipHandler membershipDAO_ ;
  protected MembershipTypeHandler membershipTypeDAO_ ;
  protected List<OrganizationServiceInitializer> listeners_ = new ArrayList<OrganizationServiceInitializer>(3);

  public  UserHandler  getUserHandler() { return userDAO_ ; }
  
  public UserProfileHandler getUserProfileHandler() { return userProfileDAO_ ; }
  
  public GroupHandler  getGroupHandler() {  return  groupDAO_ ; }
  
  public MembershipTypeHandler getMembershipTypeHandler() { return membershipTypeDAO_ ; }
  
  public MembershipHandler getMembershipHandler() { return membershipDAO_ ; }
  
  public void start() {
    try {
      for(int i = 0 ; i < listeners_.size(); i++) {
        OrganizationServiceInitializer listener =  listeners_.get(i) ;
        listener.init(this) ;
      }
    } catch (Exception ex) {
      throw new  RuntimeException(ex) ;
    }
  }

  public  void stop() {}
  
  synchronized public void addListenerPlugin(ComponentPlugin listener) throws Exception {
    if(listener instanceof UserEventListener) {
      userDAO_.addUserEventListener((UserEventListener) listener) ;
    } else if(listener instanceof GroupEventListener) {
      groupDAO_.addGroupEventListener((GroupEventListener) listener) ;
    } else if(listener instanceof MembershipEventListener) {
      membershipDAO_.addMembershipEventListener((MembershipEventListener) listener) ;
    } else if(listener instanceof UserProfileEventListener) {
      userProfileDAO_.addUserProfileEventListener((UserProfileEventListener) listener) ;
    } else if(listener instanceof OrganizationServiceInitializer) {
      listeners_.add((OrganizationServiceInitializer)listener) ;
    } else {
      throw new RuntimeException(listener.getClass().getName() + " is an unknown listener type") ;
    }
  }
}
