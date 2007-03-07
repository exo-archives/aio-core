package org.exoplatform.services.organization;

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 21, 2003
 * Time: 3:22:54 PM
 *
 * This is the interface for the group data model. Note that after each set method is called. 
 * The developer need to call @see GroupHandler.saveGroup(..) to persist the change
 */
public interface Group {
  /**
   * @return  the id of the group. The id should have the form /ancestor/parent/groupname
   */
	public String getId() ;
  /**
   * @return  the id of the parent group. if the parent id is null , it mean that  the group is at 
   * the first level. the child of  root  group.
   */
  public String getParentId() ;
  /**
   * @return the local name of the group
   */
  public String getGroupName();
  /**
   * @param name the local name for the group
   * TODO This method  should be called once only and should be set in the GroupHandler.createGroupInstance()
   * method
   */
  public void setGroupName(String name);
  /**
   * @return  The  display label of the group.
   */
  public String getLabel() ;
  /**
   * @param name  The new label of the group
   */
  public void   setLabel(String name) ;
  /**
   * @return  The  group description
   */
  public String getDescription() ;
  /**
   * @param desc  The new description of the group
   */
  public void   setDescription(String desc) ;
}
