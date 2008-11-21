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
package org.exoplatform.services.organization.jdbc;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBObjectQuery;
import org.exoplatform.services.database.DBPageList;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.database.StandardSQLDAO;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.MembershipTypeHandler;

/**
 * Created by The eXo Platform SAS Author : Nhu Dinh Thuan
 * nhudinhthuan@exoplatform.com Apr 7, 2007
 */
public class MembershipTypeDAOImpl extends StandardSQLDAO<MembershipTypeImpl> implements
    MembershipTypeHandler {

  protected ListenerService listenerService_;

  public MembershipTypeDAOImpl(ListenerService lService,
                               ExoDatasource datasource,
                               DBObjectMapper<MembershipTypeImpl> mapper) {
    super(datasource, mapper, MembershipTypeImpl.class);
    listenerService_ = lService;
    ;
  }

  public MembershipType createMembershipTypeInstance() {
    return new MembershipTypeImpl();
  }

  @SuppressWarnings("unused")
  public MembershipType createMembershipType(MembershipType mt, boolean broadcast) throws Exception {
    // System.out.println("==========CREATE MT " + mt.getName());
    Date now = Calendar.getInstance().getTime();
    mt.setCreatedDate(now);
    mt.setModifiedDate(now);
    super.save((MembershipTypeImpl) mt);
    return mt;
  }

  public MembershipType findMembershipType(String name) throws Exception {
    DBObjectQuery<MembershipTypeImpl> query = new DBObjectQuery<MembershipTypeImpl>(MembershipTypeImpl.class);
    query.addLIKE("MT_NAME", name);
    MembershipType mt = loadUnique(query.toQuery());
    ;
    // System.out.println("===========FIND MT BY NAME" + name + " - " +
    // (mt!=null));
    return mt;
  }

  public Collection findMembershipTypes() throws Exception {
    DBObjectQuery<MembershipTypeImpl> query = new DBObjectQuery<MembershipTypeImpl>(MembershipTypeImpl.class);
    DBPageList<MembershipTypeImpl> pageList = new DBPageList<MembershipTypeImpl>(20, this, query);
    // System.out.println("==========FIND ALL OF MT Size = " +
    // pageList.getAvailable());
    return pageList.getAll();
  }

  @SuppressWarnings("unused")
  public MembershipType removeMembershipType(String name, boolean broadcast) throws Exception {
    DBObjectQuery<MembershipTypeImpl> query = new DBObjectQuery<MembershipTypeImpl>(MembershipTypeImpl.class);
    query.addLIKE("MT_NAME", name);
    MembershipTypeImpl mt = loadUnique(query.toQuery());
    if (mt == null)
      return null;
    if (broadcast)
      listenerService_.broadcast(MembershipTypeHandler.PRE_DELETE_MEMBERSHIP_TYPE_EVENT, this, mt);
    super.remove(mt);
    if (broadcast)
      listenerService_.broadcast(MembershipTypeHandler.POST_DELETE_MEMBERSHIP_TYPE_EVENT, this, mt);
    return mt;
  }

  @SuppressWarnings("unused")
  public MembershipType saveMembershipType(MembershipType mt, boolean broadcast) throws Exception {
    mt.setModifiedDate(Calendar.getInstance().getTime());
    super.update((MembershipTypeImpl) mt);
    return mt;
  }

}
