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
package org.exoplatform.services.naming;

import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.exoplatform.container.xml.InitParams;
/**
 * Created by The eXo Platform SAS
 * Author : Thuannd
 *          nhudinhthuan@yahoo.com
 * Apr 12, 2006
 */
public class NamingService  {
  private static String ROOT_DOMAIN = "java:comp/exo" ;
  private Hashtable<String, String> env_ ;
  
  public NamingService(InitParams params) throws Exception {  
    Map<String,String> env = params.getPropertiesParam("environment").getProperties() ;
    env_ = new Hashtable<String, String>() ;
    env_.putAll(env) ;
    createSubcontext("java:comp", true) ;
    createSubcontext(ROOT_DOMAIN, true) ;
  }
  
  public Context getContext()  throws Exception {   return new InitialContext(env_); }
  
  public Map<String, String>  getEnvironmment() { return env_; }
  
  public void createSubcontext(String name, boolean createAncestor) throws  Exception {
    Context context =  getContext() ;
    try {
      context.lookup(ROOT_DOMAIN + "/" + name) ;
    } catch (NameNotFoundException ex) {
      context.createSubcontext(name);
    }
  }
  
  public void bind(String name, Object value) throws  Exception { 
    getContext().bind(ROOT_DOMAIN + "/" + name, value);
  }
    
  public void rebind(String name, Object value) throws  Exception {
    getContext().rebind(ROOT_DOMAIN + "/" + name, value);
  }
  
  public Object lookup(String name) throws  Exception {
    return getContext().lookup(ROOT_DOMAIN + "/" + name);
  }
}
