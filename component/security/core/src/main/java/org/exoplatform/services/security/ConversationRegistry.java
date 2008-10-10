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

import org.exoplatform.services.listener.ListenerService;

/**
 * Created by The eXo Platform SAS .<br/> In-memory registry of user's sessions
 * 
 * @author Gennady Azarenkov
 * @version $Id:$
 */
public final class ConversationRegistry {

  /**
   * Storage for ConversationStates.
   */
  private HashMap<Object, ConversationState> states = new HashMap<Object, ConversationState>();

  /**
   * @see {@link IdentityRegistry}
   */
  private IdentityRegistry                   identityRegistry;

  /**
   * @see {@link ListenerService}
   */
  private ListenerService                    listenerService;

  /**
   * @param identityRegistry @see {@link IdentityRegistry}
   * @param listenerService @see {@link ListenerService}
   */
  public ConversationRegistry(IdentityRegistry identityRegistry, ListenerService listenerService) {
    this.identityRegistry = identityRegistry;
    this.listenerService = listenerService;
  }

  /**
   * Get ConversationState with specified key.
   * 
   * @param key the key.
   * @return ConversationState.
   */
  public ConversationState getState(Object key) {
    return states.get(key);
  }

  /**
   * Sets the user's session to the registry and broadcasts ADD_SESSION_EVENT
   * message to interested listeners.
   * 
   * @param key the session identifier.
   * @param session the session.
   * @param makeCurrent the store or not the session into thread local.
   */
  public void register(Object key, ConversationState state) {
    // supposed that "old" stored value (if any) is no more useful in registry
    // so we "push" it
    // for example - we have to do "login" register with username as a key
    // but it is possible to have more than one state (session) with the same
    // UID so old one will be pushed possible drawback of this case if
    // another "same" login occurs between
    // login and possible use - first state will be just missed
    states.put(key, state);
    try {
      listenerService.broadcast("exo.core.security.ConversationRegistry.register", this, state);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Remove ConversationStae with specified key. If there is no more
   * ConversationState for user then remove Identity from IdentityRegistry.
   * 
   * @param key the key.
   * @return removed ConversationState or null.
   */
  public ConversationState unregister(Object key) {
    ConversationState state = states.remove(key);

    if (state == null)
      return null;

    String userId = state.getIdentity().getUserId();

    // if no more conversation then remove identity.
    // TODO : temporary , now old code keeps one more conversation state with
    // key userId.
    // This state created by method broadcastAuthentication in
    // AuthenticationService
    List<Object> keys = getStateKeys(userId);
    if (keys.size() == 0 || (keys.size() == 1 && keys.get(0).equals(userId))) {
      identityRegistry.unregister(userId);
    }

    try {
      listenerService.broadcast("exo.core.security.ConversationRegistry.unregister", this, state);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return state;
  }

  /**
   * @param userId the user identifier.
   * @return list of users ConversationState.
   */
  public List<Object> getStateKeys(String userId) {
    ArrayList<Object> s = new ArrayList<Object>();
    for (Map.Entry<Object, ConversationState> a : states.entrySet()) {
      if (a.getValue().getIdentity().getUserId().equals(userId))
        s.add(a.getKey());
    }
    return s;
  }

  /**
   * Remove all ConversationStates.
   */
  void clear() {
    states.clear();
  }

}
