/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.impl;

import java.util.Date;

import org.exoplatform.services.organization.MembershipType;

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 *
 * @hibernate.class  table="EXO_MEMBERSHIP_TYPE"
 */
public class MembershipTypeImpl implements MembershipType {

  private String name ;
  private String description ;
  private String owner ;
  private Date createdDate ;
  private Date modifiedDate ;
  
  public MembershipTypeImpl() {
  }

  public MembershipTypeImpl(String name, String owner, String desc) {
    this.name = name ;
    this.owner = owner ;
    this.description = desc ;
  }
  
  /**
   * @hibernate.id  generator-class="assigned"
   **/
  public String   getName() { return name ; }
  public void     setName(String s) { name = s ; }

  /**
   * @hibernate.property
   **/
  public String   getDescription() { return description ; }
  public void     setDescription(String s) { description = s ; }

  /**
   * @hibernate.property
   **/
  public String   getOwner() { return owner ; }
  public void     setOwner(String s) { owner = s ; }

  /**
   * @hibernate.property
   **/
  public Date     getCreatedDate() { return createdDate ; }
  public void     setCreatedDate(Date d) { createdDate = d ; }

  /**
   * @hibernate.property
   **/
  public Date     getModifiedDate() { return modifiedDate ; }
  public void     setModifiedDate(Date d) { modifiedDate = d ;}
}