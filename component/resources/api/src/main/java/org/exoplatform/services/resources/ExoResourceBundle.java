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
package org.exoplatform.services.resources;

import java.io.Serializable;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

/**
 * May 7, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ExoResourceBundle.java 9439 2006-10-12 03:28:53Z thuannd $
 **/
@SuppressWarnings("serial")
public class ExoResourceBundle extends ListResourceBundle implements Serializable {

  private Object[][] contents ;

  public ExoResourceBundle(String data) {
    String [] tokens = data.split("\\n");
    int i = 0;
    String [][] properties = new String[tokens.length][2];
    for(String token : tokens){
      int idx = token.indexOf('=');
      if(idx < 0 || idx >= token.length() - 1) continue;
      properties[i][0] = token.substring(0, idx);
      properties[i][1] = token.substring(idx+1, token.length());;
      i++;
    }
    contents = new Object[i][2];
    System.arraycopy(properties, 0, contents, 0, i);
  }
  
  public ExoResourceBundle(String data, ResourceBundle parent) {
    this(data) ; 
    setParent(parent);
  }
  
  public Object[][] getContents() {
    return contents;
  }
}