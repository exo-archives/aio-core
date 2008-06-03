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

package org.exoplatform.services.security;

import java.util.Set;

/**
 * Created by The eXo Platform SAS        .<br/>
 * Strategy of extraction J2EE roles from given group names  
 * @author Gennady Azarenkov
 * @version $Id:$
 */

public interface RolesExtractor {

  /**
   * Extracts J2EE roles from groups
   * @param groups
   * @return
   */
  Set <String> extractRoles(Set <String> groups);
}

