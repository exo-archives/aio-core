/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.jdbc;

import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.organization.BaseOrganizationService;
import org.picocontainer.Startable;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:51:21 PM
 */
public class OrganizationServiceImpl extends  BaseOrganizationService implements  Startable {

  public OrganizationServiceImpl(CacheService cservice) throws Exception {
    userDAO_ = new UserDAOImpl() ;
    userProfileDAO_ =  new UserProfileDAOImpl() ;
    groupDAO_ =  new GroupDAOImpl() ;
    membershipDAO_ = new MembershipDAOImpl() ;
    membershipTypeDAO_ = new MembershipTypeDAOImpl() ;
  }
}