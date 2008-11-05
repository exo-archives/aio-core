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

import java.util.List;
import java.util.ArrayList;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class RuleObject {

  private SelectorListImpl selectors;
  private List<DeclarationObject> declarations;

  public RuleObject() {
    this.selectors = new SelectorListImpl();
    this.declarations = new ArrayList<DeclarationObject>();
  }

  public void addSelector(SelectorObject selector) {
    selectors.add(selector);
  }

  public List<SelectorObject> getSelectors() {
    return selectors;
  }

  public DeclarationObject addDeclaration(String name, LexicalUnitObject value) {
    DeclarationObject decl = new DeclarationObject(name, value);
    declarations.add(decl);
    return decl;
  }

  public List<DeclarationObject> getDeclarations() {
    return declarations;
  }

  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj instanceof RuleObject) {
      RuleObject that = (RuleObject)obj;
      return selectors.equals(that.selectors) && declarations.equals(that.declarations);
    }
    return false;
  }

  protected void internalVisit(DocumentHandler handler) throws IllegalArgumentException {
    handler.startSelector(selectors);
    for (DeclarationObject declaration : declarations) {
      declaration.internalVisit(handler);
    }
    handler.endSelector(selectors);
  }
}
