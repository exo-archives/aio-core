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

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class FunctionLexicalUnitObject extends LexicalUnitObject {

  private String functionName;

  public FunctionLexicalUnitObject(short type, String functionName) {
    super(type);
    this.functionName = functionName;
  }

  public String getFunctionName() {
    return functionName;
  }

  protected boolean safeEquals(LexicalUnitObject that) {
    if (super.safeEquals(that)) {
      if (that instanceof FunctionLexicalUnitObject) {
        String thatFunctionName = ((FunctionLexicalUnitObject)that).functionName;
        return functionName != null ? functionName.equals(thatFunctionName) : thatFunctionName == null;
      }
    }
    return false;
  }
}

