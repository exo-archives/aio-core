/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Nov 15, 2005
 *
 * If the other service or a  third party want to  customize the initialization phase of the
 * organization service. they should make a customize class that implements  this interface and register
 * their plugin code via xml configuration. An example  of the customization code is we have an
 * organization service database intializer that create the predifined the user , group and membership
 * if the database is empty.
 * 
 * To Register the plugin code by the xml configuration:
 * You need to create a my.package.MyMembershipEventListener that implements this interface and add
 * a conf/portal/configuration.xml to the classpath. The configuration.xml can be in a jar
 * file. The file should contain the following configuraiton:
 * <pre>
 * <configuration>
 *   [..]
 *   <external-component-plugins>
 *     <target-component>org.exoplatform.services.organization.OrganizationService</target-component>
 *     <component-plugin>
 *        <name>my.customize.initializer.plugin</name>
 *        <set-method>addListenerPlugin</set-method>
 *        <type>my.package.MyInitializerPlugin</type>
 *        <description>your listener description</description>
 *      </component-plugin>
 *  </external-component-plugins>
 *  [...]
 *</configuration>
 * </pre>
 */
public interface OrganizationServiceInitializer {
  public void init(OrganizationService service) throws Exception ;
}