package org.exoplatform.services.organization.jdbc;

import org.exoplatform.services.database.DBObject;
import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.annotation.TableField;
import org.exoplatform.services.organization.Membership;

@Table(
    name = "Membership" ,
    field = {
        @TableField(name = "membershipId", type = "string", length = 100, unique = true, nullable = false),
        @TableField(name = "membershipType", type = "string", length = 100),
        @TableField(name = "groupId", type = "string", length = 100),
        @TableField(name = "userName", type = "string", length = 500)
    }
)
public class MembershipImpl extends DBObject implements Membership {

  private String id = null;  
  private String membershipType = "member";
  private String groupId = null ;
  private String userName = null ;

  public MembershipImpl() {
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }  
  
  public String getMembershipType() { return membershipType; }
  public void setMembershipType(String type) { this.membershipType = type; }
  
  public String getUserName() { return userName; }
  public void setUserName(String user) { this.userName = user; }
  
  public String getGroupId() { return groupId ; }
  public void   setGroupId(String group) { this.groupId = group; }
  
  public String toString() { return "Membership[" + id + "]"; }
}