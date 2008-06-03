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

package org.exoplatform.services.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by The eXo Platform SAS .<br/> In-memory registry of user's sessions
 * 
 * @author Gennady Azarenkov
 * @version $Id:$
 */

public final class ConversationRegistry {

  //public static final String         ADD_SESSION_EVENT    = "exo.service.security.login";
  //public static final String         REMOVE_SESSION_EVENT = "exo.service.security.logout";
  //public static final String SET_ATTRIBUTE_EVENT = "exo.service.security.setattribute";

  //private ListenerService            listenerService;

//  private static ThreadLocal<String> currentSessionHolder = new ThreadLocal<String>();
  
  private HashMap<String, Identity>  identities             = new HashMap<String, Identity>();

  private HashMap<Object, ConversationState>    states              = new HashMap<Object, ConversationState>();
  

  public ConversationRegistry() {
//    this.listenerService = listenerService;
  }
  
//  public void setCurrentIdentity(Identity toCurrentIdentity) {
//    currentSessionHolder.set(toCurrentIdentity.getUserId());
//  }
  
//  /**
//   * @return current session
//   */
//  public Identity getIdentity() {
//    return Identity.getCurrent();
//  }

//  /**
//   * @param key
//   * @return session
//   */
//  public Identity getIdentity(String key) {
//    return sessions.get(userId);
//  }

//  public Identity getIdentity(String userId) {
//    return identities.get(userId);
//  }
  
  public ConversationState getState(Object key) {
    return states.get(key);
  }
  
//  public void putIdentity(Identity identity) {
//    this.identities.put(identity.getUserId(), identity);
//  }
  

  /**
   * Sets the user's session to the registry and broadcasts ADD_SESSION_EVENT
   * message to interested listeners
   * 
   * @param key -
   *          a session identifier
   * @param session -
   *          the session
   * @param makeCurrent -
   *          store or not the session into thread local
   * @throws Exception
   */
  public void register(Object key, ConversationState state) throws Exception {
    
//    if(states.containsKey(key))
//      throw new Exception("The state ID already exists "+key);
    
    
    // supposed that "old" stored value (if any) is no more useful in registry
    // so we "push" it
    // for example - we have to do "login" register with username as a key
    // but it is possible to have more than one state (session) with the same UID
    // so old one will be pushed
    // possible drawback of this case if another "same" login occurs between
    // login and possible use - first state will be just missed
    states.put(key, state);
    
//    if(!identities.containsKey(state.getIdentity().getUserId()))
    identities.put(state.getIdentity().getUserId(), state.getIdentity());
    
  }
  
  public ConversationState unregister(Object key) {
    ConversationState s = states.remove(key);
    if(!states.containsKey(key))
      identities.remove(s.getIdentity().getUserId());
    return s;
  }

//  /**
//   * Removes session by key and broadcasts REMOVE_SESSION_EVENT to interested
//   * listeners
//   * 
//   * @param key
//   * @throws Exception
//   */
//  public void invalidateUser(String userId) throws Exception {
//    
//    identities.remove(userId);
//
//    // remove from aliases if any
//    for (Object key : getStates(userId))
//      states.remove(key);
//
//
//  }

//  void addAlias(String alias,
//                String userId) {
//    states.put(alias, userId);
//  }

  void clear() {
    identities.clear();
    states.clear();
//    currentSessionHolder.set(null);
  }
  
  public List <Object> getStateKeys(String userId) {
    
    ArrayList<Object> s = new ArrayList<Object>();
    for (Map.Entry<Object, ConversationState> a : states.entrySet()) {
      if (a.getValue().getIdentity().getUserId().equals(userId))
        s.add(a.getKey());
    }
    
    return s;
  }

}
