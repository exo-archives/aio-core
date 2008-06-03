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

package org.exoplatform.services.security.j2ee;

import java.security.acl.Group;

import javax.security.auth.login.LoginException;

import org.exoplatform.services.security.jaas.DefaultLoginModule;
import org.exoplatform.services.security.jaas.JAASGroup;
import org.exoplatform.services.security.jaas.RolePrincipal;

/**
 * Created by The eXo Platform SAS        .
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public class JbossLoginModule extends DefaultLoginModule {

  @Override
  public boolean commit() throws LoginException {

    if(super.commit()) {
     // subject_.getPrincipals().add(new UserPrincipal(this.identity_.getUserId()));
      Group roleGroup = new JAASGroup(JAASGroup.ROLES);
      for(String role : identity_.getRoles()) 
        roleGroup.addMember(new RolePrincipal(role));      
     
      subject_.getPrincipals().add(roleGroup); 
      return true;
    } else {
      return false;
    }
      
    
  }

}

