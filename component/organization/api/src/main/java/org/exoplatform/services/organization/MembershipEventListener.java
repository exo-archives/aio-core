/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization;

import org.exoplatform.container.component.BaseComponentPlugin;
/**
 * Author : Tuan Nguyen
 *          tuan08@ms.sourceforge.net
 * Wed, Feb 18, 2004 @ 21:33 
 * 
 * If the other service or a  third party want to  customize their code to handle a
 * membership event, the event  can be new or remove.  They  should make a class 
 * that  extends from  this class and  register  the listener with the organization service.
 * There are 2 ways to register  a listener with the service.
 * a) To do it programatically:
 * [..]
 * import org.exoplatform.container.PortalContainer ;
 * import org.exoplatform.services.organization.OrganizationService ;
 * [..]
 * MembershipEventListener listener = new MyMembershipEventListener(..) ;
 * PortalContainer pcontainer =  PortalContainer.getInstance() ;
 * OrganizationService service = 
 *   (OrganizationService) pcontainer.getInstanceOfType(OrganizationService.class) ;
 * service.getMembershipHandler().addMembershipEventListener(listener) ;
 * b) Register by the xml configuration:
 * You need to create a my.package.MyMembershipEventListener that extends this class and add
 * a conf/portal/configuration.xml to the classpath. The configuration.xml can be in a jar
 * file. The file should contain the following configuraiton:
 * <pre>
 * <configuration>
 *   [..]
 *   <external-component-plugins>
 *     <target-component>org.exoplatform.services.organization.OrganizationService</target-component>
 *     <component-plugin>
 *        <name>my.membership.listener</name>
 *        <set-method>addListenerPlugin</set-method>
 *        <type>my.package.MyMembershipEventListener</type>
 *        <description>your listener description</description>
 *      </component-plugin>
 *  </external-component-plugins>
 *  [...]
 *</configuration>
 * </pre>
 */
public class MembershipEventListener extends BaseComponentPlugin {
  /**
   * This method is called  before the membership  object is saved. 
   * @param m the membership  to be saved
   * @param isNew If the membership is a new record in the database or not.
   * @throws Exception The developer can decide to throw  the exception or not. If the method
   * throw an exception. The organization service should not save the membership.
   */
  public void preSave(Membership m, boolean isNew) throws Exception { }
  /**
   * This method is called after the membership has been saved but not commited yet
   * @param m The mebership object
   * @param isNew The membership is a new record or not.
   * @throws Exception The developer can decide to throw  the exception or not. If the method
   * throw an exception. The organization service should role back the data to the state before
   * the method MembershipHandler.linkMembership(..)  is called.
   */
  public void postSave(Membership m, boolean isNew) throws Exception { }
  /**
   * This method is called before the membership is removed
   * @param m The membership object to be removed
   * @throws Exception he developer can decide to throw the exception or not. If the method throw
   * an exception. The organization service should not remove the membership record from the database.
   */
  public void preDelete(Membership m) throws Exception { }

  /**
   * This method should be called after the membership  has been removed from the database but not
   * commited yet.
   * @param m The membership which has been removed from the database.
   * @throws Exception The developer can decide to throw the exception or not. If the method 
   * throw the exception, the organization service should role back the database to the state
   * before the method  MembershipHandler.linkMembership(..)  is called.
   */
  public void postDelete(Membership m) throws Exception { }
}
