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

import junit.framework.TestCase;
import junit.framework.AssertionFailedError;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class JarJarTest extends TestCase {

  public static AssertionFailedError error = null;

  public JarJarTest() {
  }

  public JarJarTest(String s) {
    super(s);
  }

  private void testTop(Script script) {
    Mapping m1 = new Mapping();
    m1.addMapping("a", "b");
    Mapping m2 = new Mapping();
    m2.addMapping("a", "prefix1.a");

    // Transform a top package into a top package
    assertEquals("b", script.execute(m1));

    // Transform a top package into a prefixed package
    assertEquals("prefix1.a", script.execute(m2));
  }

  public void testTopClassLitteral() throws Exception {
    testTop(new Script("classlitteral1.groovy"));
    testTop(new Script("classlitteral_1.groovy"));
    testTop(new Script("import1.groovy"));
  }

  private void testPrefix(Script script) throws Exception {
    Mapping m1 = new Mapping();
    m1.addMapping("prefix1", "prefix2");
    Mapping m2 = new Mapping();
    m2.addMapping("prefix1.a", "a");
    Mapping m3 = new Mapping();
    m3.addMapping("prefix1.a", "prefix2.b");
    Mapping m4 = new Mapping();
    m4.addMapping("prefix1.a", "prefix1.b");

    // Transform the top package prefix
    assertEquals("prefix2.a", script.execute(m1));

    // Transform the full prefixed package
    assertEquals("a", script.execute(m2));

    // Transform the full prefixed package
    assertEquals("prefix2.b", script.execute(m3));

    // Transform the full prefixed package
    assertEquals("prefix1.b", script.execute(m4));
  }

  public void testPrefixClassLitteral() throws Exception {
    testPrefix(new Script("classlitteral2.groovy"));
    testPrefix(new Script("classlitteral_2.groovy"));
    testPrefix(new Script("import2.groovy"));
  }
}
