/**
 *  Copyright 2003-2007 Greg Luck
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.exoplatform.services.organization.auth.pam.jaas;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.auth.JAASGroup;
import org.exoplatform.services.organization.auth.UserPrincipal;
import org.exoplatform.services.organization.auth.pam.Pam;
import org.exoplatform.services.organization.auth.pam.PamReturnValue;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;

/**
 * A <code>LoginModule</code> which invokes JPAM. This can be used from
 * standard JAAS implementations. It is an alternative to directly using the
 * {@link Pam} class. <p/> This class relies on the existence of a
 * .java.login.config with a login configuration called <code>net-sf-jpam</code>.
 * Copy the .java.login.config in the src/config/<architecture> directory to
 * running user's home directory.
 * 
 * @author <a href="mailto:gregluck@users.sourceforge.net">Greg Luck</a>
 * @version $Id: JpamLoginModule.java 19 2007-04-01 23:13:48Z gregluck $
 */
public class JpamLoginModule implements LoginModule {

  private static final Log LOGGER = ExoLogger.getLogger("JAASPam");
  private static final String SERVICE_NAME_OPTION = "serviceName";
  private Subject subject_;
  private CallbackHandler callbackHandler_;
  private Map sharedState_;
  private Map options_;
  private Pam pam_;
  private boolean loginStatus_ = false;

  /**
   * Method to abort the authentication process (phase 2). <p/>
   * <p>
   * This method is called if the LoginContext's overall authentication failed.
   * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules did
   * not succeed). <p/>
   * <p>
   * If this LoginModule's own authentication attempt succeeded (checked by
   * retrieving the private state saved by the <code>login</code> method),
   * then this method cleans up any state that was originally saved. <p/> <p/>
   * 
   * @return true if this method succeeded, or false if this
   *         <code>LoginModule</code> should be ignored.
   * @throws javax.security.auth.login.LoginException if the abort fails
   */
  public boolean abort() throws LoginException {
    return true;
  }

  /**
   * Method to commit the authentication process (phase 2). <p/>
   * <p>
   * This method is called if the LoginContext's overall authentication
   * succeeded (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
   * LoginModules succeeded). <p/>
   * <p>
   * If this LoginModule's own authentication attempt succeeded (checked by
   * retrieving the private state saved by the <code>login</code> method),
   * then this method associates relevant Principals and Credentials with the
   * <code>Subject</code> located in the <code>LoginModule</code>. If this
   * LoginModule's own authentication attempted failed, then this method
   * removes/destroys any state that was originally saved. <p/> <p/>
   * 
   * @return true if this method succeeded, or false if this
   *         <code>LoginModule</code> should be ignored.
   * @throws javax.security.auth.login.LoginException if the commit fails
   */
  public boolean commit() throws LoginException {
    String[] groups = pam_.getGroups();
    JAASGroup jaasGroup = new JAASGroup("Groups");
    for (String g : groups) {
      jaasGroup.addMember(new GroupPrincipal(g));
    }
    subject_.getPrincipals().add(jaasGroup);    
    return loginStatus_;
  }

  /**
   * Method to authenticate a <code>Subject</code> (phase 1). <p/>
   * <p>
   * The implementation of this method authenticates a <code>Subject</code>.
   * For example, it may prompt for <code>Subject</code> information such as a
   * username and password and then attempt to verify the password. This method
   * saves the result of the authentication attempt as private state within the
   * LoginModule. <p/> <p/>
   * 
   * @return true if the authentication succeeded, or false if this
   *         <code>LoginModule</code> should be ignored.
   * @throws javax.security.auth.login.LoginException if the authentication
   *           fails
   */
  public boolean login() throws LoginException {
    
    pam_ = createPam();

    Callback[] callbacks = new Callback[2];
    String username = null;
    NameCallback nameCallback = new NameCallback("Username: ");
    callbacks[0] = nameCallback;
    String password = null;
    PasswordCallback passwordCallback = new PasswordCallback("Password: ", false);
    callbacks[1] = passwordCallback;

    try {
      callbackHandler_.handle(callbacks);
    } catch (IOException e) {
      LOGGER.error("IOException handling login: " + e.getMessage(), e);
      throw new LoginException(e.getMessage());
    } catch (UnsupportedCallbackException e) {
      LOGGER.error("UnsupportedCallbackException handling login: " + e.getMessage(), e);
      throw new LoginException(e.getMessage());
    }
    username = nameCallback.getName();
    password = String.copyValueOf(passwordCallback.getPassword());
    PamReturnValue pamReturnValue = pam_.authenticate(username, password);
    if (pamReturnValue.equals(PamReturnValue.PAM_SUCCESS)) {
      loginStatus_ = true;
      subject_.getPrincipals().add(new UserPrincipal(username));
//      sharedState_.put("javax.security.auth.login.name", username);
//      subject_.getPrivateCredentials().add(password);
    } else if (pamReturnValue.equals(PamReturnValue.PAM_ACCT_EXPIRED)) {
      throw new AccountExpiredException(PamReturnValue.PAM_ACCT_EXPIRED.toString());
    } else if (pamReturnValue.equals(PamReturnValue.PAM_CRED_EXPIRED)) {
      throw new CredentialExpiredException(PamReturnValue.PAM_CRED_EXPIRED.toString());
    } else {
      throw new FailedLoginException(pamReturnValue.toString());
    }
    return loginStatus_;
  }

  
  private Pam createPam() {
    String serviceName = (String) options_.get(SERVICE_NAME_OPTION);
    if (serviceName == null) {
      LOGGER.debug("No serviceName configured in JAAS configuration file. "
          + "Using default service name of "
          + Pam.DEFAULT_SERVICE_NAME);
      serviceName = Pam.DEFAULT_SERVICE_NAME;
    } else {
      LOGGER.debug("Using service name of "
          + serviceName
          + " from JAAS configuration file.");
    }
    return new Pam(serviceName);
  }

  /**
   * Method which logs out a <code>Subject</code>. <p/>
   * <p>
   * An implementation of this method might remove/destroy a Subject's
   * Principals and Credentials. <p/> <p/>
   * 
   * @return true if this method succeeded, or false if this
   *         <code>LoginModule</code> should be ignored.
   * @throws javax.security.auth.login.LoginException if the logout fails
   */
  public boolean logout() throws LoginException {
    return true;
  }

  /**
   * Initialize this LoginModule. <p/>
   * <p>
   * This method is called by the <code>LoginContext</code> after this
   * <code>LoginModule</code> has been instantiated. The purpose of this
   * method is to initialize this <code>LoginModule</code> with the relevant
   * information. If this <code>LoginModule</code> does not understand any of
   * the data stored in <code>sharedState</code> or <code>options</code>
   * parameters, they can be ignored. <p/> <p/>
   * 
   * @param subject the <code>Subject</code> to be authenticated.
   *          <p>
   * @param callbackHandler a <code>CallbackHandler</code> for communicating
   *          with the end user (prompting for usernames and passwords, for
   *          example).
   *          <p>
   * @param sharedState state shared with other configured LoginModules.
   *          <p>
   * @param options options specified in the login <code>Configuration</code>
   *          for this particular <code>LoginModule</code>.
   */
  public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map sharedState, Map options) {
    
    this.subject_ = subject;
    this.callbackHandler_ = callbackHandler;
    this.sharedState_ = sharedState;
    this.options_ = options;
  }

  /**
   * Get the underlying PAM object
   */
  public Pam getPam() {
    return pam_;
  }
  
}
