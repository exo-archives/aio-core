package org.exoplatform.services.organization.jdbc;

import java.util.Date;

import org.exoplatform.services.database.DBObject;
import org.exoplatform.services.database.annotation.Table;
import org.exoplatform.services.database.annotation.TableField;
import org.exoplatform.services.organization.User;

@Table(
    name = "EXO_USER" ,
    field = {
        @TableField(name = "USER_NAME", type = "string", length = 200, unique = true, nullable = false),
        @TableField(name = "PASSWORD", type = "string", length = 100),
        @TableField(name = "FIRST_NAME", type = "string", length = 500),
        @TableField(name = "LAST_NAME", type = "string", length = 200),
        @TableField(name = "EMAIL", type = "string", length = 200),
        @TableField(name = "CREATED_DATE", type = "date", length = 100),
        @TableField(name = "LAST_LOGIN_TIME", type = "date", length = 100),
        @TableField(name = "ORGANIZATION_ID", type = "string", length = 100)
    }
)
public class UserImpl extends DBObject implements User {

  private String userName = null;
  private String password = null;
  private String firstName = null;
  private String lastName = null;
  private String email = null;
  private Date   createdDate ;
  private Date  lastLoginTime ;
  private String organizationId = null;
  
  public UserImpl() {
  }
  
  public UserImpl(String username) {
    this.userName = username;
  }

  public String getUserName() { return userName; }
  public void setUserName(String name) { this.userName = name; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
  
  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  
  public String getFullName() { return getFirstName() + " " + getLastName(); }
  @SuppressWarnings("unused") 
  public void setFullName(String s) {}

  
  public Date   getCreatedDate() { return createdDate ; } 
  public void   setCreatedDate(Date t) { createdDate = t ; }
 
  public Date getLastLoginTime() {  return lastLoginTime ; }
  public void setLastLoginTime(Date t) {   lastLoginTime = t ; }

  public String toString() {
    return "User[" + dbObjectId_ + "|" + userName + "]"+organizationId==null?"":("@"+organizationId);
  }

  public String getOrganizationId() { return organizationId; }
  
  public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }

}