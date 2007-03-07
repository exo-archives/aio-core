/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.database.impl;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.cache.CacheProvider;
import org.hibernate.cfg.SettingsFactory;

/**
 * Created by The eXo Platform SARL        .
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady Azarenkov</a>
 * @version $Id: HibernateSettingsFactory.java 5332 2006-04-29 18:32:44Z geaz $
 * 
 * Hibernate's SettingsFactory for configure settings
 * @see SettingsFactory
 */
public class HibernateSettingsFactory extends SettingsFactory { 
	
	private static final String HIBERNATE_CACHE_PROPERTY = "hibernate.cache.provider_class" ;

	private ExoCacheProvider cacheProvider;
	
	public HibernateSettingsFactory(ExoCacheProvider cacheProvider) throws HibernateException {
		super();
		this.cacheProvider = cacheProvider;
	}
	
	protected CacheProvider createCacheProvider(Properties properties) {
		properties.setProperty(HIBERNATE_CACHE_PROPERTY, ExoCacheProvider.class.getName());
		return cacheProvider;
	}
	
}
