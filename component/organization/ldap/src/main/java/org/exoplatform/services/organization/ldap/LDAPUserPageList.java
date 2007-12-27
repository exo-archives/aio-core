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

import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;
import javax.naming.ldap.SortControl;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.User;

/**
 * Created by VietSpider Studio
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@yahoo.com
 * Dec 7, 2005
 */
public class LDAPUserPageList extends PageList {  
 
  private String searchBase_;
  private String filter_;
  private LDAPService ldapService_;
  private LDAPAttributeMapping ldapAttrMapping_;
  static boolean SEARCH_CONTROL = Control.NONCRITICAL;
  
  public LDAPUserPageList(LDAPAttributeMapping ldapAttrMapping,
      LDAPService ldapService,String searchBase,String filter,int pageSize) throws Exception {   
    super( pageSize);
    ldapAttrMapping_ = ldapAttrMapping;
    ldapService_= ldapService;
    searchBase_ = searchBase;
    filter_ = filter;
    try{
      int size = this.getResultSize();   
      setAvailablePage(size);
    } catch(NameNotFoundException exp) {
      setAvailablePage(0);
    } catch(OperationNotSupportedException exp) {
      setAvailablePage(0);
    }
  }
  
  protected void populateCurrentPage(int page) throws Exception  {
    List<User> users = new ArrayList<User>(); 
    PagedResultsControl prc = new PagedResultsControl(getPageSize(), Control.CRITICAL) ; 
    String keys[] = { ldapAttrMapping_.userUsernameAttr };
    SortControl sctl = new SortControl(keys, SEARCH_CONTROL);
    
    LdapContext ctx = ldapService_.getLdapContext();
    ctx.setRequestControls(new Control[] { sctl, prc });
    SearchControls constraints = new SearchControls();
    constraints.setSearchScope( SearchControls.SUBTREE_SCOPE);
    
    byte[] cookie = null;
    int counter = 0;
    
    do {
      counter++;
      NamingEnumeration<SearchResult> results = ctx.search( searchBase_, filter_, constraints);    
      if( results == null) break;     
      while (results.hasMore()){
        SearchResult result = results.next();
        if( counter == page) users.add( ldapAttrMapping_.attributesToUser(result.getAttributes()));        
      }      
      Control[] responseControls = ctx.getResponseControls();      
      for (int z = 0; z < responseControls.length; z++) {
        if (responseControls[z] instanceof PagedResultsResponseControl)                 
          cookie = ((PagedResultsResponseControl)responseControls[z]).getCookie();         
      }
      ctx.setRequestControls(new Control[]{
          new PagedResultsControl(getPageSize(), cookie, Control.CRITICAL)});     
    } while ( cookie != null);   
    this.currentListPage_ = users;
  }
  
  private int getResultSize() throws Exception {    
    PagedResultsControl prc = new PagedResultsControl(getPageSize(), Control.CRITICAL) ; 
    String keys[] = { ldapAttrMapping_.userUsernameAttr };
    SortControl sctl = new SortControl(keys, SEARCH_CONTROL);
    
    LdapContext ctx = ldapService_.getLdapContext();
    ctx.setRequestControls(new Control[] { sctl, prc });
    SearchControls constraints = new SearchControls();
    String returnedAtts[]={ ldapAttrMapping_.userUsernameAttr};
    constraints.setReturningAttributes(returnedAtts);
    constraints.setSearchScope( SearchControls.SUBTREE_SCOPE);
    
    byte[] cookie = null;
    int counter = -1;
    
    do {      
      NamingEnumeration<SearchResult> results = ctx.search( searchBase_, filter_, constraints);
      if( results == null) break;     
      while (results.hasMore()){
        counter++;
        results.next();
      }
      
      Control[] responseControls = ctx.getResponseControls();    
      if(responseControls != null){
        for (int z = 0; z < responseControls.length; z++) {
          if (responseControls[z] instanceof PagedResultsResponseControl)                 
            cookie = ((PagedResultsResponseControl)responseControls[z]).getCookie();     
        }
        ctx.setRequestControls(new Control[]{
            new PagedResultsControl(getPageSize(), cookie, Control.NONCRITICAL)});
      }
    } while ( cookie != null);   
    return  counter+1;      
  }
  
  public List getAll() throws Exception  { 
    return null;
  }  

  
}
