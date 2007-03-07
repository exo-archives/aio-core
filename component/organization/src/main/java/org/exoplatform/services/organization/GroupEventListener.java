/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization;

import org.exoplatform.container.component.BaseComponentPlugin;
/**
 * Author : Tuan Nguyen
 *          tuan08@groups.sourceforge.net
 * Wed, Feb 18, 2004 @ 21:33 
 * 
 * If the other service or a  third party want to  customize their code to handle a
 * group event, the event  can be new , update or remove.  They  should make a class 
 * that  extends from  this class and  register  the listener with the organization service.
 * There are 2 ways to register  a listener with the service.
 * a) To do it programatically:
 * [..]
 * import org.exoplatform.container.PortalContainer ;
 * import org.exoplatform.services.organization.OrganizationService ;
 * [..]
 * GroupEventListener listener = new MyGroupEventListener(..) ;
 * PortalContainer pcontainer =  PortalContainer.getInstance() ;
 * OrganizationService service = 
 *   (OrganizationService) pcontainer.getInstanceOfType(OrganizationService.class) ;
 * service.getGroupHandler().addGroupEventListener(listener) ;
 * b) Register by the xml configuration:
 * You need to create a my.package.MyGroupEventListener that extends this class and add
 * a conf/portal/configuration.xml to the classpath. The configuration.xml can be in a jar
 * file. The file should contain the following configuraiton:
 * <pre>
 * <configuration>
 *   [..]
 *   <external-component-plugins>
 *     <target-component>org.exoplatform.services.organization.OrganizationService</target-component>
 *     <component-plugin>
 *        <name>my.group.listener</name>
 *        <set-method>addListenerPlugin</set-method>
 *        <type>my.package.MyGroupEventListener</type>
 *        <description>your listener description</description>
 *      </component-plugin>
 *  </external-component-plugins>
 *  [...]
 *</configuration>
 * </pre>
 */
public class GroupEventListener extends BaseComponentPlugin {
  /**
   * This method is called before the group is persisted  to the database.
   * @param group The group to be saved
   * @param isNew if the group is a new record in the database or not
   * @throws Exception The developer can decide to throw an exception or not. If the listener
   * throw an exception, the organization service should not save/update the group to  the database 
   */
  public void preSave(Group group, boolean isNew) throws Exception {  }

  /**
   * This method is called after the group has been saved but not commited yet
   * @param group The group has been saved.
   * @param isNew  if the group is a new  record in the database or not
   * @throws Exception The developer can decide to throw  the exception or not. If the method
   * throw an exception. The organization service should role back the data to the state before
   * the method GroupHandler.addChild(..) or GroupHandler.saveGroup(..) is called.
   */
  public void postSave(Group group, boolean isNew) throws Exception {  }

  /**
   * This method is called before a group should be deleted
   * @param group  the group to be delete
   * @throws Exception The developer can decide to throw the exception or not. If the method throw
   * an exception. The organization service should not remove the group record from the database.
   */
  public void preDelete(Group group) throws Exception { }
  /**
   * This method should be called after the group  has been removed from the database but not
   * commited yet.
   * @param group The  group has been removed.
   * @throws Exception The developer can decide to throw the exception or not. If the method 
   * throw the exception, the organization service should role back the database to the state
   * before the method  GroupHandler.removeGroup(..) is called.
   */ 
  public void postDelete(Group group) throws Exception {  }
}
