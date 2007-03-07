/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.ldap;

import java.util.Enumeration;
import java.util.Iterator;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.net.NetService;
import org.exoplatform.test.BasicTestCase;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPSearchResults;
import com.novell.ldap.util.Base64;



/**
 * Created by The eXo Platform SARL
 * Author : Nhu Thuan
 *          thuannd@exoplatform.com
 * Oct 3, 2005
 */
public class TestNovellLDAPAPI extends BasicTestCase {
  final  static public String ROOT_DN = "cn=Manager,dc=exoplatform,dc=org" ;
  final  static public String ROOT_PASSWORD = "secret" ;
  final  static public String DEVELOPER_UNIT_DN = "ou=developer,o=company,c=vietnam,dc=exoplatform,dc=org" ;
  final  static public String EXO_DEVELOPER_DN = "cn=exo, " + DEVELOPER_UNIT_DN ;
  static private  String      LDAP_HOST = "127.0.0.1" ;
  static private  int         LDAP_PORT = 389 ;
  
  private NetService nservice_ ;  
  public TestNovellLDAPAPI(String name) {
    super(name) ;
  }
  
  public void setUp() throws Exception {
    PortalContainer manager = PortalContainer.getInstance() ;
    nservice_ = (NetService)manager.getComponentInstanceOfType(NetService.class) ;
  }
  
  public void testNovellLDAPAPI() throws Exception {
    if(nservice_.ping(LDAP_HOST,LDAP_PORT)<0) {
      System.out.println("LDAP Server is not started on host:"+LDAP_HOST + " port:"+LDAP_PORT) ;
      return ;
    }
    int    ldapVersion = LDAPConnection.LDAP_V3;        
    LDAPConnection conn = new LDAPConnection();
    conn.connect( LDAP_HOST, LDAP_PORT);
    //conn.authenticate("cn=Manager,dc=example,dc=com", "secret");
    conn.bind(ldapVersion, ROOT_DN, ROOT_PASSWORD.getBytes("utf-8") );
    
    System.out.println("========> create ldap entry " + EXO_DEVELOPER_DN);
    LDAPAttributeSet attributeSet = new LDAPAttributeSet();      
    attributeSet.add(new LDAPAttribute( "objectClass", "person"));                
    attributeSet.add(new LDAPAttribute("cn", "exo"));          
    attributeSet.add(new LDAPAttribute("sn", "platform"));        
    attributeSet.add(new LDAPAttribute("telephonenumber", "0989654990"));                                                     
    LDAPEntry newEntry = new LDAPEntry( EXO_DEVELOPER_DN, attributeSet );
    conn.add( newEntry );
    System.out.println( "<======Added object: " + EXO_DEVELOPER_DN + " successfully." );
    
    System.out.println( "\nAdded object: " +EXO_DEVELOPER_DN + " successfully." );
    
    LDAPSearchResults results =
      conn.search(DEVELOPER_UNIT_DN,  //  search only the object in the subtree of  this dn 
          LDAPConnection.SCOPE_SUB, // return  all the  objects that match the  filter criteria and in the  sub tree
          "(objectclass=person)", // query  filter, (objectclass=*) for all  the object  
          null, // return all the attributes   of the  object
          false) ;  // return attrs and values
    
    //assertEquals("Expect to find 1 entry", 1, results.getCount()) ;
    while (results.hasMore()) {
      LDAPEntry nextEntry = null;
      nextEntry = results.next();
      System.out.println("\n entry: " + nextEntry.getDN());
      System.out.println("  Attributes: ");
      LDAPAttributeSet attrs = nextEntry.getAttributeSet();
      printLDAPAttributeSet(attrs) ;
    }
    System.out.println("   ---> count: " + results.getCount());
    System.out.println( "<======search " + DEVELOPER_UNIT_DN  + " successfully." );
    //delete the  new  created  ldap  entry
    conn.delete(EXO_DEVELOPER_DN) ;
    conn.disconnect() ;
  }
  
  
  private void  printLDAPAttributeSet(LDAPAttributeSet attrs) {
    Iterator allAttributes = attrs.iterator();
    while(allAttributes.hasNext()) {
      LDAPAttribute attribute = (LDAPAttribute)allAttributes.next();
      String attributeName = attribute.getName();
      System.out.print("\n    " + attributeName + ": ");
      Enumeration allValues = attribute.getStringValues();
      if( allValues != null) {
        System.out.print("[") ;
        while(allValues.hasMoreElements()) {
          String Value = (String) allValues.nextElement();
          if (!Base64.isLDIFSafe(Value)) {
            Value = Base64.encode(Value.getBytes());
          }
          System.out.print(" " + Value + " ");
        }
        System.out.print("]") ;
      }
    }
    System.out.println() ;
  }
}
