/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.database.jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.configuration.ConfigurationException;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.log.ExoLogger;
/**
 * Created by The eXo Platform SARL        .
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady Azarenkov</a>
 * @version $Id: CreateDBSchemaPlugin.java 8017 2006-08-16 15:12:00Z peterit $
 */
public class CreateDBSchemaPlugin extends BaseComponentPlugin {
  
  protected static Log log = ExoLogger.getLogger("jcr.CreateDBSchemaPlugin");
  
  private String dataSource;
  private String script;

  public CreateDBSchemaPlugin(InitParams params) throws ConfigurationException {
    ValueParam dsParam = params.getValueParam("data-source");
    ValueParam scriptFileParam = params.getValueParam("script-file");
    ValueParam scriptParam = params.getValueParam("script");
    
    if (dsParam == null)  return;
    dataSource = dsParam.getValue();
    if(scriptParam != null) {
      script = scriptParam.getValue();
      return ;
    } 
    //ClassLoader cl = this.getClass().getClassLoader(); //Thread.currentThread().getContextClassLoader();
    ClassLoader cl = Thread.currentThread().getContextClassLoader();       
    InputStream  is = cl.getResourceAsStream(scriptFileParam.getValue());
    
    if(is == null) is = ClassLoader.getSystemResourceAsStream(scriptFileParam.getValue());

    if(is == null) {
      try {
        log.warn("Db script not found as system resource... Trying to search as file by path: " + scriptFileParam.getValue());
        is = new FileInputStream(scriptFileParam.getValue());
        log.info("Db script found as file by path: " + scriptFileParam.getValue());
      } catch(IOException e) {
        log.warn("Db script not found as file by path: " + scriptFileParam.getValue() + ". " + e);
      }
    }

    if(is == null) {
      try {
        log.warn("Db script not found as system resource... Trying to search as file by url: " + scriptFileParam.getValue());
        is = new URL(scriptFileParam.getValue()).openStream();
        log.info("Db script found as file by url: " + scriptFileParam.getValue());
      } catch(IOException e) {
        log.warn("Db script not found as file by url: " + scriptFileParam.getValue() + ". " + e);
      }
    }
    
    if(is == null) {
      throw new ConfigurationException("Could not open input stream for db script "+cl.getResource(scriptFileParam.getValue()));
    }
    
    try {
      byte[] buf = new byte[is.available()];
      is.read(buf);
      script = new String(buf);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        is.close();
      } catch (IOException e) {}
    }
    
  }

  public String getDataSource() { return dataSource; }

  public String getScript() { return script; }

}
