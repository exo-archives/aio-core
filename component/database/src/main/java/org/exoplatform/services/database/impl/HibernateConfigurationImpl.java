/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.database.impl;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;

/**
 * Created by The eXo Platform SARL .
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady Azarenkov</a>
 * @version $Id: HibernateConfigurationImpl.java 5332 2006-04-29 18:32:44Z geaz $
 * 
 * Hibernate's Configuration. One per 'properties-param' entry in 
 * container configuration 
 */
public class HibernateConfigurationImpl  extends Configuration  {

	public HibernateConfigurationImpl(HibernateSettingsFactory settingsFactory) throws HibernateException {
		super(settingsFactory);
	}
}