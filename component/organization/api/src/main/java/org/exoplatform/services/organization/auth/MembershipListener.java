/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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
package org.exoplatform.services.organization.auth;

import java.util.Collection;

import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.OrganizationService;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * 21 févr. 08  
 */
public class MembershipListener extends Listener<AuthenticationService, Identity> {

  public MembershipListener() {
    super();
  }
  
  @Override
  public void onEvent(Event<AuthenticationService, Identity> event) throws Exception {
    Identity identity = event.getData() ;
    OrganizationService orgService = event.getSource().getOrganizationService() ;
    Collection<Membership> memberships = orgService.getMembershipHandler().findMembershipsByUser(identity.getUsername());
    identity.setMemberships(memberships);    
  }

}
