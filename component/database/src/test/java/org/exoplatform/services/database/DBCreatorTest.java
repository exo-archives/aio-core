
package org.exoplatform.services.database;

import java.util.List;

import junit.framework.TestCase;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.database.jdbc.CreateDBSchemaPlugin;
import org.exoplatform.services.database.jdbc.DBSchemaCreator;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov </a>
 * @version $Id: DBCreatorTest.java 5569 2006-05-17 12:48:47Z lautarul $
 */
public class DBCreatorTest extends TestCase {

  private StandaloneContainer container;
  
  public void setUp() throws Exception {
    
    StandaloneContainer.setConfigurationPath("src/java/conf/standalone/test-configuration.xml");
  	
    container = StandaloneContainer.getInstance();
  }
  
  public void testConf() throws Exception {
    DBSchemaCreator creator = (DBSchemaCreator)container.getComponentInstanceOfType(DBSchemaCreator.class);
    List plugins = (List)creator.getPlugins(); 
    assertFalse(plugins.isEmpty());
    
    assertTrue(plugins.get(0) instanceof CreateDBSchemaPlugin);
    CreateDBSchemaPlugin plugin = (CreateDBSchemaPlugin)plugins.get(0);
    
    assertNotNull(plugin.getDataSource());
    assertNotNull(plugin.getScript());
  }

  public void tearDown() throws Exception {
    container.stop();
  }
}
