/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.security.jaas;

import java.security.acl.Group;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.HashSet;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 29 avr. 2004
 */
public class JAASGroup implements Group {
  public static final String ROLES = "Roles";

  private String name = null;
  private HashSet members = null;


  public JAASGroup(String n) {
    this.name = n;
    this.members = new HashSet();
  }

  public synchronized boolean addMember(Principal principal) {
    return members.add(principal);
  }

  public synchronized boolean removeMember(Principal principal) {
    return members.remove(principal);
  }

  public boolean isMember(Principal principal) {    
    Enumeration en = members();
    while (en.hasMoreElements()) {
      Principal principal1 = (Principal) en.nextElement();
      if(principal1.getName().equals(principal.getName()))
        return true;
    }
    return false;
  }

  public Enumeration members() {
    class MembersEnumeration implements Enumeration {
      private Iterator itor;
      public MembersEnumeration(Iterator itor) {
        this.itor = itor;
      }
      public boolean hasMoreElements() {
        return this.itor.hasNext();
      }
      public Object nextElement() {
        return this.itor.next();
      }
    }
    return new MembersEnumeration(members.iterator());
  }

  public int hashCode() {
    return getName().hashCode();
  }

  public boolean equals(Object object) {
    if (!(object instanceof Group))
      return false;
    return ((Group) object).getName().equals(getName());
  }

  public String toString() {
    return getName();
  }

  public String getName() {
    return name;
  }

}
