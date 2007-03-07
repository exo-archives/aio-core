/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.ldap;

import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.xml.InitParams;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Oct 16, 2005
 */
public class DeleteObjectCommand extends BaseComponentPlugin {
  
  public List<String> objectsToDelete_ ;
  
  public DeleteObjectCommand(InitParams params) {
    objectsToDelete_ =  params.getValuesParam("objects.to.delete").getValues() ;
  }
  
  public void deleteObjects(LdapContext context)  {
    for(String dn :  objectsToDelete_ )    unbind( context, dn) ;        
  }
  
  private void unbind( LdapContext context, String dn){
    try {        
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope( SearchControls.ONELEVEL_SCOPE);      
      NamingEnumeration results = context.search(  dn, "(objectclass=*)",  constraints);
      while( results.hasMore()){
        SearchResult sr = (SearchResult) results.next();        
        unbind( context, sr.getNameInNamespace());
      }
      context.unbind( dn);
    }catch( Exception exp){

    }
  }
}
