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
package org.exoplatform.services.web.css.model;

import org.w3c.css.sac.DocumentHandler;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class DeclarationObject{

  private final String name;
  private final LexicalUnitObject value;

  public DeclarationObject(String name, LexicalUnitObject value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public LexicalUnitObject getValue() {
    return value;
  }

  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj instanceof DeclarationObject) {
      DeclarationObject that = (DeclarationObject)obj;
      return name.equals(that.name) && value.equals(that.value);
    }
    return false;
  }

  void internalVisit(DocumentHandler handler) {
    handler.property(name, value, false);
  }
}
