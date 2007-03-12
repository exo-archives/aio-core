/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.exception.ObjectNotFoundException;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.PropertiesParam;
import org.exoplatform.container.xml.Property;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.ObjectQuery;
import org.exoplatform.services.log.LogService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
/**
 * Created by The eXo Platform SARL .
 * 
 * @author Tuan Nguyen tuan08@users.sourceforge.net Date: Jun 14, 2003
 * @author dhodnett $Id: HibernateServiceImpl.java,v 1.3 2004/10/30 02:27:52
 *         tuan08 Exp $
 */
public class HibernateServiceImpl implements HibernateService, ComponentRequestLifecycle {
	private ThreadLocal<Session> threadLocal_;
	private Log log_;
  private HibernateConfigurationImpl conf_ ;
  private SessionFactory sessionFactory_;
  private HashSet<String> mappings_ = new HashSet<String>() ;

	public HibernateServiceImpl(InitParams initParams, LogService lservice,
                              CacheService cacheService) {
	  log_ = lservice.getLog(getClass());
	  threadLocal_ = new ThreadLocal<Session>();
	  PropertiesParam param = initParams.getPropertiesParam("hibernate.properties");
	  HibernateSettingsFactory settingsFactory =  
	    new HibernateSettingsFactory(new ExoCacheProvider(cacheService));
	  conf_ = new HibernateConfigurationImpl(settingsFactory);
	  Iterator properties = param.getPropertyIterator();
	  while (properties.hasNext()) {
	    Property p = (Property) properties.next();
	    conf_.setProperty(p.getName(), p.getValue());
	  }
    String  connectionURL = conf_.getProperty("hibernate.connection.url") ;
    connectionURL = connectionURL.replace("${java.io.tmpdir}", System.getProperty("java.io.tmpdir")) ;
    conf_.setProperty("hibernate.connection.url", connectionURL) ;
	}
	
	public void addPlugin(ComponentPlugin plugin) {
	  if(plugin instanceof AddHibernateMappingPlugin) {
	    AddHibernateMappingPlugin impl = (AddHibernateMappingPlugin) plugin ;  
	    try {
	      List path =  impl.getMapping() ;
	      ClassLoader cl = Thread.currentThread().getContextClassLoader();       
	      for (int i = 0; i < path.size(); i++) {
          String relativePath = (String) path.get(i) ;
          if(!mappings_.contains(relativePath)) {
            mappings_.add(relativePath) ;
            URL url = cl.getResource (relativePath);
            System.err.println("Add  Hibernate Mapping: " + relativePath);
            conf_.addURL(url);          
          }
	      }
	    } catch (Exception ex) {
	      ex.printStackTrace() ;
	    }      
	  }
	}
	
	public ComponentPlugin removePlugin(String name) {  return null; }
	public Collection getPlugins() {    return null;  } 
    
  public Configuration getHibernateConfiguration()  {
    return conf_ ;
  }
  
  /**
   * @return the SessionFactory
   */
  public SessionFactory getSessionFactory() {
    if (sessionFactory_ == null) {    
      sessionFactory_ = conf_.buildSessionFactory();
      new SchemaUpdate(conf_).execute(false, true);
    }
    return sessionFactory_;
  }

	public Session openSession() {
		Session currentSession = threadLocal_.get();      
		if (currentSession == null) {
			log_.debug("open new hibernate session in openSession()");
			currentSession = getSessionFactory().openSession();
			threadLocal_.set(currentSession);
		}  
		return currentSession;
	}

	public Session openNewSession() {
		Session currentSession = threadLocal_.get();
		if (currentSession != null) {
			closeSession(currentSession);
		}
		currentSession = getSessionFactory().openSession();
		threadLocal_.set(currentSession);
		return currentSession;
	}

	public void closeSession(Session session) {
		if (session == null)
			return;
		try {
			session.close();
			log_.debug("close hibernate session in openSession(Session session)");
		} catch (Throwable t) {
			log_.error("Error: ", t);
		}
		threadLocal_.set(null);
	}

	final public void closeSession() {
		Session s = threadLocal_.get();
		if (s != null) s.close();
		threadLocal_.set(null);
	}

	public Object findExactOne(Session session, String query, String id)	throws Exception {
		Object res = session.createQuery(query).setString(0, id).uniqueResult();
		if (res == null) {
			throw new ObjectNotFoundException("Cannot find the object with id: " + id);
		}
		return res;
	}

	public Object findOne(Session session, String query, String id)	throws Exception {
		List l = session.createQuery(query).setString(0, id).list();
		if (l.size() == 0) {
			return null;
		} else if (l.size() > 1) {
			throw new Exception("Expect only one object but found" + l.size());
		} else {
			return l.get(0);
		}
	}

	public Object findOne(Class clazz, Serializable id) throws Exception {
		Session session = openSession();
		Object obj = session.get(clazz, id);
		return obj;
	}

	public Object findOne(ObjectQuery q) throws Exception {
		Session session = openSession();
		List l = session.createQuery(q.getHibernateQuery()).list();
		if (l.size() == 0) {
			return null;
		} else if (l.size() > 1) {
			throw new Exception("Expect only one object but found" + l.size());
		} else {
			return l.get(0);
		}
	}

	public Object create(Object obj) throws Exception {
		Session session = openSession();
		session.save(obj);
		session.flush();
		return obj;
	}

	public Object update(Object obj) throws Exception {
		Session session = openSession();
		session.update(obj);
		session.flush();
		return obj;
	}

	public Object save(Object obj) throws Exception {
		Session session = openSession();
		session.merge(obj);
		session.flush();
		return obj;
	}

	public Object remove(Object obj) throws Exception {
		Session session = openSession();
		session.delete(obj);
		session.flush();
		return obj;
	}

	public Object remove(Class clazz, Serializable id) throws Exception {
		Session session = openSession();
		Object obj = session.get(clazz, id);
		session.delete(obj);
		session.flush();
		return obj;
	}

	public Object remove(Session session, Class clazz, Serializable id)	throws Exception {
		Object obj = session.get(clazz, id);
		session.delete(obj);
		return obj;
	}

  public void startRequest(ExoContainer container) {
    
  }

  public void endRequest(ExoContainer container)  {
    closeSession() ;
  }
}
