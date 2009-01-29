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
package org.exoplatform.services.web.css;

import junit.framework.TestSuite;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Tests {

  public static TestSuite suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(org.exoplatform.services.web.css.comment.Tests.suite());
    suite.addTest(org.exoplatform.services.web.css.model.Tests.suite());
    suite.addTestSuite(CSSSerializerTestCase.class);
    suite.addTestSuite(OrientationFilterTestCase.class);
    return suite;
  }

}