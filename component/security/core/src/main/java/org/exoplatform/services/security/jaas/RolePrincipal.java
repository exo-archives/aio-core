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
package org.exoplatform.services.security.jaas;

import java.io.Serializable;
import java.security.Principal;

/**
 * Created y the eXo platform team User: Tuan Nguyen Date: May 6th, 2007
 */
public class RolePrincipal implements Principal, Serializable {

  private static final long serialVersionUID = -8943003720689495978L;

  private String            name;

  public RolePrincipal(String name) {
    this.name = name;
  }

  /*
   * (non-Javadoc)
   * @see java.security.Principal#getName()
   */
  public String getName() {
    return name;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return getName();
  }
}
