/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import java.util.List;

import org.exoplatform.container.component.BaseComponentPlugin ;
import org.exoplatform.container.xml.InitParams;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Jul 26, 2005
 */
public class AddHibernateMappingPlugin extends BaseComponentPlugin {

  List mapping_ ;
  
  public  AddHibernateMappingPlugin(InitParams params) {
    mapping_ = params.getValuesParam("hibernate.mapping").getValues();  
  }
  
  public List getMapping() { return mapping_ ; }
  
}
