/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.hibernate;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.MembershipTypeHandler;
import org.exoplatform.services.organization.impl.MembershipTypeImpl;
import org.hibernate.Query;
import org.hibernate.Session;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:51:21 PM
 */
public class MembershipTypeDAOImpl implements MembershipTypeHandler {
  private static final String queryFindMembershipType =
    "from m in class org.exoplatform.services.organization.impl.MembershipTypeImpl " +
    "where m.name = ? " ;
  private static final String queryFindAllMembershipType =
    "from m in class org.exoplatform.services.organization.impl.MembershipTypeImpl"; 
  

  private HibernateService service_ ;

  public MembershipTypeDAOImpl(HibernateService service) {
    service_ = service ; 
  }

  final public MembershipType createMembershipTypeInstance() {
    return new MembershipTypeImpl() ;
  }
  
  public MembershipType createMembershipType(MembershipType mt, boolean broadcast) throws Exception {    
  	Session session = service_.openSession();    
  	Date now = new Date() ;
  	mt.setCreatedDate(now) ;
  	mt.setModifiedDate(now) ;
  	session.save(mt);
  	session.flush();
  	return mt ;
  }
  
  public MembershipType saveMembershipType(MembershipType mt, boolean broadcast) throws Exception {
  	Session  session = service_.openSession();
  	Date now = new Date() ;
  	mt.setModifiedDate(now) ;
  	session.update(mt);
  	session.flush();
  	return mt ;
  }

  public MembershipType findMembershipType(String name) throws Exception {
  	Session session = service_.openSession();
  	MembershipType m = (MembershipType) service_.findOne(session,queryFindMembershipType, name);
  	return m;
  }

  public MembershipType removeMembershipType(String name, boolean broadcast) throws Exception {    
    Session session = service_.openSession();
    MembershipTypeImpl m = (MembershipTypeImpl)session.get( MembershipTypeImpl.class, name);    
    try{
       List entries = session.createQuery("from m in class "
                +" org.exoplatform.services.organization.impl.MembershipImpl " +
                      "where m.membershipType = '" + name + "'").list();      
       for(int i=0; i<entries.size(); i++) session.delete(entries.get(i));       
    }catch( Exception exp){}
    
  	if (m != null) {  	
  		session.delete(m);      
  		session.flush();
  	}
  	return m;
  }
  
  public Collection findMembershipTypes() throws Exception {
  	Session session = service_.openSession();
  	return session.createQuery(queryFindAllMembershipType).list();
  }
}