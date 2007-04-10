package org.exoplatform.services.security;

import javax.security.auth.Subject;

import org.apache.commons.logging.Log;
import org.exoplatform.services.security.sso.SSOAuthenticationConfig;

/**
 * Date: 27 avr. 2004
 * Time: 13:30:38
 */
public interface SecurityService {
	
	public static String STANDALONE_AUTHENTICATION = "standalone" ;
	public static String SSO_AUTHENTICATION = "sso" ;

  public boolean authenticate(String login, String password) throws Exception;

  public Subject getSubject(String userName);
  public void setUpAndCacheSubject(String userName, Subject value) throws Exception;
  public void removeSubject(String userName);
  
  /**
   * @return current thread's subject (stored as ThreadLocal)
   */
  Subject getCurrentSubject();

  public void addSubjectEventListener(SubjectEventListener subjectEventListener);

  public boolean hasMembershipInGroup(String user, String roleExpression) ;
  public boolean hasMembershipInGroup(String userId, String membershipName, String groupName);
  public boolean isUserInRole(String userName, String role);
  
  public String getSSOAuthentication() ;
  public SSOAuthenticationConfig getSSOAuthenticationConfig() ;
  
  public boolean isSSOAuthentication() ;
  public boolean isStandaloneAuthentication() ;
  
  public String getProxyTicket(String userName, String urlOfTargetService) throws Exception ;

  public Log getLog() ;
}
