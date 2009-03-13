package org.exoplatform.services.database;

import java.util.List;

import junit.framework.TestCase;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.jdbc.CreateDBSchemaPlugin;
import org.exoplatform.services.database.jdbc.DBSchemaCreator;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov </a>
 * @version $Id: DBCreatorTest.java 5569 2006-05-17 12:48:47Z lautarul $
 */
public class DBCreatorTest extends TestCase {

//  private StandaloneContainer container;
  private DBSchemaCreator dbcreator;

  public void setUp() throws Exception {
    // >>>>> to avoid  two top-level container exception  
//  StandaloneContainer.setConfigurationPath("src/main/java/conf/standalone/test-configuration.xml");
//  container = StandaloneContainer.getInstance();
    PortalContainer container = PortalContainer.getInstance();
    dbcreator = (DBSchemaCreator) container.getComponentInstanceOfType(DBSchemaCreator.class);
  }

  public void testConf() throws Exception {
//    DBSchemaCreator creator = (DBSchemaCreator) container.getComponentInstanceOfType(DBSchemaCreator.class);
    List plugins = (List) dbcreator.getPlugins();
    assertFalse(plugins.isEmpty());

    assertTrue(plugins.get(0) instanceof CreateDBSchemaPlugin);
    CreateDBSchemaPlugin plugin = (CreateDBSchemaPlugin) plugins.get(0);

    assertNotNull(plugin.getDataSource());
    assertNotNull(plugin.getScript());
  }

  public void tearDown() throws Exception {
//    container.stop();
  }
}
