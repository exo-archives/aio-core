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
package org.exoplatform.services.organization.ldap;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.BaseOrganizationService;
import org.exoplatform.services.organization.hibernate.UserProfileDAOImpl;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Oct 14, 2005
 */
public class OrganizationServiceImpl extends BaseOrganizationService {

  public OrganizationServiceImpl(InitParams params,
                                 LDAPService ldapService,
                                 HibernateService hservice,
                                 CacheService cservice) throws Exception {

    LDAPAttributeMapping ldapAttrMapping = (LDAPAttributeMapping) params.getObjectParam("ldap.attribute.mapping")
                                                                        .getObject();

    if (ldapService.getServerType() == LDAPService.ACTIVE_DIRECTORY_SERVER) {
      userDAO_ = new ADUserDAOImpl(ldapAttrMapping, ldapService);
      ADSearchBySID adSearch = new ADSearchBySID(ldapAttrMapping, ldapService);
      groupDAO_ = new ADGroupDAOImpl(ldapAttrMapping, ldapService, adSearch);
      membershipDAO_ = new ADMembershipDAOImpl(ldapAttrMapping, ldapService, adSearch);
    } else {
      ValueParam param = params.getValueParam("ldap.userDN.key");
      ldapAttrMapping.userDNKey = param.getValue();
      userDAO_ = new UserDAOImpl(ldapAttrMapping, ldapService);
      groupDAO_ = new GroupDAOImpl(ldapAttrMapping, ldapService);
      membershipDAO_ = new MembershipDAOImpl(ldapAttrMapping, ldapService);
    }
    // userProfileHandler_ = new UserProfileHandlerImpl(ldapAttrMapping,
    // ldapService) ;
    userProfileDAO_ = new UserProfileDAOImpl(hservice, cservice);
    membershipTypeDAO_ = new MembershipTypeDAOImpl(ldapAttrMapping, ldapService);

  }

}
