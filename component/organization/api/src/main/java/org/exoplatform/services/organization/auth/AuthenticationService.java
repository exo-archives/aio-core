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
package org.exoplatform.services.organization.auth;

import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.security.Identity;

/**
 * Created by The eXo Platform SAS
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * May 17, 2007
 * @deprecated
 */
public interface AuthenticationService {
    
  public boolean login(String userName, String password) throws Exception;
  
  public void broadcastAuthentication(Identity identity) throws Exception;

  public Identity getIdentityBySessionId(String sessionId) throws Exception;
  
  public Identity getCurrentIdentity();
  
  public void setCurrentIdentity(Identity identity);
  
  public void logout(String sessionId) throws Exception;
  
  public OrganizationService getOrganizationService();
}