package org.exoplatform.services.ldap.impl;


public class LDAPConnectionConfig {
  
  private String  providerURL = "ldap://127.0.0.1:389" ;
  
  private  String rootdn ;//= "cn=admin,dc=exoplatform,dc=com";
  
  private  String password ;//= "exo";
  
  private String  version ;//= LDAPConnection.LDAP_V3;
  
  private String  authenticationType = "simple" ; 
 
  private String serverName = "default";
  
  private int minConnection ;
  
  private int maxConnection ;
  
  private String referralMode = "follow";
  
  public String getRootDN(){ return this.rootdn; }
  public void setRootDN( String d){ this.rootdn = d; }
  
  public String getPassword(){  return this.password; }
  public void setPassword( String p){ this.password = p; }
  
  public String getVerion(){ return this.version; }
  public void setVersion(String v){  this.version = v; }
  
  public String getAuthenticationType() { return authenticationType ; }
  public void   setAuthenticationType(String s) { authenticationType = s ; }
  
  public int  getMinConnection(){ return this.minConnection ; }
  public void setMinConnection(int n){  this.minConnection = n; }
  
  public int  getMaxConnection(){ return this.maxConnection ; }
  public void setMaxConnection(int n){  this.maxConnection = n; }
  
  public String getProviderURL() {  return providerURL ; }
  public void   setProviderURL(String s) { providerURL = s ; }  
  
  public String getServerName() { return serverName; }  
  public void setServerName(String n) { serverName = n ; }
  
  public String getReferralMode() { return referralMode; }  
  public void setReferralMode(String referral) { referralMode = referral ; } 
}