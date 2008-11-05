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

import org.w3c.css.sac.Condition;
import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CombinatorCondition;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class ConditionObject implements Condition {

  public static ConditionObject create(Condition condition) {
    if (condition instanceof AttributeCondition) {
      AttributeCondition attribute = (AttributeCondition)condition;
      return new AttributeConditionObject(
        attribute.getConditionType(),
        attribute.getNamespaceURI(),
        attribute.getLocalName(),
        attribute.getSpecified(),
        attribute.getValue());
    } else if (condition instanceof CombinatorCondition) {
      CombinatorCondition combinator = (CombinatorCondition)condition;
      return new CombinatorConditionObject(
        combinator.getConditionType(),
        create(combinator.getFirstCondition()),
        create(combinator.getSecondCondition()));
    } else {
      throw new UnsupportedOperationException("Condition " + condition + " not yet supported");
    }
  }

  private final short type;

  public ConditionObject(short type) {
    this.type = type;
  }

  public short getConditionType() {
    return type;
  }

  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj instanceof ConditionObject) {
      ConditionObject that = (ConditionObject)obj;
      if (type == that.type) {
        return safeEquals(that);
      }
    }
    return false;
  }

  protected abstract boolean safeEquals(ConditionObject that);
}
