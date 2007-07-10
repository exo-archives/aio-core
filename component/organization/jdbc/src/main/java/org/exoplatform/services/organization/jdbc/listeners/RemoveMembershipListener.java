/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc.listeners;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.services.database.DBObjectQuery;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.MembershipTypeHandler;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.jdbc.MembershipDAOImpl;
import org.exoplatform.services.organization.jdbc.MembershipImpl;
/**
 * Created by The eXo Platform SARL
 * Author : Le Bien Thuy
 *          thuy.le@exoplatform.com
 * Jun 28, 2007  
 */
public class RemoveMembershipListener extends Listener<Object, Object>{
  private OrganizationService service_ ;
  
  public RemoveMembershipListener(OrganizationService service) {
    service_ = service ;
  }
  
  @SuppressWarnings("unchecked")
  public void onEvent(Event<Object, Object> event) throws Exception {
    
    Object target = event.getData();
    MembershipHandler membershipHanler = service_.getMembershipHandler();
    if(target instanceof User){
      User user = (User) target;
//      System.out.println("\n\nRemove all Membership by User: " + user.getUserName() + "\n\n");
      membershipHanler.removeMembershipByUser(user.getUserName(), true);
    } else if (target instanceof Group){
      Group group = (Group) target;
//      System.out.println("\n\nRemove all Membership by Group: " + group.getGroupName() + "\n\n");
      List<Membership> members = (List<Membership>) membershipHanler.findMembershipsByGroup( group);
      for(Membership member: members){
        membershipHanler.removeMembership(member.getId(), true);
      }
    } else if( target instanceof MembershipType){
      try{
      MembershipType memberType = (MembershipType) target;
      MembershipDAOImpl mtHandler = (MembershipDAOImpl) service_.getMembershipHandler();
      DBObjectQuery<MembershipImpl> query = new DBObjectQuery<MembershipImpl>(MembershipImpl.class);
      query.addLIKE("MEMBERSHIP_TYPE", memberType.getName());
      mtHandler.removeMemberships(query, true);
      }catch(Exception e){ e.printStackTrace(); }
    }
  }
}
