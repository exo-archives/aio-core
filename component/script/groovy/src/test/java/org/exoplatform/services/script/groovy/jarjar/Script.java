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
package org.exoplatform.services.script.groovy.jarjar;

import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;

import java.net.URL;
import java.io.IOException;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Script {

  /** . */
  private final String name;

  public Script(String name) {
    this.name = name;
  }

  public Object execute(Mapping mapping) {

    //
    JarJarClassLoader loader = new JarJarClassLoader(Thread.currentThread().getContextClassLoader());

    //
    mapping.configure(loader);

    // Obtain script class
    URL url = Thread.currentThread().getContextClassLoader().getResource("jarjar/" + name);
    Assert.assertNotNull(url);
    GroovyCodeSource gcs;
    try {
      gcs = new GroovyCodeSource(url);
    }
    catch (IOException e) {
      AssertionFailedError err = new AssertionFailedError();
      err.initCause(e);
      throw err;
    }
    Class testClass = loader.parseClass(gcs);

    // Instantiate script
    GroovyObject testObject;
    try {
      testObject = (GroovyObject)testClass.newInstance();
    }
    catch (Exception e) {
      AssertionFailedError err = new AssertionFailedError();
      err.initCause(e);
      throw err;
    }

    // Invoke finally
    return testObject.invokeMethod("run", new Object[0]);
  }
}
