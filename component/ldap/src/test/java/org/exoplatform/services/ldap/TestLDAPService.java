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
package org.exoplatform.services.ldap;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapContext;

import junit.framework.TestCase;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.net.NetService;
/**
 * Created by The eXo Platform SAS
 * Author : Nhu Thuan
 *          thuannd@exoplatform.com
 * Oct 3, 2005
 */
public class TestLDAPService extends TestCase {
  final  static public String ROOT_DN = "cn=Manager,dc=exoplatform,dc=org" ;
  final  static public String ROOT_PASSWORD = "secret" ;
  final  static public String DEVELOPER_UNIT_DN = "ou=developer,o=company,c=vietnam,dc=exoplatform,dc=org" ;
  final  static public String EXO_DEVELOPER_DN = "cn=exo, " + DEVELOPER_UNIT_DN ;
//  static private String       LDAP_HOST = "192.168.0.10" ;
  static private String       LDAP_HOST = "localhost" ;

  static private int          LDAP_PORT = 389 ;
  
  private LDAPService service_;
  private NetService nservice_ ;   
  
  private boolean test = true ;
  
  public void setUp() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance();
    nservice_ = (NetService)pcontainer.getComponentInstanceOfType(NetService.class) ;
    if(nservice_.ping(LDAP_HOST,LDAP_PORT)<0) {      
      test = false ;
      return ;
    }
    service_ = (LDAPService) pcontainer.getComponentInstanceOfType(LDAPService.class); 
        
  }

  public void testCreate() throws Exception {    
    if(!test) {
      System.out.println("===LDAP Server is not started on host:"+LDAP_HOST + " port:"+LDAP_PORT) ;
      return ; 
    }
    LdapContext ctx = service_.getLdapContext() ;
//    // Create attributes to be associated with the new context
//    System.out.println("##############################CREATE##############################");
//    // Create the context
////    String dn = "dc=exoplatform,dc=org";
//    String dn = "";
//    CompositeName remaining = new CompositeName("dn=net");
//    ctx.createSubcontext(remaining);
//    NamingEnumeration ne = ctx.list("dc=exoplatform,dc=org");
//    while(ne.hasMore())
//      System.out.println(">>>>>>"+ne.next());
 
    String BASE = "dc=exoplatform,dc=org";
    String PSW = "secret";
    

    Attributes attrs = new BasicAttributes(true); // case-ignore
    Attribute objclass = new BasicAttribute("objectClass");
    objclass.add("top");
    objclass.add("organizationalUnit");
    attrs.put(objclass);
    //attrs.put(new BasicAttribute("ou", "clients"));          
    ctx.createSubcontext("ou=clients,"+BASE, attrs);
    
    attrs = new BasicAttributes(true); // case-ignore
    objclass = new BasicAttribute("objectClass");
    objclass.add("top");
    objclass.add("organization");
    attrs.put(objclass);
    ctx.createSubcontext("o=ibm.com,ou=clients,"+BASE, attrs);
    
    attrs = new BasicAttributes(true); // case-ignore
    objclass = new BasicAttribute("objectClass");
    objclass.add("top");
    objclass.add("organizationalUnit");
    attrs.put(objclass);
    ctx.createSubcontext("ou=users,o=ibm.com,ou=clients,"+BASE, attrs);


    attrs = new BasicAttributes(true); // case-ignore
    objclass.add("top");
    objclass.add("person");
    objclass.add("organizationalPerson");
    objclass.add("inetOrgPerson");
    attrs.put(objclass);
    attrs.put("cn", "g");
    attrs.put("sn", "a");
    ctx.createSubcontext("uid=gena,ou=users,o=ibm.com,ou=clients,"+BASE, attrs);
    //System.out.println(ctx.getAttributes(dn));

  }
  
/*
  
  public void testT() throws Exception {

    String BASE = "dc=exoplatform,dc=org";
    String PSW = "secret";
    
      // Set up the environment for creating the initial context
      Hashtable env = new Hashtable(11);
      env.put(Context.INITIAL_CONTEXT_FACTORY, 
          "com.sun.jndi.ldap.LdapCtxFactory");
      env.put(Context.PROVIDER_URL, "ldap://localhost:389");
//      env.put(Context.PROVIDER_URL, "ldap://exoua.dnsalias.net:389");
      
      env.put(Context.SECURITY_AUTHENTICATION, "simple");
      env.put(Context.SECURITY_PRINCIPAL, "cn=Manager,"+BASE);
      env.put(Context.SECURITY_CREDENTIALS,  PSW);



//      try {
          // Create the initial context
          DirContext ctx = new InitialDirContext(env);

          // Create attributes to be associated with the new context
          Attributes attrs = new BasicAttributes(true); // case-ignore
          Attribute objclass = new BasicAttribute("objectClass");
          objclass.add("top");
          objclass.add("organizationalUnit");
          attrs.put(objclass);

          // Create the context
          Context result = ctx.createSubcontext("ou=Fruits,"+BASE, attrs);

          // Check that it was created by listing its parent
          NamingEnumeration list = ctx.list(BASE);

          // Go through each item in list
          while (list.hasMore()) {
        NameClassPair nc = (NameClassPair)list.next();
        System.out.println(">>>>"+nc);
          }

          // Close the contexts when we're done
          result.close();
          ctx.close();
//      } catch (NamingException e) {
//          System.out.println("Create failed: " + e);
//      }
  }
*/  
  

