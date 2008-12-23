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
package org.exoplatform.services.ldap;

import java.util.List;

import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.xml.InitParams;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Oct 16, 2005
 */
public class DeleteObjectCommand extends BaseComponentPlugin {

  public List<String> objectsToDelete;

  @SuppressWarnings("unchecked")
  public DeleteObjectCommand(InitParams params) {
    objectsToDelete = params.getValuesParam("objects.to.delete").getValues();
  }

  public List<String> getObjectsToDelete() {
    return objectsToDelete;
  }

}
