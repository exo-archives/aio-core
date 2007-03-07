package org.exoplatform.services.organization;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 21, 2003
 * Time: 3:22:54 PM
 *
 * This is the interface for the membership data model.
 */
public interface Membership {
  /**
   * the type of Membership allows distinction between 'hierarchical'
   * and 'supportive' Memberships.
   */
  public String getMembershipType();
  public void setMembershipType(String type);
  public String getId() ;
  public String getGroupId() ;
  public String getUserName() ;
}