/*  
  public void testLDAPService() throws Exception {    
    if(!test) {
      System.out.println("===LDAP Server is not started on host:"+LDAP_HOST + " port:"+LDAP_PORT) ;
      return ; 
    }
    LdapContext ctx = service_.getLdapContext() ;
    // Create attributes to be associated with the new context
    System.out.println("##############################CREATE##############################");
    // Create the context
    for(int i = 0; i < 10; i++) {
      String cn = "exo" + i ;
      String sn = "sn" + i ;
      String dn = "cn="+ cn + ", " + DEVELOPER_UNIT_DN ;
      Attributes attrs = new BasicAttributes(true); // case-ignore
      attrs.put(new BasicAttribute( "objectClass", "person"));                
      attrs.put(new BasicAttribute("cn", cn));          
      attrs.put(new BasicAttribute("sn", sn));        
      attrs.put(new BasicAttribute("telephonenumber", "0989654990"));   
      ctx.createSubcontext(dn, attrs);
      System.out.println(ctx.getAttributes(dn));
    }
    
    // Search for objects that have those matching attributes
    System.out.println("#################SEARCH BY ATTRIBUTES#############################");
    Attributes matchAttrs = new BasicAttributes(true); // ignore attribute name case
    matchAttrs.put(new BasicAttribute("sn", "sn1"));
    matchAttrs.put(new BasicAttribute("telephonenumber", "0989654990"));
    NamingEnumeration results = ctx.search(DEVELOPER_UNIT_DN, matchAttrs);
    while (results.hasMore()) {
      SearchResult sr = (SearchResult)results.next();
      printAttributes(sr.getName(), sr.getAttributes()) ; 
    }
    System.out.println("#################SEARCH BY QUERY FILTER##########################");
    SearchControls  searchControls = new SearchControls() ;
    searchControls.setCountLimit(10) ;
    searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE)  ;
    Control[] requestControls = {new PagedResultsControl(5, Control.CRITICAL) ,
                                 new SortControl(new String[]{"cn"}, Control.NONCRITICAL)} ;
    ctx.setRequestControls(requestControls);
    results = ctx.search(DEVELOPER_UNIT_DN,"(objectclass=person)", searchControls);
    while (results.hasMoreElements()) {
      SearchResult sr = (SearchResult)results.nextElement();
      printAttributes(sr.getName(), sr.getAttributes()) ; 
      if (sr instanceof HasControls) {
        Control[] controls = ((HasControls)sr).getControls();
        if (controls != null) {
          System.out.println("====================>response control is not null");
          for (int i = 0; i < controls.length; i++) {
            if (controls[i] instanceof PagedResultsResponseControl) {
              PagedResultsResponseControl prrc =  (PagedResultsResponseControl)controls[i];
              System.out.println("page result size: " + prrc.getResultSize());
              System.out.println("cookie: " + prrc.getCookie());
            } else {
              // Handle other response controls (if any)
            }
          }
        }
      }
    }
    
    Control[] controls = ctx.getResponseControls();
    if (controls != null) {
      System.out.println("====================>response control is not null");
      for (int i = 0; i < controls.length; i++) {
        if (controls[i] instanceof PagedResultsResponseControl) {
          PagedResultsResponseControl prrc =  (PagedResultsResponseControl)controls[i];
          System.out.println("page result size: " + prrc.getResultSize());
          System.out.println("cookie: " + prrc.getCookie());
        } else {
          // Handle other response controls (if any)
        }
      }
    }
    ctx.setRequestControls(null);
    System.out.println("##############################REMOVE##############################");
    for(int i = 0; i < 10; i++) {
      String cn = "exo" + i ;
      String dn = "cn="+ cn + ", " + DEVELOPER_UNIT_DN ;
      ctx.unbind(dn) ;
      System.out.println("remove " +  dn + " successfully");
    }
  }
*/  
  private  void printAttributes(String entry, Attributes attrs) {
    System.out.println("entry: " + entry);
    System.out.println("    " + attrs) ; 
  }
}
