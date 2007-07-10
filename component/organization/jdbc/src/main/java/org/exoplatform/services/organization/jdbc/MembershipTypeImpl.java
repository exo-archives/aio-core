/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.jdbc;

import java.util.Date;

import org.exoplatform.services.database.DBObject;
import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.annotation.TableField;
import org.exoplatform.services.organization.MembershipType;

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
@Table(
    name = "EXO_MEMBERSHIP_TYPE" ,
    field = {
        @TableField(name = "MT_NAME", type = "string", length = 200, unique = true, nullable = false),
        @TableField(name = "MT_OWNER", type = "string", length = 100),
        @TableField(name = "MT_DESCRIPTION", type = "string", length = 500),
        @TableField(name = "CREATED_DATE", type = "date", length = 100),
        @TableField(name = "MODIFIED_DATE", type = "date", length = 100)
    }
)
public class MembershipTypeImpl extends DBObject implements MembershipType {

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
  
  public String   getName() { return name ; }
  public void     setName(String s) { name = s ; }

  public String   getDescription() { return description ; }
  public void     setDescription(String s) { description = s ; }

  public String   getOwner() { return owner ; }
  public void     setOwner(String s) { owner = s ; }

  public Date     getCreatedDate() { return createdDate ; }
  public void     setCreatedDate(Date d) { createdDate = d ; }

  public Date     getModifiedDate() { return modifiedDate ; }
  public void     setModifiedDate(Date d) { modifiedDate = d ;}
}