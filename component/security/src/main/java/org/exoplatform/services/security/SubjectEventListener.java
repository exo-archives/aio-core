package org.exoplatform.services.security;

import javax.security.auth.Subject;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 27 avr. 2004
 */
public interface SubjectEventListener {

  public void preSave(Subject subject, boolean isNew) throws Exception;
  public void postSave(Subject subject, boolean isNew) throws Exception ;

  public void preDelete(Subject subject) throws Exception ;
  public void postDelete(Subject subject) throws Exception ;

}
