/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization;

import org.exoplatform.container.component.BaseComponentPlugin;
/**
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Wed, Feb 18, 2004 @ 21:33 
 * 
 * If the other service or a  third party want to  customize their code to handle an
 * user event, the event  can be new , update or remove.  They  should make a class 
 * that  extends from  this class and  register  the listener with the organization service.
 * There are 2 ways to register  a listener with the service.
 * a) To do it programatically:
 * [..]
 * import org.exoplatform.container.PortalContainer ;
 * import org.exoplatform.services.organization.OrganizationService ;
 * [..]
 * UserEventListener listener = new MyUserEventListener(..) ;
 * PortalContainer pcontainer =  PortalContainer.getInstance() ;
 * OrganizationService service = 
 *   (OrganizationService) pcontainer.getInstanceOfType(OrganizationService.class) ;
 * service.getUserHandler().addUserEventListener(listener) ;
 * b) Register by the xml configuration:
 * You need to create a my.package.MyUserEventListener that extends this class and add
 * a conf/portal/configuration.xml to the classpath. The configuration.xml can be in a jar
 * file. The file should contain the following configuraiton:
 * <pre>
 * <configuration>
 *   [..]
 *   <external-component-plugins>
 *     <target-component>org.exoplatform.services.organization.OrganizationService</target-component>
 *     <component-plugin>
 *        <name>my.user.listener</name>
 *        <set-method>addListenerPlugin</set-method>
 *        <type>my.package.MyUserEventListener</type>
 *        <description>your listener description</description>
 *      </component-plugin>
 *  </external-component-plugins>
 *  [...]
 *</configuration>
 * </pre>
 */
public class UserEventListener extends BaseComponentPlugin {  
  /**
   * This method is called before the user is persisted  to the database.
   * @param user The user to be saved
   * @param isNew if the user is a new record in the database or not
   * @throws Exception The developer can decide to throw an exception or not. If the listener
   * throw an exception, the organization service should not save/update the user to  the database 
   */
  public void preSave(User user, boolean isNew) throws Exception {  }
  /**
   * This method is called after the user has been saved but not commited yet
   * @param user The user instance has been saved.
   * @param isNew if the user is a new record in the database or not
   * @throws Exception The developer can decide to throw  the exception or not. If the method
   * throw an exception. The organization service should role back the data to the state before
   * the method userHandler.createUser(..) or UserHandler.saveUser(..) is called.
   */
  public void postSave(User user, boolean isNew) throws Exception {  }
  /**
   * This method is called before an user should be deleted
   * @param user the user to be delete
   * @throws Exception The developer can decide to throw the exception or not. If the method throw
   * an exception. The organization service should not remove the user record from the database.
   */
  public void preDelete(User user) throws Exception {  }
  /**
   * This method should be called after the user  has been removed from the database but not
   * commited yet.
   * @param user The user instance which has been removed from the database.
   * @throws Exception The developer can decide to throw the exception or not. If the method 
   * throw the exception, the organization service should role back the database to the state
   * before the method  UserHandler.removeUser(..) is called.
   */
  public void postDelete(User user) throws Exception {  }
}