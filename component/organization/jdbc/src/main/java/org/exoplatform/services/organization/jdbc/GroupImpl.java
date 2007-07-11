/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL        .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Oct 6, 2003
 * Time: 5:04:37 PM
 */
package org.exoplatform.services.organization.jdbc;

import org.exoplatform.services.database.DBObject;
import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.annotation.TableField;
import org.exoplatform.services.organization.Group;

@Table(
    name = "EXO_GROUP" ,
    field = {
        @TableField(name = "GROUP_ID", type = "string", length = 100, unique = true, nullable = false),
        @TableField(name = "PARENT_ID", type = "string", length = 100),
        @TableField(name = "GROUP_NAME", type = "string", length = 500),
        @TableField(name = "LABEL", type = "string", length = 500),
        @TableField(name = "GROUP_DESC", type = "string", length = 1000)
    }
)
public class GroupImpl extends DBObject implements Group {
  final static public String GROUP_ID_FIELD = "GROUP_ID" ;
  
  private String groupId  ;
  private String parentId  ;
  private String groupName ;
  private String label ;
  private String desc ;
  
  public GroupImpl()  {
  
  }
  
  public GroupImpl(String name) { groupName = name ; }
  
  public String getId() { return groupId; }
  public void setId(String id) { this.groupId = id; }
  
  public String getParentId() { return parentId ; }
  public void setParentId(String parentId) { this.parentId = parentId; }
  
  public String getGroupName() { return groupName; }
  public void setGroupName(String name) { this.groupName = name; }

  public String getLabel() { return label ; }
  public void   setLabel(String s) { label = s ; }
  
  public String getDescription() { return desc ; }
  public void   setDescription(String s)  { desc = s ; }

  public String toString() { return "Group[" + groupId + "|" + groupName + "]"; }
}