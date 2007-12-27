package org.exoplatform.services.security.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.exception.ExoServiceException;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.security.SecurityService;
/**
 * Use with
 * <code>
 * 	<configuration>
 * 		<component>
 * 			<key>org.exoplatform.services.security.SecurityService</key>
 * 			<type>org.exoplatform.services.security.impl.Tomcat55SecurityServiceImpl</type>
 * 			<init-params>
 * 				<value-param>
 * 					<name>security.authentication</name>
 * 					<value>standalone</value>
 * 				</value-param>
 * 			</init-params>
 * 		</component>
 *   </configuration>
 * </code>
 * 
 * in exo-platform/web/share-portal/WEB-INF/conf/security-configuration.xml. This configuration will be 
 * transferred to a specific web-app like ecm upon build, and override the default one, see the notes below.
 * 
 * Make sure the configuration.xml of your web-app contains:
 * <code>
 *   <import>war:/conf/security-configuration.xml</import> 
 * </code>
 * to override the default setting from the distribution.
 * 
 * Note: some of the web apps, like ecm use the jass security mechanism. On some linux platforms, like
 * Xandros, where the default shell is bash, the configuration line in exo-run.sh must be:
 * export JAVA_OPTS="$LOG_OPTS $SECURITY_OPTS $JAVA_OPTS" instead of:JAVA_OPTS="$LOG_OPTS $SECURITY_OPTS $JAVA_OPTS"
 * 
 * @author arylwen - 03-18-2006
 * 
 */
public class Tomcat55SecurityServiceImpl extends SecurityServiceImpl implements SecurityService {

//  public Tomcat55SecurityServiceImpl(LogService logService,
//      OrganizationService organizationService, InitParams params) {
  public Tomcat55SecurityServiceImpl(
        OrganizationService organizationService, InitParams params) {
    super(organizationService, params);
   }

  //Use this for  tomcat 5.5.x
  public void setUpAndCacheSubject(String userName, Subject value) throws ExoServiceException {
    Set principals = value.getPrincipals();
    principals.add(new UserPrincipalImpl(userName));
    Collection groups = null;
    try {
      groups = getOrgService().getGroupHandler().findGroupsOfUser(userName);
    } catch (Exception e) {
      throw new ExoServiceException(e);
    }
    for (Iterator iter = groups.iterator(); iter.hasNext();) {
      org.exoplatform.services.organization.Group group = 
        (org.exoplatform.services.organization.Group) iter.next();
      String groupId = group.getId();
      String[] splittedGroupName = StringUtils.split(groupId, "/");
      value.getPrincipals().add(new RolePrincipalImpl(splittedGroupName[0]));
    }
    getSubjects().put(userName, value);
    currentUserHolder.set(userName);
  }
}
