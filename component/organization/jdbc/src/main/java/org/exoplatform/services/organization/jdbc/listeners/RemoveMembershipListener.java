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
package org.exoplatform.services.organization.jdbc.listeners;

import java.util.List;

import org.exoplatform.services.log.Log;

import org.exoplatform.services.database.DBObjectQuery;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.jdbc.MembershipDAOImpl;
import org.exoplatform.services.organization.jdbc.MembershipImpl;

/**
 * Created by The eXo Platform SAS Author : Le Bien Thuy thuy.le@exoplatform.com
 * Jun 28, 2007
 */
public class RemoveMembershipListener extends Listener<Object, Object> {
  private OrganizationService service_;

  protected static Log        log = ExoLogger.getLogger("organization:RemoveMembershipListener");

  public RemoveMembershipListener(OrganizationService service) {
    service_ = service;
  }

  @SuppressWarnings("unchecked")
  public void onEvent(Event<Object, Object> event) throws Exception {

    Object target = event.getData();
    MembershipHandler membershipHanler = service_.getMembershipHandler();
    if (target instanceof User) {
      User user = (User) target;
      log.info("Remove all Membership by User: " + user.getUserName());
      membershipHanler.removeMembershipByUser(user.getUserName(), true);
    } else if (target instanceof Group) {
      Group group = (Group) target;
      log.info("Remove all Membership by Group: " + group.getGroupName());
      List<Membership> members = (List<Membership>) membershipHanler.findMembershipsByGroup(group);
      for (Membership member : members) {
        membershipHanler.removeMembership(member.getId(), true);
      }
    } else if (target instanceof MembershipType) {
      try {
        MembershipType memberType = (MembershipType) target;
        MembershipDAOImpl mtHandler = (MembershipDAOImpl) service_.getMembershipHandler();
        DBObjectQuery<MembershipImpl> query = new DBObjectQuery<MembershipImpl>(MembershipImpl.class);
        query.addLIKE("MEMBERSHIP_TYPE", memberType.getName());
        mtHandler.removeMemberships(query, true);
      } catch (Exception e) {
        log.error("Error while removing a Membership", e);
      }
    }
  }
}
