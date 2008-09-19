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
package org.exoplatform.services.organization;

import java.util.Date;

/**
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: Aug 21, 2003 Time: 3:22:54 PM This
 * is the interface for the membership type data model. Note that after each set
 * method is called. The developer need to call @see
 * MembershipTypeHandler.saveMembershipType(..) or
 * 
 * @see MembershipTypeHandler.createMembershipType(..) to persist the change
 */
public interface MembershipType {
  /**
   * @return the name name of the membership type. The name of the membershipt
   *         type should be unique in the membership type database.
   */
  public String getName();

  /**
   * @param s The name of the membership type TODO This method should not be
   *          available to the developer as it should call only once, When a new
   *          membership type record is created
   */
  public void setName(String s);

  /**
   * @return The description of the membership type
   */
  public String getDescription();

  /**
   * @param s The new description of the membership type
   */
  public void setDescription(String s);

  /**
   * @return The owner of the membership
   */
  public String getOwner();

  /**
   * @param s The new owner of the membership
   */
  public void setOwner(String s);

  /**
   * @return The date that the membership type is saved to the database
   */
  public Date getCreatedDate();

  /**
   * @param d the created date TODO This method should be managed by the
   *          organization service and the developer should not called this
   *          method.
   */
  public void setCreatedDate(Date d);

  /**
   * @return The last time that an user modify the data of the membership type.
   */
  public Date getModifiedDate();

  /**
   * @param d the modified date TODO this field should be managed by the
   *          organization service and developer should not called this method.
   */
  public void setModifiedDate(Date d);
}
