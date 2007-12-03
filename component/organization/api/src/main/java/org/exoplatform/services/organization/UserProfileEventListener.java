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
package org.exoplatform.services.organization;

import org.exoplatform.container.component.BaseComponentPlugin;

/**
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:46:04 PM
 * 
 * If the other service or a  third party want to  customize their code to handle an 
 * user profile event, the event  can be save or delete,  they  should make a class that 
 * extends from  this class and  register  the listener with the organization service.
 * There are 2 ways to register  a listener with the service.
 * a) To do it programatically:
 * [..]
 * import org.exoplatform.container.PortalContainer ;
 * import org.exoplatform.services.organization.OrganizationService ;
 * [..]
 * UserProfileListener listener = new MyUserProfileListener(..) ;
 * PortalContainer pcontainer =  PortalContainer.getInstance() ;
 * OrganizationService service = 
 *   (OrganizationService) pcontainer.getInstanceOfType(OrganizationService.class) ;
 * service.getUserHandler().addUserProfileEventListener(listener) ;
 * b) Register by the xml configuration:
 * You need to create a my.package.MyUserProfileEventListener that extends this class and add
 * a conf/portal/configuration.xml to the classpath. The configuration.xml can be in a jar
 * file. The file should contain the following configuraiton:
 * <pre>
 * <configuration>
 *   [..]
 *   <external-component-plugins>
 *     <target-component>org.exoplatform.services.organization.OrganizationService</target-component>
 *     <component-plugin>
 *        <name>my.user.profile.listener</name>
 *        <set-method>addListenerPlugin</set-method>
 *        <type>my.package.MyUserProfileEventListener</type>
 *        <description>your listener description</description>
 *      </component-plugin>
 *  </external-component-plugins>
 *  [...]
 *</configuration>
 * </pre>
 */
public class UserProfileEventListener extends BaseComponentPlugin {
   
  /**
   * When  you  register the listener with the organization service. This method 
   * should be called in the @see UserProfileHandler saveUseProfile(..) method before the
   * user profile instance is saved to the database.
   * @param user The user profile instance  that will be saved.
   * @param isNew if  there is an user profile record already in the database or not
   * @throws Exception The developer can decide to throw the exception or not. If he throw
   * an exception, then the organization service should not persist  the new record and the 
   * change of  the user profile instance to the database.
   */
  public void preSave(UserProfile user, boolean isNew) throws Exception {
  }

  /**
   * When  you  register the listener with the organization service. This method 
   * should be called in the @see UserProfileHandler saveUseProfile(..) method after the
   * user profile instance is saved to the database.
   * @param user The user profile instance  that is already saved in the database. But not 
   * commited yet
   * @param isNew if  there is an user profile record already in the database or not
   * @throws Exception The developer can decide to throw the exception or not. If he throw
   * an exception, then the organization service should not persist  the new record and the 
   * change of  the user profile instance to the database. The database state should be roled
   * back to the state before the saveUserProfile(..) method is called.
   */
  public void postSave(UserProfile user, boolean isNew) throws Exception {
  }
  /**
   * When  you  register the listener with the organization service. This method 
   * should be called in the @see UserProfileHandler removeUseProfile(..) method before the
   * user profile instance is  removed from the database.
   * @param user The user to be removed
   * @throws Exception The developer can decide what to do in his overrided method. If the
   * developer throw an exception. The organization service should not removed the user 
   * profile record
   */
  public void preDelete(UserProfile user) throws Exception {
  }

  /**
   * When  you  register the listener with the organization service. This method 
   * should be called in the @see UserProfileHandler removeUseProfile(..) method after the
   * user profile instance is  removed from the database.
   * @param user The user instance that is already removed from the database
   * @throws Exception The developer can decide what to do in his overrided method. If the
   * developer throw an exception. The organization service should role back the database
   * to the state before the removeUserProfile(..) is called.
   */
  public void postDelete(UserProfile user) throws Exception {
  }
}