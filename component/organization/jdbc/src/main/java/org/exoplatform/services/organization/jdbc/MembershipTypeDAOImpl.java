/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import java.util.Collection;
import java.util.Date;

import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBObjectQuery;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.database.StandardSQLDAO;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.MembershipTypeHandler;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Apr 7, 2007  
 */
public class MembershipTypeDAOImpl extends StandardSQLDAO<MembershipTypeImpl> implements MembershipTypeHandler {
  
  public MembershipTypeDAOImpl(ListenerService lService, ExoDatasource datasource, DBObjectMapper<MembershipTypeImpl> mapper) {
    super(lService, datasource, mapper, MembershipTypeImpl.class);
  }

  public MembershipType createMembershipTypeInstance() { return new MembershipTypeImpl(); }
  
  public MembershipType createMembershipType(MembershipType mt, boolean broadcast) throws Exception {
    Date now = new Date() ;
    mt.setCreatedDate(now) ;
    mt.setModifiedDate(now) ;
    super.save((MembershipTypeImpl)mt);
    return mt ;
  }


  public MembershipType findMembershipType(String name) throws Exception {
    DBObjectQuery<UserImpl> query = new DBObjectQuery<UserImpl>(UserImpl.class);
    query.addLIKE("name", name);
    return loadUnique(query.toQuery());
  }

  public Collection findMembershipTypes() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public MembershipType removeMembershipType(String name, boolean broadcast) throws Exception {
    DBObjectQuery<UserImpl> query = new DBObjectQuery<UserImpl>(UserImpl.class);
    query.addLIKE("name", name);
    MembershipTypeImpl mt = loadUnique(query.toQuery());
    if(mt == null) return null;
    super.remove(mt);
    return mt;
  }

  public MembershipType saveMembershipType(MembershipType mt, boolean broadcast) throws Exception {
    Date now = new Date() ;
    mt.setModifiedDate(now) ;
    super.update((MembershipTypeImpl)mt);
    return mt ;
  }

}
