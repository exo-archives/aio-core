 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.ldap;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.BaseOrganizationService;
import org.exoplatform.services.organization.hibernate.UserProfileDAOImpl;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 14, 2005
 */
public class OrganizationServiceImpl extends  BaseOrganizationService  {  
  
  public OrganizationServiceImpl(InitParams params, LDAPService ldapService, 
                                 HibernateService hservice, 
                                 CacheService cservice ) throws Exception {    
    
    LDAPAttributeMapping ldapAttrMapping = 
      (LDAPAttributeMapping)params.getObjectParam("ldap.attribute.mapping").getObject();
    
    if(ldapService.getServerType() == LDAPService.ACTIVE_DIRECTORY_SERVER){
      userDAO_ =  new ADUserDAOImpl(ldapAttrMapping, ldapService);
      ADSearchBySID adSearch = new ADSearchBySID(ldapAttrMapping, ldapService);
      groupDAO_ =  new ADGroupDAOImpl(ldapAttrMapping, ldapService, adSearch); 
      membershipDAO_ = new ADMembershipDAOImpl(ldapAttrMapping, ldapService, adSearch);
    } else{
    	ValueParam param = params.getValueParam("ldap.userDN.key");
      ldapAttrMapping.userDNKey = param.getValue() ;
      userDAO_ = new UserDAOImpl(ldapAttrMapping, ldapService) ;
      groupDAO_ =  new GroupDAOImpl(ldapAttrMapping, ldapService); 
      membershipDAO_ = new MembershipDAOImpl(ldapAttrMapping, ldapService) ;
    }    
//    userProfileHandler_ =  new UserProfileHandlerImpl(ldapAttrMapping, ldapService) ;
    userProfileDAO_ =  new UserProfileDAOImpl(hservice, cservice) ;
    membershipTypeDAO_ = new MembershipTypeDAOImpl(ldapAttrMapping, ldapService) ;
    
  }
  
  
  
}
