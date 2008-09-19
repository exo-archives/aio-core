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
package org.exoplatform.services.organization.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;

import org.exoplatform.services.database.DBObjectMapper;
import org.exoplatform.services.database.DBObjectQuery;
import org.exoplatform.services.database.ExoDatasource;
import org.exoplatform.services.database.StandardSQLDAO;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileEventListener;
import org.exoplatform.services.organization.UserProfileHandler;

/**
 * Created by The eXo Platform SAS Apr 7, 2007
 */
public class UserProfileDAOImpl extends StandardSQLDAO<UserProfileData> implements
    UserProfileHandler {

  protected static Log      log = ExoLogger.getLogger("organization:UserProfileDAOImpl");

  protected ListenerService listenerService_;

  public UserProfileDAOImpl(ListenerService lService,
                            ExoDatasource datasource,
                            DBObjectMapper<UserProfileData> mapper) {
    super(datasource, mapper, UserProfileData.class);
    listenerService_ = lService;
  }

  // This method should have a parameter, such as userName
  public UserProfile createUserProfileInstance() {
    return new UserProfileData("").getUserProfile();
  }

  public UserProfile createUserProfileInstance(String userName) {
    if (log.isDebugEnabled())
      log.debug("----------------Create Profile with userName = " + userName);
    return new UserProfileData(userName).getUserProfile();
  }

  public UserProfile findUserProfileByName(String userName) throws Exception {
    UserProfileData data = findUserProfileDataByName(userName);
    if (data != null)
      return data.getUserProfile();
    return null;
  }

  private UserProfileData findUserProfileDataByName(String userName) throws Exception {
    DBObjectQuery<UserProfileData> query = new DBObjectQuery<UserProfileData>(UserProfileData.class);
    query.addLIKE("USER_NAME", userName);
    return loadUnique(query.toQuery());
  }

  public Collection findUserProfiles() throws Exception {
    List<UserProfileData> allProfileData = new ArrayList<UserProfileData>();
    DBObjectQuery<UserProfileData> query = new DBObjectQuery<UserProfileData>(UserProfileData.class);
    loadInstances(query.toQuery(), allProfileData);
    List<UserProfile> allProfile = new ArrayList<UserProfile>();
    for (UserProfileData ele : allProfileData) {
      allProfile.add(ele.getUserProfile());
    }
    return allProfile;
  }

  public UserProfile removeUserProfile(String userName, boolean broadcast) throws Exception {
    UserProfileData userImpl = findUserProfileDataByName(userName);
    if (userImpl == null)
      return null;
    if (broadcast)
      listenerService_.broadcast("organization.userProfile.preDelete", this, userImpl);
    super.remove(userImpl);
    if (broadcast)
      listenerService_.broadcast("organization.userProfile.postDelete", this, userImpl);
    return userImpl.getUserProfile();
  }

  public void saveUserProfile(UserProfile profile, boolean broadcast) throws Exception {
    UserProfileData userImpl = findUserProfileDataByName(profile.getUserName());
    if (userImpl == null) {
      userImpl = new UserProfileData(profile.getUserName());
      userImpl.setUserProfile(profile);
      if (broadcast)
        listenerService_.broadcast("organization.userProfile.preCreate", this, userImpl);
      super.save(userImpl);
      if (broadcast)
        listenerService_.broadcast("organization.userProfile.postCreate", this, userImpl);
    } else {
      if (broadcast)
        listenerService_.broadcast("organization.userProfile.preUpdate", this, userImpl);
      userImpl.setUserProfile(profile);
      super.update(userImpl);
      if (broadcast)
        listenerService_.broadcast("organization.userProfile.postUpdate", this, userImpl);
    }
  }

  public void addUserProfileEventListener(UserProfileEventListener listener) {
    // TODO Auto-generated method stub

  }
}
