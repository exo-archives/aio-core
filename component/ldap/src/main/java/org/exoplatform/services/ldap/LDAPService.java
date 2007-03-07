package org.exoplatform.services.ldap;

import javax.naming.InitialContext;
import javax.naming.ldap.LdapContext;

/**
 * Created by the eXo platform team User: Daniel Summer Date: 25/5/2004
 * 
 * interface abstracted from JSDK
 */
public interface LDAPService {
  
  public static int DEFAULT_SERVER = 0;  
  public static int ACTIVE_DIRECTORY_SERVER = 1;  
//  public static int OPEN_LDAP_SERVER = 2;  
//  public static int NETSCAPE_SERVER = 3;  
//  public static int REDHAT_SERVER = 4;

	//	 Normal context for all directories
	public LdapContext getLdapContext() throws Exception;
	
	// LDAP booster pack context for v3 directories (except Active Directory)
	public InitialContext getInitialContext() throws Exception;
	
	// LDAP bind authentication
	public boolean authenticate(String userDN, String password) throws Exception;  
  
  public int getServerType();  
}
