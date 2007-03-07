/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.ldap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapContext;

import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.services.ldap.ObjectClassAttribute;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.OrganizationServiceInitializer;

/**
 * Created by The eXo Platform SARL
 * Author : Thuannd
 *         nhudinhthuan@yahoo.com
 * Feb 14, 2006
 */
public class OrganizationLdapInitializer 
  extends BaseComponentPlugin implements OrganizationServiceInitializer, ComponentPlugin {
  
  private BaseDAO baseHandler;
  
  public void init(OrganizationService service) throws Exception {   
    baseHandler = (BaseDAO)service.getUserHandler();
    createSubContext(baseHandler.ldapAttrMapping_.groupsURL);
    createSubContext(baseHandler.ldapAttrMapping_.userURL);
    createSubContext(baseHandler.ldapAttrMapping_.membershipTypeURL);
    createSubContext(baseHandler.ldapAttrMapping_.profileURL);
  }
  
  public void createSubContext(String dn) throws Exception {
    Pattern pattern = Pattern.compile("\\b\\p{Space}*=\\p{Space}*", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(dn);
    dn = matcher.replaceAll("="); 
    LdapContext context = baseHandler.ldapService_.getLdapContext();
    String [] explodeDN = baseHandler.explodeDN(dn, false);
    if(explodeDN.length < 1) return;
    dn = explodeDN[explodeDN.length-1];      
    int i = explodeDN.length-2;
    for(; i > -1; i--){
      if(!explodeDN[i].startsWith("dc=")) break;
      dn = explodeDN[i]+","+dn;
    }   
    createDN(dn, context);      
    for(; i > -1; i--){
      dn = explodeDN[i]+","+dn;
      createDN(dn, context);      
    }    
  } 
  
  private void createDN(String dn, LdapContext context) throws Exception {   
    try{
      Object obj = context.lookupLink(dn);      
      if(obj != null) return;
    }catch(Exception exp){}    
    String nameValue = dn.substring(dn.indexOf("=")+1, dn.indexOf(","));
    BasicAttributes attrs = new BasicAttributes();
    if(dn.toLowerCase().startsWith("ou=")){
      attrs.put( new ObjectClassAttribute(new String[]{"top", "organizationalUnit"}));  
      attrs.put("ou", nameValue);   
    }else if(dn.toLowerCase().startsWith("cn=")){
      attrs.put( new ObjectClassAttribute(new String[]{"top", "organizationalRole"}));  
      attrs.put("cn", nameValue);  
    }else if(dn.toLowerCase().startsWith("c=")){      
      attrs.put( new ObjectClassAttribute(new String[]{"country"}));  
      attrs.put("c", nameValue);      
    }else if(dn.toLowerCase().startsWith("o=")){      
      attrs.put( new ObjectClassAttribute(new String[]{"organization"}));  
      attrs.put("o", nameValue);      
    }else if(dn.toLowerCase().startsWith("dc=")){      
      attrs.put( new ObjectClassAttribute(new String[]{"top","dcObject","organization"}));  
      attrs.put("dc", nameValue);      
      attrs.put("o", nameValue);      
    }
    attrs.put("description", nameValue);
    context.createSubcontext(dn, attrs);    
  }  

}
