/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.database.impl;

import java.util.Properties;

import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.CacheProvider;
import org.hibernate.cache.Timestamper;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady Azarenkov
 *         </a>
 * @version $Id: ExoCacheProvider.java 5332 2006-04-29 18:32:44Z geaz $
 */

public class ExoCacheProvider implements CacheProvider {

	private CacheService cacheService;

	public ExoCacheProvider(CacheService cacheService) {
		this.cacheService = cacheService;

	}

	public Cache buildCache(String name, Properties properties)
			throws CacheException {
		try {
			ExoCache cache = cacheService.getCacheInstance(name);
			cache.setMaxSize(5000);
			return new ExoCachePlugin(cache);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CacheException("Cannot instanstiate cache provider");
		}
	}

	public long nextTimestamp() {
		return Timestamper.next();
	}

	/**
	 * Callback to perform any necessary initialization of the underlying cache
	 * implementation during SessionFactory construction.
	 * 
	 * @param properties
	 *            current configuration settings.
	 */
	public void start(Properties properties) throws CacheException {

	}

	/**
	 * Callback to perform any necessary cleanup of the underlying cache
	 * implementation during SessionFactory.close().
	 */
	public void stop() {

	}

	public boolean isMinimalPutsEnabledByDefault() {
		return true;
	}

}