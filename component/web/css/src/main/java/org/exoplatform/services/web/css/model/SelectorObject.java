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

import org.w3c.css.sac.Selector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.ConditionalSelector;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class SelectorObject implements Selector {

  public static SelectorObject create(Selector selector) {
    if (selector instanceof ConditionalSelector) {
      ConditionalSelector conditional = (ConditionalSelector)selector;
      return new ConditionalSelectorObject(
        conditional.getSelectorType(),
        (SimpleSelectorObject)create(conditional.getSimpleSelector()),
        ConditionObject.create(conditional.getCondition()));
    } else if (selector instanceof ElementSelector) {
      ElementSelector element = (ElementSelector)selector;
      return new ElementSelectorObject(
        element.getSelectorType(),
        element.getNamespaceURI(),
        element.getLocalName());
    } else if (selector instanceof DescendantSelector) {
      DescendantSelector descendant = (DescendantSelector)selector;
      SimpleSelectorObject simple = (SimpleSelectorObject)create(descendant.getSimpleSelector());
      SelectorObject ancestor = create(descendant.getAncestorSelector());
      return new DescendantSelectorObject(
        descendant.getSelectorType(),
        ancestor,
        simple);
    } else {
      throw new UnsupportedOperationException("Cannot create selector object for " + selector);
    }
  }

  private final short type;

  public SelectorObject(short type) {
    this.type = type;
  }

  public short getSelectorType() {
    return type;
  }

  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj instanceof SelectorObject) {
      SelectorObject that = (SelectorObject)obj;
      if (type == that.type) {
        return safeEquals(that);
      }
    }
    return false;
  }

  protected abstract boolean safeEquals(SelectorObject that);

}
