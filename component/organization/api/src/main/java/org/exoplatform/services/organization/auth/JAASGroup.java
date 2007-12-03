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

package org.exoplatform.services.organization.auth;

import java.security.acl.Group;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.HashSet;
import java.io.Serializable;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 29 avr. 2004
 */
public class JAASGroup implements Group, Serializable {
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
