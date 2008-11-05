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

import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.css.sac.Condition;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ConditionalSelectorObject extends SimpleSelectorObject implements ConditionalSelector {

  private SimpleSelectorObject simple;
  private ConditionObject condition;

  public ConditionalSelectorObject(short type, SimpleSelectorObject simple, ConditionObject condition) {
    super(type);
    this.simple = simple;
    this.condition = condition;
  }

  public SimpleSelector getSimpleSelector() {
    return simple;
  }

  public Condition getCondition() {
    return condition;
  }

  protected boolean safeEquals(SelectorObject that) {
    if (that instanceof ConditionalSelectorObject) {
      ConditionalSelectorObject thatConditional = (ConditionalSelectorObject)that;
      if (simple == null) {
        if (thatConditional.simple != null) {
          return false;
        }
      }
      return simple.equals(thatConditional.simple) && condition.equals(thatConditional.condition);
    }
    return false;
  }
}

